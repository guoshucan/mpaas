package ghost.framework.web.mvc.nginx.ui.plugin.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.web.angular1x.context.controller.ControllerBase;
import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.http.responseContent.DataResponse;
import ghost.framework.web.mvc.context.ModelAndView;
import ghost.framework.web.mvc.context.bind.annotation.Controller;
import ghost.framework.web.mvc.context.bind.annotation.ResponseBody;
import ghost.framework.web.mvc.nginx.ui.plugin.config.InitConfig;
import ghost.framework.web.mvc.nginx.ui.plugin.entity.Cert;
import ghost.framework.web.mvc.nginx.ui.plugin.service.SettingService;
import ghost.framework.web.mvc.nginx.ui.plugin.utils.SystemTool;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//@Controller("/adminPage/cert")
@Controller("/api")
public class CertController extends ControllerBase {
	@Autowired
	SettingService settingService;

	Boolean isInApply = false;

	@RequestMapping("")
	public ModelAndView index(HttpSession httpSession, ModelAndView modelAndView) {
//		List<Cert> certs = sqlHelper.findAll(Cert.class);
		List<Cert> certs = sessionFactory.findAll(Cert.class);
		modelAndView.addObject("certs", certs);
		modelAndView.setViewName("/cert/index");
		return modelAndView;
	}

	@RequestMapping("addOver")
	@ResponseBody
	public DataResponse addOver(Cert cert) throws SQLException{
//		sqlHelper.insertOrUpdate(cert);
		sessionFactory.insert(cert);
		return DataResponse.success();
	}

	@RequestMapping("setAutoRenew")
	@ResponseBody
	public DataResponse setAutoRenew(Cert cert) throws SQLException{
//		sqlHelper.updateById(cert);
		sessionFactory.update(cert);
		return DataResponse.success();
	}

	@RequestMapping("detail")
	@ResponseBody
	public DataResponse detail(String id) throws SQLException {
		return DataResponse.success(sessionFactory.findById(Cert.class, id));
	}

	@RequestMapping("del")
	@ResponseBody
	public DataResponse del(String id) throws SQLException {
//		Cert cert = sqlHelper.findById(id, Cert.class);
		Cert cert = sessionFactory.findById(Cert.class, id);
		if (cert.getKey() != null) {
			FileUtil.del(cert.getKey());
		}
		if (cert.getPem() != null) {
			FileUtil.del(cert.getPem());
		}
//		sqlHelper.deleteById(id, Cert.class);
		sessionFactory.deleteById(Cert.class, id);
		return DataResponse.success();
	}

	@RequestMapping("apply")
	@ResponseBody
	public DataResponse apply(String id, String type) throws SQLException{
		if (!SystemTool.isLinux()) {
//			return DataResponse.error(2, m.get("certStr.error2"));
		}
//		Cert cert = sqlHelper.findById(id, Cert.class);
		Cert cert = sessionFactory.findById(Cert.class, id);
		if (cert.getDnsType() == null) {
//			return DataResponse.error(3, m.get("certStr.error3"));
		}

		if (isInApply) {
//			return DataResponse.error(4, m.get("certStr.error4"));
		}
		isInApply = true;

		String rs = "";
		try {
			// 设置dns账号
			setEnv(cert);
			
			String cmd = "";
			if (type.equals("issue") || StrUtil.isEmpty(cert.getPem())) {
				// 申请
				String dnsType = "";
				if(cert.getDnsType().equals("ali")) {
					dnsType = "dns_ali";
				}else if(cert.getDnsType().equals("dp")) {
					dnsType = "dns_dp";
				}
				cmd = InitConfig.acmeSh + " --issue --dns " + dnsType + " -d " + cert.getDomain();
			} else if (type.equals("renew")) {
				// 续签,以第一个域名为证书名
				String domain = cert.getDomain().split(",")[0];
				cmd = InitConfig.acmeSh + " --renew --force -d " + domain;
			}
			logger.info(cmd);

			rs = RuntimeUtil.execForStr(cmd);

			logger.info(rs);

		} catch (Exception e) {
			e.printStackTrace();
			rs = e.getMessage();
		}

		// 申请完后,马上备份.acme.sh,以便在升级docker后可还原
		FileUtil.del(InitConfig.home + ".acme.sh");
		FileUtil.copy("/root/.acme.sh", InitConfig.home, true);

		if (rs.contains("Your cert is in")) {
			try {
				// 将证书复制到/home/nginxWebUI
				String domain = cert.getDomain().split(",")[0];
				String certDir = "/root/.acme.sh/" + domain + "/";

				String dest = InitConfig.home + "cert/" + domain + ".fullchain.cer";
				FileUtil.copy(new File(certDir + "fullchain.cer"), new File(dest), true);
				cert.setPem(dest);

				dest = InitConfig.home + "cert/" + domain + ".key";
				FileUtil.copy(new File(certDir + domain + ".key"), new File(dest), true);
				cert.setKey(dest);

				cert.setMakeTime(System.currentTimeMillis());
//				sqlHelper.updateById(cert);
				sessionFactory.update(cert);
			} catch (Exception e) {
				e.printStackTrace();
			}
			isInApply = false;
			return DataResponse.success();
		} else {

			isInApply = false;
			return DataResponse.error(1, rs.replace("\n", "<br>"));
		}
	}
	
	private void setEnv(Cert cert) {
		List<String> list = new ArrayList<>();
		list.add("UPGRADE_HASH='" + UUID.randomUUID().toString().replace("-", "") + "'");
		if (cert.getDnsType().equals("ali")) {
			list.add("SAVED_Ali_Key='" + cert.getAliKey() + "'");
			list.add("SAVED_Ali_Secret='" + cert.getAliSecret() + "'");
		}
		if (cert.getDnsType().equals("dp")) {
			list.add("SAVED_DP_Id='" + cert.getDpId() + "'");
			list.add("SAVED_DP_Key='" + cert.getDpKey() + "'");
		}
		list.add("USER_PATH='/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/snap/bin'");

		FileUtil.writeLines(list, new File(InitConfig.acmeSh.replace("/acme.sh", "/account.conf")), Charset.defaultCharset());
	}



}
