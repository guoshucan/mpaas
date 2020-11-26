package ghost.framework.web.mvc.nginx.ui.plugin.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.web.angular1x.context.controller.ControllerBase;
import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.http.responseContent.DataResponse;
import ghost.framework.web.mvc.context.ModelAndView;
import ghost.framework.web.mvc.context.bind.annotation.Controller;
import ghost.framework.web.mvc.context.bind.annotation.ResponseBody;
import ghost.framework.web.mvc.nginx.ui.plugin.config.InitConfig;
import ghost.framework.web.mvc.nginx.ui.plugin.entity.Www;
import ghost.framework.web.mvc.nginx.ui.plugin.service.WwwService;
import ghost.framework.web.mvc.nginx.ui.plugin.utils.SystemTool;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.List;

//@Controller("/adminPage/www")
@Controller("/api")
public class WwwController extends ControllerBase {
	@Autowired
	WwwService wwwService;

	@RequestMapping("")
	public ModelAndView index(HttpSession httpSession, ModelAndView modelAndView) {
		List<Www> wwwList = sessionFactory.findAll(Www.class);
//		modelAndView.addObject("list", sqlHelper.findAll(new Sort("dir", Direction.ASC), Www.class));
		modelAndView.addObject("list", wwwList);
		modelAndView.setViewName("/www/index");
		return modelAndView;
	}

	@RequestMapping("addOver")
	@ResponseBody
	public DataResponse addOver(Www www) {
		if (wwwService.hasName(www.getName())) {
			return DataResponse.error(1, "m.get('wwwStr.sameName')");
		}
		try {
			String dir = InitConfig.home + "wwww/" + www.getName();
			FileUtil.del(dir);
			try {
				ZipUtil.unzip(www.getDir(), dir);
			} catch (Exception e) {
				// 默认UTF-8下不能解压中文字符, 尝试使用gbk
				ZipUtil.unzip(www.getDir(), dir, Charset.forName("GBK"));
			}

			FileUtil.del(www.getDir());
			if (!SystemTool.isWindows()) {
				www.setDir(dir);
			} else {
				String classPath = getClassPath();
				www.setDir(classPath.split(":")[0] + ":" + dir);
			}

//			sqlHelper.insertOrUpdate(www);
			sessionFactory.insert(www);
			return DataResponse.success();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return DataResponse.error(2, "m.get('wwwStr.zipError')");
	}

	@RequestMapping("rename")
	@ResponseBody
	public DataResponse rename(Www www) throws SQLException {
		if (wwwService.hasName(www.getName())) {
			return DataResponse.error(1, "m.get('wwwStr.sameName')");
		}

		// 修改名称, 也要修改文件夹名
//		Www wwwOrg = sqlHelper.findById(www.getId(), Www.class);
		Www wwwOrg = sessionFactory.findById(Www.class, www.getId());
		FileUtil.rename(new File(wwwOrg.getDir()), InitConfig.home + "wwww/" + www.getName(), true);

		www.setDir(InitConfig.home + "wwww/" + www.getName());
//		sqlHelper.insertOrUpdate(www);
		sessionFactory.update(www);
		return DataResponse.success();

	}

	@RequestMapping("update")
	@ResponseBody
	public DataResponse update(Www www) {

		try {
			String dir = InitConfig.home + "wwww/" + www.getName();
			FileUtil.del(dir);
			try {
				ZipUtil.unzip(www.getDir(), dir);
			} catch (Exception e) {
				// 默认UTF-8下不能解压中文字符, 尝试使用gbk
				ZipUtil.unzip(www.getDir(), dir, Charset.forName("GBK"));
			}

			FileUtil.del(www.getDir());
			if (!SystemTool.isWindows()) {
				www.setDir(dir);
			} else {
				String classPath = getClassPath();
				www.setDir(classPath.split(":")[0] + ":" + dir);
			}
//			sqlHelper.insertOrUpdate(www);
			sessionFactory.update(www);
			return DataResponse.success();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return DataResponse.error(1, "m.get('wwwStr.zipError')");
	}

	@RequestMapping("del")
	@ResponseBody
	public DataResponse del(String id) throws SQLException {
//		Www www = sqlHelper.findById(id, Www.class);
		Www www = sessionFactory.findById(Www.class, id);
//		sqlHelper.deleteById(id, Www.class);
		sessionFactory.deleteById(Www.class, id);
		if (StrUtil.isNotEmpty(www.getDir()) && www.getDir() != "/") {
			FileUtil.del(www.getDir());
		}

		return DataResponse.success();
	}

	@RequestMapping("detail")
	@ResponseBody
	public DataResponse detail(String id) throws SQLException {
//		Www www = sqlHelper.findById(id, Www.class);
		Www www = sessionFactory.findById(Www.class, id);
		return DataResponse.success(www);
	}

	public String getClassPath() throws Exception {
		try {
			String strClassName = getClass().getName();
			String strPackageName = "";
			if (getClass().getPackage() != null) {
				strPackageName = getClass().getPackage().getName();
			}
			String strClassFileName = "";
			if (!"".equals(strPackageName)) {
				strClassFileName = strClassName.substring(strPackageName.length() + 1, strClassName.length());
			} else {
				strClassFileName = strClassName;
			}
			URL url = null;
			url = getClass().getResource(strClassFileName + ".class");
			String strURL = url.toString();
			strURL = strURL.substring(strURL.indexOf('/') + 1, strURL.lastIndexOf('/'));
			// 返回当前类的路径，并且处理路径中的空格，因为在路径中出现的空格如果不处理的话，
			// 在访问时就会从空格处断开，那么也就取不到完整的信息了，这个问题在web开发中尤其要注意
			return strURL.replaceAll("%20", " ");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}
}