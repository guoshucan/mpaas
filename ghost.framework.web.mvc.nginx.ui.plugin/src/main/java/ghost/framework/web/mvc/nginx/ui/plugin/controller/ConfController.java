package ghost.framework.web.mvc.nginx.ui.plugin.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.injection.Value;
import ghost.framework.web.angular1x.context.controller.ControllerBase;
import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.http.responseContent.DataResponse;
import ghost.framework.web.mvc.context.ModelAndView;
import ghost.framework.web.mvc.context.bind.annotation.Controller;
import ghost.framework.web.mvc.context.bind.annotation.ResponseBody;
import ghost.framework.web.mvc.nginx.ui.plugin.config.VersionConfig;
import ghost.framework.web.mvc.nginx.ui.plugin.ext.ConfExt;
import ghost.framework.web.mvc.nginx.ui.plugin.ext.ConfFile;
import ghost.framework.web.mvc.nginx.ui.plugin.service.ConfService;
import ghost.framework.web.mvc.nginx.ui.plugin.service.ServerService;
import ghost.framework.web.mvc.nginx.ui.plugin.service.SettingService;
import ghost.framework.web.mvc.nginx.ui.plugin.service.UpstreamService;
import ghost.framework.web.mvc.nginx.ui.plugin.utils.SystemTool;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

//@Controller("/adminPage/conf")
@Controller("/api")
public class ConfController extends ControllerBase {
	final UpstreamService upstreamService;
	final SettingService settingService;
	final ServerService serverService;
	final ConfService confService;
	final MainController mainController;

	@Autowired
	VersionConfig versionConfig;
	@Value("${project.version}")
	String currentVersion;

	public ConfController(UpstreamService upstreamService, SettingService settingService, ServerService serverService, ConfService confService, MainController mainController) {
		this.upstreamService = upstreamService;
		this.settingService = settingService;
		this.serverService = serverService;
		this.confService = confService;
		this.mainController = mainController;
	}

	@RequestMapping("")
	public ModelAndView index(ModelAndView modelAndView) {

		String nginxPath = settingService.get("nginxPath");
		modelAndView.addObject("nginxPath", nginxPath);

		String nginxExe = settingService.get("nginxExe");
		modelAndView.addObject("nginxExe", nginxExe);

		String nginxDir = settingService.get("nginxDir");
		modelAndView.addObject("nginxDir", nginxDir);

		String decompose = settingService.get("decompose");
		modelAndView.addObject("decompose", decompose);

		modelAndView.setViewName("/conf/index");
		return modelAndView;
	}

	@RequestMapping(value = "nginxStatus")
	@ResponseBody
	public DataResponse nginxStatus() {
//		if (NginxUtils.isRun()) {
//			return DataResponse.success(m.get("confStr.nginxStatus") + "：<span class='green'>" + m.get("confStr.running") + "</span>");
//		} else {
//			return DataResponse.success(m.get("confStr.nginxStatus") + "：<span class='red'>" + m.get("confStr.stopped") + "</span>");
//		}
		return DataResponse.success("nginxStatus");
	}

	@RequestMapping(value = "replace")
	@ResponseBody
	public DataResponse replace(String json) {
		JSONObject jsonObject = JSONUtil.parseObj(json);
		String nginxPath = jsonObject.getStr("nginxPath");
		String nginxContent = jsonObject.getStr("nginxContent");
		List<String> subContent = jsonObject.getJSONArray("subContent").toList(String.class);
		List<String> subName = jsonObject.getJSONArray("subName").toList(String.class);

		if (nginxPath == null) {
			nginxPath = settingService.get("nginxPath");
		}
		if (!FileUtil.exist(nginxPath)) {
			return DataResponse.error(1, "m.get('confStr.error1')");
		}
		if (FileUtil.isDirectory(nginxPath)) {
			return DataResponse.error(2, "m.get('confStr.error2')");
		}

		try {
			confService.replace(nginxPath, nginxContent, subContent, subName);
			return DataResponse.success("m.get('confStr.replaceSuccess')");
		} catch (Exception e) {
			e.printStackTrace();

			return DataResponse.error(3, "m.get('confStr.error3')" + ":" + e.getMessage());
		}

	}

	@RequestMapping(value = "check")
	@ResponseBody
	public DataResponse check(String nginxPath, String nginxExe, String nginxDir) {
		if (nginxPath == null) {
			nginxPath = settingService.get("nginxPath");
		}
		if (nginxExe == null) {
			nginxExe = settingService.get("nginxExe");
		}
		if (nginxDir == null) {
			nginxDir = settingService.get("nginxDir");
		}

		String rs;
		String cmd = null;
		try {
			if (SystemTool.isWindows()) {
				cmd = nginxExe + " -t -c " + nginxPath + " -p " + nginxDir;
			} else {
				cmd = nginxExe + " -t";
				if (nginxExe.contains("/")) {
					cmd = cmd + " -c " + nginxPath + " -p " + nginxDir;
				}
			}
			rs = RuntimeUtil.execForStr(cmd);
		} catch (Exception e) {
			e.printStackTrace();
			rs = e.getMessage().replace("\n", "<br>");
		}

		cmd = "<span class='blue'>" + cmd + "</span>";
		if (rs.contains("successful")) {
			return DataResponse.success(cmd + "<br>" + "m.get('confStr.verifySuccess')" + "<br>" + rs.replace("\n", "<br>"));
		} else {
			return DataResponse.error(1, cmd + "<br>" + "m.get('confStr.verifyFail')" + "<br>" + rs.replace("\n", "<br>"));
		}

	}

	@RequestMapping(value = "saveCmd")
	@ResponseBody
	public DataResponse saveCmd(String nginxPath, String nginxExe, String nginxDir) {
		nginxPath = nginxPath.replace("\\", "/");
		settingService.set("nginxPath", nginxPath);

		nginxExe = nginxExe.replace("\\", "/");
		settingService.set("nginxExe", nginxExe);

		nginxDir = nginxDir.replace("\\", "/");
		settingService.set("nginxDir", nginxDir);

		return DataResponse.success();
	}

	@RequestMapping(value = "reload")
	@ResponseBody
	public synchronized DataResponse reload(String nginxPath, String nginxExe, String nginxDir) {
		if (nginxPath == null) {
			nginxPath = settingService.get("nginxPath");
		}
		if (nginxExe == null) {
			nginxExe = settingService.get("nginxExe");
		}
		if (nginxDir == null) {
			nginxDir = settingService.get("nginxDir");
		}

		try {
			String rs;
			String cmd;
			if (SystemTool.isWindows()) {
				cmd = nginxExe + " -s reload -c " + nginxPath + " -p " + nginxDir;
			} else {
				cmd = nginxExe + " -s reload";
				if (nginxExe.contains("/") && StrUtil.isNotEmpty(nginxPath) && StrUtil.isNotEmpty(nginxDir)) {
					cmd = cmd + " -c " + nginxPath + " -p " + nginxDir;
				}
			}
			rs = RuntimeUtil.execForStr(cmd);

			cmd = "<span class='blue'>" + cmd + "</span>";
			if (StrUtil.isEmpty(rs) || rs.contains("signal process started")) {
				return DataResponse.success(cmd + "<br>" + "m.get('confStr.reloadSuccess')" + "<br>" + rs.replace("\n", "<br>"));
			} else {
				if (rs.contains("The system cannot find the file specified") || rs.contains("nginx.pid") || rs.contains("PID")) {
					rs = rs + "m.get('confStr.mayNotRun')";
				}

				return DataResponse.error(1, cmd + "<br>" + "m.get('confStr.reloadFail')" + "<br>" + rs.replace("\n", "<br>"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return DataResponse.error(2, "m.get('confStr.reloadFail')" + "<br>" + e.getMessage().replace("\n", "<br>"));
		}
	}

	@RequestMapping(value = "start")
	@ResponseBody
	public DataResponse start(String nginxPath, String nginxExe, String nginxDir) {
		if (nginxPath == null) {
			nginxPath = settingService.get("nginxPath");
		}
		if (nginxExe == null) {
			nginxExe = settingService.get("nginxExe");
		}
		if (nginxDir == null) {
			nginxDir = settingService.get("nginxDir");
		}
		try {
			String rs = "";
			String cmd;
			if (SystemTool.isWindows()) {
				cmd = "cmd /c start nginx.exe" + " -c " + nginxPath + " -p " + nginxDir;
				RuntimeUtil.exec(new String[]{}, new File(nginxDir), cmd);
			} else {
				cmd = nginxExe;
				if (nginxExe.contains("/") && StrUtil.isNotEmpty(nginxPath) && StrUtil.isNotEmpty(nginxDir)) {
					cmd = cmd + " -c " + nginxPath + " -p " + nginxDir;
				}
				rs = RuntimeUtil.execForStr(cmd);
			}

			cmd = "<span class='blue'>" + cmd + "</span>";
			if (StrUtil.isEmpty(rs) || rs.contains("signal process started")) {
				return DataResponse.success(cmd + "<br>" + "m.get('confStr.startSuccess')" + "<br>" + rs.replace("\n", "<br>"));
			} else {
				return DataResponse.error(1, cmd + "<br>" + "m.get('confStr.startFail')" + "<br>" + rs.replace("\n", "<br>"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return DataResponse.error(2, "m.get('confStr.startFail')" + "<br>" + e.getMessage().replace("\n", "<br>"));
		}
	}

	@RequestMapping(value = "stop")
	@ResponseBody
	public DataResponse stop(String nginxExe, String nginxDir) {
		if (nginxExe == null) {
			nginxExe = settingService.get("nginxExe");
		}
		if (nginxDir == null) {
			nginxDir = settingService.get("nginxDir");
		}
		try {
			String rs;
			String cmd;
			if (SystemTool.isWindows()) {
				cmd = "taskkill /im nginx.exe /f";
			} else {
				cmd = nginxExe + " -s stop";
				if (nginxExe.contains("/") && StrUtil.isNotEmpty(nginxDir)) {
					cmd = cmd + " -p " + nginxDir;
				}
			}
			rs = RuntimeUtil.execForStr(cmd);

			cmd = "<span class='blue'>" + cmd + "</span>";
			if (StrUtil.isEmpty(rs) || rs.contains("已终止进程") || rs.toLowerCase().contains("terminated process")) {
				return DataResponse.success(cmd + "<br>" + "m.get('confStr.stopSuccess')" + "<br>" + rs.replace("\n", "<br>"));
			} else {
				return DataResponse.error(1, cmd + "<br>" + "m.get('confStr.stopFail')" + "<br>" + rs.replace("\n", "<br>"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return DataResponse.error(2, "m.get('confStr.stopFail')" + "<br>" + e.getMessage().replace("\n", "<br>"));
		}
	}

	@RequestMapping(value = "loadConf")
	@ResponseBody
	public DataResponse loadConf() {
		String decompose = settingService.get("decompose");
		ConfExt confExt = confService.buildConf(StrUtil.isNotEmpty(decompose) && decompose.equals("true"));
		return DataResponse.success(confExt);
	}

	@RequestMapping(value = "loadOrg")
	@ResponseBody
	public DataResponse loadOrg(String nginxPath) {
		String decompose = settingService.get("decompose");
		ConfExt confExt = confService.buildConf(StrUtil.isNotEmpty(decompose) && decompose.equals("true"));

		if (StrUtil.isNotEmpty(nginxPath) && FileUtil.exist(nginxPath) && FileUtil.isFile(nginxPath)) {
			String orgStr = FileUtil.readString(nginxPath, StandardCharsets.UTF_8);
			confExt.setConf(orgStr);

			for (ConfFile confFile : confExt.getFileList()) {
				confFile.setConf("");

				String filePath = nginxPath.replace("nginx.conf", "conf.d/" + confFile.getName());
				if (FileUtil.exist(filePath)) {
					confFile.setConf(FileUtil.readString(filePath, StandardCharsets.UTF_8));
				}
			}

			return DataResponse.success(confExt);
		} else {
			if (FileUtil.isDirectory(nginxPath)) {
				return DataResponse.error(1, "m.get('confStr.error2')");
			}

			return DataResponse.error(2, "m.get('confStr.notExist')");
		}

	}

	@RequestMapping(value = "decompose")
	@ResponseBody
	public DataResponse decompose(String decompose) {
		settingService.set("decompose", decompose);
		return DataResponse.success();
	}

	@RequestMapping(value = "update")
	@ResponseBody
	public DataResponse update() {
		versionConfig.getNewVersion();
		if (Integer.parseInt(currentVersion.replace(".", "").replace("v", "")) < Integer.parseInt(versionConfig.getVersion().getVersion().replace(".", "").replace("v", ""))) {
			mainController.autoUpdate(versionConfig.getVersion().getUrl());
			return DataResponse.success("m.get('confStr.updateSuccess')");
		} else {
			return DataResponse.success("m.get('confStr.noNeedUpdate')");
		}
	}
}