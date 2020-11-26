package ghost.framework.web.mvc.nginx.ui.plugin.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.base.ApplicationHome;
import ghost.framework.web.angular1x.context.controller.ControllerBase;
import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.bind.annotation.RequestParam;
import ghost.framework.web.context.http.multipart.MultipartFile;
import ghost.framework.web.context.http.responseContent.DataResponse;
import ghost.framework.web.mvc.context.ModelAndView;
import ghost.framework.web.mvc.context.bind.annotation.Controller;
import ghost.framework.web.mvc.context.bind.annotation.ResponseBody;
import ghost.framework.web.mvc.nginx.ui.plugin.config.InitConfig;
import ghost.framework.web.mvc.nginx.ui.plugin.entity.Remote;
import ghost.framework.web.mvc.nginx.ui.plugin.utils.AsyncUtils;
import ghost.framework.web.mvc.nginx.ui.plugin.utils.SystemTool;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

//@RequestMapping("")
@Controller("/api")
public class MainController extends ControllerBase {
	@Autowired
	AsyncUtils asyncUtils;

	@RequestMapping("")
	public ModelAndView index(ModelAndView modelAndView, String keywords) {

		modelAndView.setViewName("redirect:/admin/");
		return modelAndView;
	}

	@ResponseBody
	@RequestMapping("/upload")
	public DataResponse upload(@RequestParam("file") MultipartFile file, HttpSession httpSession) {
		try {
			File temp = new File(FileUtil.getTmpDir() + "/" + file.getOriginalFilename());
			file.transferTo(temp);

			// 移动文件
			File dest = new File(InitConfig.home + "cert/" + file.getOriginalFilename());
			FileUtil.move(temp, dest, true);

			String localType = (String) httpSession.getAttribute("localType");
			if ("remote".equals(localType)) {
				Remote remote = (Remote) httpSession.getAttribute("remote");

				HashMap<String, Object> paramMap = new HashMap<>();
				paramMap.put("file", dest);

				String rs = HttpUtil.post(remote.getProtocol() + "://" + remote.getIp() + ":" + remote.getPort() + "/upload", paramMap);
				DataResponse jsonResult = JSONUtil.toBean(rs, DataResponse.class);
				FileUtil.del(dest);
				return jsonResult;
			}

			return DataResponse.success(dest.getPath().replace("\\", "/"));
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}

		return DataResponse.error(1);
	}

	@ResponseBody
	@RequestMapping("/autoUpdate")
	public DataResponse autoUpdate(String url) {
		if (!SystemTool.isLinux()) {
			return DataResponse.error(1, "m.get('commonStr.updateTips')");
		}
		ApplicationHome home = new ApplicationHome(getClass());
		File jar = home.getSource();
		String path = jar.getParent() + "/nginxWebUI.jar.update";
		logger.info("download:" + path);
		HttpUtil.downloadFile(url, path);
		asyncUtils.run(path);
		return DataResponse.success();
	}
}