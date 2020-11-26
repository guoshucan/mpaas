package ghost.framework.web.mvc.nginx.ui.plugin.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.web.angular1x.context.controller.ControllerBase;
import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.http.responseContent.DataResponse;
import ghost.framework.web.mvc.context.ModelAndView;
import ghost.framework.web.mvc.context.bind.annotation.Controller;
import ghost.framework.web.mvc.context.bind.annotation.ResponseBody;
import ghost.framework.web.mvc.nginx.ui.plugin.config.InitConfig;
import ghost.framework.web.mvc.nginx.ui.plugin.ext.AsycPack;
import ghost.framework.web.mvc.nginx.ui.plugin.service.ConfService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Date;

//@Controller("/adminPage/export")
@Controller("/api")
public class ExportController extends ControllerBase {
	@Autowired
	ConfService confService;

	@RequestMapping("")
	public ModelAndView index(HttpSession httpSession, ModelAndView modelAndView) {

		modelAndView.setViewName("/export/index");
		return modelAndView;
	}

	@RequestMapping("dataExport")
	public void dataExport(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String date = DateUtil.format(new Date(), "yyyy-MM-dd_HH-mm-ss");

		AsycPack asycPack = confService.getAsycPack();
		String json = JSONUtil.toJsonPrettyStr(asycPack);

		response.addHeader("Content-Type", "application/octet-stream");
		response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(date + ".json", "UTF-8")); // 设置文件名

		byte[] buffer = new byte[1024];
		BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(json.getBytes(Charset.forName("UTF-8"))));
		OutputStream os = response.getOutputStream();
		int i = bis.read(buffer);
		while (i != -1) {
			os.write(buffer, 0, i);
			i = bis.read(buffer);
		}
	}

	@RequestMapping(value = "dataImport")
	@ResponseBody
	public DataResponse dataImport(String json) {
		AsycPack asycPack = JSONUtil.toBean(json, AsycPack.class);

		confService.setAsycPack(asycPack);

		return DataResponse.success();
	}

	@RequestMapping("logExport")
	public void logExport(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		File file = new File(InitConfig.home + "log/nginxWebUI.log");
		if (file.exists()) {
			// 配置文件下载
			response.setHeader("content-type", "application/octet-stream");
			response.setContentType("application/octet-stream");
			// 下载文件能正常显示中文
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
			// 实现文件下载
			byte[] buffer = new byte[1024];
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			try {
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);
				OutputStream os = response.getOutputStream();
				int i = bis.read(buffer);
				while (i != -1) {
					os.write(buffer, 0, i);
					i = bis.read(buffer);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (bis != null) {
					try {
						bis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}