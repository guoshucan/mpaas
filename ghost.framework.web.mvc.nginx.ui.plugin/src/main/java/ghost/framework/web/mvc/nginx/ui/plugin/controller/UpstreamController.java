package ghost.framework.web.mvc.nginx.ui.plugin.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.data.commons.domain.Page;
import ghost.framework.web.angular1x.context.controller.ControllerBase;
import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.http.responseContent.DataResponse;
import ghost.framework.web.mvc.context.ModelAndView;
import ghost.framework.web.mvc.context.bind.annotation.Controller;
import ghost.framework.web.mvc.context.bind.annotation.ResponseBody;
import ghost.framework.web.mvc.nginx.ui.plugin.entity.Upstream;
import ghost.framework.web.mvc.nginx.ui.plugin.entity.UpstreamServer;
import ghost.framework.web.mvc.nginx.ui.plugin.ext.UpstreamExt;
import ghost.framework.web.mvc.nginx.ui.plugin.service.ParamService;
import ghost.framework.web.mvc.nginx.ui.plugin.service.SettingService;
import ghost.framework.web.mvc.nginx.ui.plugin.service.UpstreamService;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("/api")
//@RequestMapping("/adminPage/upstream")
public class UpstreamController extends ControllerBase {
	@Autowired
	UpstreamService upstreamService;
	@Autowired
	ParamService paramService;
	@Autowired
	SettingService settingService;

	@RequestMapping("")
	public ModelAndView index(HttpSession httpSession, ModelAndView modelAndView, Page page, String keywords) {
		// 检测node
//		String upstreamMonitor = settingService.get("upstreamMonitor");
//		if ("true".equals(upstreamMonitor)) {
//			testNode();
//		}

		page = upstreamService.search(page, keywords);

		List<UpstreamExt> list = new ArrayList<UpstreamExt>();
		for (Upstream upstream : page.getRecords(Upstream.class)) {
			UpstreamExt upstreamExt = new UpstreamExt();
			upstreamExt.setUpstream(upstream);

			List<String> str = new ArrayList<String>();
			List<UpstreamServer> servers = upstreamService.getUpstreamServers(upstream.getId());
			for (UpstreamServer upstreamServer : servers) {
				str.add(buildStr(upstreamServer, upstream.getProxyType()));
			}

			upstreamExt.setServerStr(StrUtil.join("", str));
			list.add(upstreamExt);
		}
		page.setRecords(list);

		modelAndView.addObject("page", page);
		modelAndView.addObject("keywords", keywords);
		modelAndView.setViewName("/upstream/index");
		return modelAndView;
	}

	public String buildStr(UpstreamServer upstreamServer, Integer proxyType) {
		String status = "m.get('upstreamStr.noStatus')";
		if (!"none".equals(upstreamServer.getStatus())) {
			status = upstreamServer.getStatus();
		}

		String monitorStatus = "";

		String upstreamMonitor = settingService.get("upstreamMonitor");
		if ("true".equals(upstreamMonitor)) {
			monitorStatus += "<td>";
			if (upstreamServer.getMonitorStatus() == 1) {
				monitorStatus += "<span class='green'>" + "m.get('upstreamStr.green')" + "</span>";
			} else {
				monitorStatus += "<span class='red'>" + "m.get('upstreamStr.red')" + "</span>";
			}
			monitorStatus += "</td>";
		}
		System.err.println(upstreamServer.getServer() + ":" + upstreamServer.getMonitorStatus());

		return "<tr><td>" + upstreamServer.getServer() + ":" + upstreamServer.getPort() + "</td>"//
				+ "<td>weight=" + upstreamServer.getWeight() + "</td>"//
				+ "<td>fail_timeout=" + upstreamServer.getFailTimeout() + "s</td>"//
				+ "<td>max_fails=" + upstreamServer.getMaxFails() + "</td>"//
				+ "<td>" + status + "</td>" //
				+ monitorStatus + "</tr>";

	}

	@RequestMapping("addOver")
	@ResponseBody
	public DataResponse addOver(String upstreamJson, String upstreamParamJson, String upstreamServerJson) {
		Upstream upstream = JSONUtil.toBean(upstreamJson, Upstream.class);
		List<UpstreamServer> upstreamServers = JSONUtil.toList(JSONUtil.parseArray(upstreamServerJson), UpstreamServer.class);

		if (StrUtil.isEmpty(upstream.getId())) {
			Long count = upstreamService.getCountByName(upstream.getName());
			if (count > 0) {
				return DataResponse.error(1, "m.get('upstreamStr.sameName')");
			}
		} else {
			Long count = upstreamService.getCountByNameWithOutId(upstream.getName(), upstream.getId());
			if (count > 0) {
				return DataResponse.error(2, "m.get('upstreamStr.sameName')");
			}
		}

		upstreamService.addOver(upstream, upstreamServers, upstreamParamJson);

		return DataResponse.success();
	}

	@RequestMapping("detail")
	@ResponseBody
	public DataResponse detail(String id) throws SQLException {

		UpstreamExt upstreamExt = new UpstreamExt();
//		upstreamExt.setUpstream(sqlHelper.findById(id, Upstream.class));
		upstreamExt.setUpstream(sessionFactory.findById( Upstream.class, id));
		upstreamExt.setUpstreamServerList(upstreamService.getUpstreamServers(id));

		upstreamExt.setParamJson(paramService.getJsonByTypeId(upstreamExt.getUpstream().getId(), "upstream"));

		return DataResponse.success(upstreamExt);
	}

	@RequestMapping("del")
	@ResponseBody
	public DataResponse del(String id) {

		upstreamService.del(id);

		return DataResponse.success();
	}

	@RequestMapping("setMonitor")
	@ResponseBody
	public DataResponse setMonitor(String id, Integer monitor) throws SQLException {
		Upstream upstream = new Upstream();
		upstream.setId(id);
		upstream.setMonitor(monitor);
//		sqlHelper.updateById(upstream);
		this.sessionFactory.update(upstream);
		return DataResponse.success();
	}

	@RequestMapping("upstreamStatus")
	@ResponseBody
	public DataResponse upstreamStatus(HttpSession httpSession) {
		Map<String, String> map = new HashMap<>();
		map.put("mail", settingService.get("mail"));

		String upstreamMonitor = settingService.get("upstreamMonitor");
		map.put("upstreamMonitor", upstreamMonitor != null ? upstreamMonitor : "false");

		return DataResponse.success(map);
	}

	@RequestMapping("upstreamOver")
	@ResponseBody
	public DataResponse upstreamOver(String mail, String upstreamMonitor) {
		settingService.set("mail", mail);
		settingService.set("upstreamMonitor", upstreamMonitor);

		return DataResponse.success();
	}

	/**
	 * 检测node
	 */
//	private void testNode() {
//		List<UpstreamServer> upstreamServers = upstreamService.getAllServer();
//		for (UpstreamServer upstreamServer : upstreamServers) {
//			if (!TelnetUtils.isRunning(upstreamServer.getServer(), upstreamServer.getPort())) {
//				upstreamServer.setMonitorStatus(0);
//			} else {
//				upstreamServer.setMonitorStatus(1);
//			}
//
//			sqlHelper.updateById(upstreamServer);
//		}
//	}

}
