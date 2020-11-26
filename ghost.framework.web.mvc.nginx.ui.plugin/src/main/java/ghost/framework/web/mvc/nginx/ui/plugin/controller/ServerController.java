package ghost.framework.web.mvc.nginx.ui.plugin.controller;

import cn.hutool.core.io.FileUtil;
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
import ghost.framework.web.mvc.nginx.ui.plugin.entity.*;
import ghost.framework.web.mvc.nginx.ui.plugin.ext.ServerExt;
import ghost.framework.web.mvc.nginx.ui.plugin.service.ParamService;
import ghost.framework.web.mvc.nginx.ui.plugin.service.ServerService;
import ghost.framework.web.mvc.nginx.ui.plugin.service.SettingService;
import ghost.framework.web.mvc.nginx.ui.plugin.service.UpstreamService;
import ghost.framework.web.mvc.nginx.ui.plugin.utils.TelnetUtils;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller("/api")
//@RequestMapping("/adminPage/server")
public class ServerController extends ControllerBase {
	@Autowired
	ServerService serverService;
	@Autowired
	UpstreamService upstreamService;
	@Autowired
	ParamService paramService;
	@Autowired
	SettingService settingService;

	@RequestMapping("")
	public ModelAndView index(HttpSession httpSession, ModelAndView modelAndView, Page page, String sort, String direction, String keywords) throws SQLException {
		page = serverService.search(page, sort, direction, keywords);

		List<ServerExt> exts = new ArrayList<ServerExt>();
		for (Server server : page.getRecords(Server.class)) {
			ServerExt serverExt = new ServerExt();
			if (server.getEnable() == null) {
				server.setEnable(false);
			}

			serverExt.setServer(server);
			if (server.getProxyType() == 0) {
				serverExt.setLocationStr(buildLocationStr(server.getId()));
			} else {
//				Upstream upstream = sqlHelper.findById(server.getProxyUpstreamId(), Upstream.class);
				Upstream upstream = sessionFactory.findById(Upstream.class, server.getProxyUpstreamId());
				serverExt.setLocationStr("m.get('serverStr.server')" + ": " + (upstream != null ? upstream.getName() : ""));
			}

			exts.add(serverExt);
		}
		page.setRecords(exts);

		modelAndView.addObject("page", page);

		List<Upstream> upstreamList = upstreamService.getListByProxyType(0);
		modelAndView.addObject("upstreamList", upstreamList);
		modelAndView.addObject("upstreamSize", upstreamList.size());

		List<Upstream> upstreamTcpList = upstreamService.getListByProxyType(1);
		modelAndView.addObject("upstreamTcpList", upstreamTcpList);
		modelAndView.addObject("upstreamTcpSize", upstreamTcpList.size());

		modelAndView.addObject("certList", sessionFactory.findAll(Cert.class));
		modelAndView.addObject("wwwList", sessionFactory.findAll(Www.class));
		modelAndView.addObject("sort", sort);
		modelAndView.addObject("direction", direction);

		modelAndView.addObject("keywords", keywords);
		modelAndView.setViewName("/adminPage/server/index");
		return modelAndView;
	}

	private String buildLocationStr(String id) throws SQLException {
		List<String> str = new ArrayList<String>();
		List<Location> locations = serverService.getLocationByServerId(id);
		for (Location location : locations) {
			if (location.getType() == 0) {
				str.add("<span class='path'>" + location.getPath() + "</span><span class='value'>" + location.getValue() + "</span>");
			} else if (location.getType() == 1) {
				str.add("<span class='path'>" + location.getPath() + "</span><span class='value'>" + location.getRootPath() + "</span>");
			} else if (location.getType() == 2) {
				Upstream upstream = sessionFactory.findById(Upstream.class, location.getUpstreamId());
				if (upstream != null) {
					str.add("<span class='path'>" + location.getPath() + "</span><span class='value'>http://" + upstream.getName()
							+ (location.getUpstreamPath() != null ? location.getUpstreamPath() : "") + "</span>");
				}
			} else if (location.getType() == 3) {
				str.add("<span class='path'>" + location.getPath() + "</span>");
			}

		}
		return StrUtil.join("<br>", str);
	}

	@RequestMapping("addOver")
	@ResponseBody
	public DataResponse addOver(String serverJson, String serverParamJson, String locationJson) {
		Server server = JSONUtil.toBean(serverJson, Server.class);
		List<Location> locations = JSONUtil.toList(JSONUtil.parseArray(locationJson), Location.class);

		if (server.getProxyType() == 0) {
			try {
				serverService.addOver(server, serverParamJson, locations);
			} catch (Exception e) {
				return DataResponse.error(1, e.getMessage());
			}
		} else {
			serverService.addOverTcp(server, serverParamJson);
		}

		return DataResponse.success();
	}

	@RequestMapping("setEnable")
	@ResponseBody
	public DataResponse setEnable(Server server) throws SQLException {
//		sqlHelper.updateById(server);
		sessionFactory.update(server);
		return DataResponse.success();
	}

	@RequestMapping("detail")
	@ResponseBody
	public DataResponse detail(String id) throws SQLException {
		Server server = sessionFactory.findById(Server.class, id);

		ServerExt serverExt = new ServerExt();
		serverExt.setServer(server);
		List<Location> list = serverService.getLocationByServerId(id);
		for (Location location : list) {
			String json = paramService.getJsonByTypeId(location.getId(), "location");
			location.setLocationParamJson(json != null ? json : null);
		}
		serverExt.setLocationList(list);
		String json = paramService.getJsonByTypeId(server.getId(), "server");
		serverExt.setParamJson(json != null ? json : null);

		return DataResponse.success(serverExt);
	}

	@RequestMapping("del")
	@ResponseBody
	public DataResponse del(String id) {
		serverService.deleteById(id);

		return DataResponse.success();
	}

	@RequestMapping("clone")
	@ResponseBody
	public DataResponse clone(String id) {
		serverService.clone(id);

		return DataResponse.success();
	}

	@RequestMapping("importServer")
	@ResponseBody
	public DataResponse importServer(String nginxPath) {

		if (StrUtil.isEmpty(nginxPath) || !FileUtil.exist(nginxPath)) {
			return DataResponse.error(1, "m.get('serverStr.fileNotExist')");
		}

		try {
			serverService.importServer(nginxPath);
			return DataResponse.success("m.get('serverStr.importSuccess')");
		} catch (Exception e) {
			e.printStackTrace();

			return DataResponse.error(2, "m.get('serverStr.importFail')" + "：" + e.getMessage());
		}
	}

	@RequestMapping("testPort")
	@ResponseBody
	public DataResponse testPort() {
		List<Server> servers = sessionFactory.findAll(Server.class);

		List<String> ips = new ArrayList<>();
		for (Server server : servers) {
			String ip = "";
			String port = "";
			if (server.getListen().contains(":")) {
				ip = server.getListen().split(":")[0];
				port = server.getListen().split(":")[1];
			} else {
				ip = "127.0.0.1";
				port = server.getListen();
			}

			if (TelnetUtils.isRunning(ip, Integer.parseInt(port)) && !ips.contains(server.getListen())) {
				ips.add(server.getListen());
			}
		}

		if (ips.size() == 0) {
			return DataResponse.success();
		} else {
			return DataResponse.error(1, "m.get('serverStr.portUserdList')" + ": " + StrUtil.join(" , ", ips));
		}
	}
}
