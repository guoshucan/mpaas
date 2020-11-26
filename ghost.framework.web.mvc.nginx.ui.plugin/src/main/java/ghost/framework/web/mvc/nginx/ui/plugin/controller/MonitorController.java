package ghost.framework.web.mvc.nginx.ui.plugin.controller;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.web.angular1x.context.controller.ControllerBase;
import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.http.responseContent.DataResponse;
import ghost.framework.web.mvc.context.ModelAndView;
import ghost.framework.web.mvc.context.bind.annotation.Controller;
import ghost.framework.web.mvc.context.bind.annotation.ResponseBody;
import ghost.framework.web.mvc.nginx.ui.plugin.model.MonitorInfo;
import ghost.framework.web.mvc.nginx.ui.plugin.service.MonitorService;
import ghost.framework.web.mvc.nginx.ui.plugin.service.SettingService;
import ghost.framework.web.mvc.nginx.ui.plugin.utils.SystemTool;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@RequestMapping("/adminPage/monitor")
@Controller("/api")
public class MonitorController extends ControllerBase {
	@Autowired
	MonitorService monitorService;
	@Autowired
	SettingService settingService;

	@RequestMapping("")
	public ModelAndView index(HttpSession httpSession, ModelAndView modelAndView) {
		List<Map<String, String>> list = new ArrayList<>();
		if (SystemTool.isWindows()) {
			File[] roots = File.listRoots();// 获取磁盘分区列表
			for (File file : roots) {
				Map<String, String> map = new HashMap<String, String>();

				long freeSpace = file.getFreeSpace();
				long totalSpace = file.getTotalSpace();
				long usableSpace = totalSpace - freeSpace;

				map.put("path", file.getPath());
				map.put("freeSpace", freeSpace / 1024 / 1024 / 1024 + "G");// 空闲空间
				map.put("usableSpace", usableSpace / 1024 / 1024 / 1024 + "G");// 已用空间
				map.put("totalSpace", totalSpace / 1024 / 1024 / 1024 + "G");// 总空间
				map.put("percent", NumberUtil.decimalFormat("#.##%", (double) usableSpace / (double) totalSpace));// 总空间
				list.add(map);
			}
		} else {
			try {
				List<String> lines = RuntimeUtil.execForLines("df -h");

				for (int i = 1; i < lines.size(); i++) {
					String line = lines.get(i);
					if (line.startsWith(File.separator)) {
						while (line.contains("  ")) {
							line = line.replace("  ", " ");
						}
						Map<String, String> map = new HashMap<String, String>();

						String[] names = line.split(" ");
						map.put("path", names[0]);
						map.put("freeSpace", names[3]);// 空闲空间
						map.put("usableSpace", names[2]);// 已用空间
						map.put("totalSpace", names[1]);// 总空间
						map.put("percent", names[4]);// 总空间

						list.add(map);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		modelAndView.addObject("list", list);

		String nginxPath = settingService.get("nginxPath");
		String nginxExe = settingService.get("nginxExe");
		String nginxDir = settingService.get("nginxDir");

		Boolean isInit = StrUtil.isNotEmpty(nginxExe) || StrUtil.isNotEmpty(nginxPath);
		if (StrUtil.isEmpty(nginxExe)) {
			nginxExe = "nginx";
		}

		modelAndView.addObject("nginxDir", nginxDir);
		modelAndView.addObject("nginxExe", nginxExe);
		modelAndView.addObject("nginxPath", nginxPath);

		modelAndView.addObject("isInit", isInit.toString());
		modelAndView.setViewName("/adminPage/monitor/index");
		return modelAndView;
	}


	@RequestMapping("check")
	@ResponseBody
	public DataResponse check() {

		MonitorInfo monitorInfo = monitorService.getMonitorInfo();
		return DataResponse.success(monitorInfo);
	}

	@RequestMapping("addNginxGiudeOver")
	@ResponseBody
	public DataResponse addNginxGiudeOver(String nginxDir, String nginxExe, String nginxPath) {

		settingService.set("nginxDir", nginxDir);
		settingService.set("nginxExe", nginxExe);
		settingService.set("nginxPath", nginxPath);

		return DataResponse.success();
	}
}