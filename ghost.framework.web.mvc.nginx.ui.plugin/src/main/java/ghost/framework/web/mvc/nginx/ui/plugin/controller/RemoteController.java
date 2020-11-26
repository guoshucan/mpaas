package ghost.framework.web.mvc.nginx.ui.plugin.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import ghost.framework.beans.annotation.injection.Value;
import ghost.framework.web.angular1x.context.controller.ControllerBase;
import ghost.framework.web.angular1x.ssh.plugin.entity.SshGroupEntity;
import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.http.responseContent.DataResponse;
import ghost.framework.web.mvc.context.ModelAndView;
import ghost.framework.web.mvc.context.bind.annotation.Controller;
import ghost.framework.web.mvc.context.bind.annotation.ResponseBody;
import ghost.framework.web.mvc.nginx.ui.plugin.entity.Remote;
import ghost.framework.web.mvc.nginx.ui.plugin.ext.AsycPack;
import ghost.framework.web.mvc.nginx.ui.plugin.ext.Tree;
import ghost.framework.web.mvc.nginx.ui.plugin.service.ConfService;
import ghost.framework.web.mvc.nginx.ui.plugin.service.GroupService;
import ghost.framework.web.mvc.nginx.ui.plugin.service.RemoteService;
import ghost.framework.web.mvc.nginx.ui.plugin.service.SettingService;
import ghost.framework.web.mvc.nginx.ui.plugin.utils.NginxUtils;
import ghost.framework.web.mvc.nginx.ui.plugin.utils.SystemTool;

import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Controller("/api")
//@Controller("/adminPage/remote")
public class RemoteController extends ControllerBase {
	final RemoteService remoteService;
	final SettingService settingService;
	final ConfService confService;
	final GroupService groupService;
	final ConfController confController;
	final MainController mainController;

	@Value("${project.version}")
	String projectVersion;
	@Value("${server.port}")
	Integer port;

	public RemoteController(RemoteService remoteService, SettingService settingService, ConfService confService, GroupService groupService, ConfController confController,
							MainController mainController) {
		this.remoteService = remoteService;
		this.settingService = settingService;
		this.confService = confService;
		this.groupService = groupService;
		this.confController = confController;
		this.mainController = mainController;
	}

	@RequestMapping("version")
	@ResponseBody
	public Map<String, Object> version() {
		Map<String, Object> map = new HashMap<>();
		map.put("version", projectVersion);

		if (NginxUtils.isRun()) {
			map.put("nginx", 1);
		} else {
			map.put("nginx", 0);
		}

		return map;
	}

	@RequestMapping("")
	public ModelAndView index(ModelAndView modelAndView) {
		modelAndView.setViewName("/adminPage/remote/index");
		modelAndView.addObject("projectVersion", projectVersion);
		return modelAndView;
	}

	@RequestMapping("allTable")
	@ResponseBody
	public List<Remote> allTable() {
		List<Remote> remoteList = sessionFactory.findAll(Remote.class);

		for (Remote remote : remoteList) {
			remote.setStatus(0);
			remote.setType(0);
			if (remote.getParentId() == null) {
				remote.setParentId("");
			}

			try {
				String json = HttpUtil.get(remote.getProtocol() + "://" + remote.getIp() + ":" + remote.getPort() + "/remote/version?creditKey=" + remote.getCreditKey(), 1000);
				if (StrUtil.isNotEmpty(json)) {
					Map<String, Object> map = JSONUtil.toBean(json, new TypeReference<Map<String, Object>>() {
					}.getType(), false);

					remote.setStatus(1);
					remote.setVersion((String) map.get("version"));
					remote.setNginx((Integer) map.get("nginx"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		Remote remoteLocal = new Remote();
		remoteLocal.setId("local");
		remoteLocal.setIp("");
		remoteLocal.setProtocol("");
		remoteLocal.setParentId("");
		remoteLocal.setDescr("m.get('remoteStr.local')");
		Map<String, Object> map = version();
		remoteLocal.setVersion((String) map.get("version"));
		remoteLocal.setNginx((Integer) map.get("nginx"));
		remoteLocal.setPort(port);
		remoteLocal.setStatus(1);
		remoteLocal.setType(0);
		remoteLocal.setMonitor(settingService.get("monitorLocal") != null ? Integer.parseInt(settingService.get("monitorLocal")) : 0);
		remoteLocal.setSystem(SystemTool.getSystem());
		remoteList.add(0, remoteLocal);

		List<SshGroupEntity> groupList = sessionFactory.findAll(SshGroupEntity.class);
		for (SshGroupEntity group : groupList) {
			Remote remoteGroup = new Remote();
			remoteGroup.setDescr(group.getName());
			remoteGroup.setId(group.getId());
//			remoteGroup.setParentId(group.getParentId() != null ? group.getParentId() : "");
			remoteGroup.setType(1);

			remoteGroup.setIp("");
			remoteGroup.setProtocol("");
			remoteGroup.setVersion("");
			remoteGroup.setSystem("");

			remoteList.add(remoteGroup);
		}

		return remoteList;
	}

	@RequestMapping("addGroupOver")
	@ResponseBody
	public DataResponse addGroupOver(SshGroupEntity group) throws SQLException {

//		sqlHelper.insertOrUpdate(group);
		sessionFactory.insert(group);
		return DataResponse.success();
	}

	@RequestMapping("groupDetail")
	@ResponseBody
	public DataResponse groupDetail(String id) throws SQLException {
		return DataResponse.success(sessionFactory.findById(SshGroupEntity.class, id));
	}

	@RequestMapping("delGroup")
	@ResponseBody
	public DataResponse delGroup(String id) {

		groupService.delete(id);
		return DataResponse.success();
	}

	@RequestMapping("getGroupTree")
	@ResponseBody
	public DataResponse getGroupTree() {

		List<SshGroupEntity> groups = groupService.getListByParent(null);
		List<Tree> treeList = new ArrayList<>();
		fillTree(groups, treeList);

		Tree tree = new Tree();
		tree.setName("m.get('remoteStr.noGroup')");
		tree.setValue("");

		treeList.add(0, tree);

		return DataResponse.success(treeList);
	}

	private void fillTree(List<SshGroupEntity> groups, List<Tree> treeList) {
		for (SshGroupEntity group : groups) {
			Tree tree = new Tree();
			tree.setName(group.getName());
			tree.setValue(group.getId());

			List<Tree> treeSubList = new ArrayList<>();
			fillTree(groupService.getListByParent(group.getId()), treeSubList);
			tree.setChildren(treeSubList);

			treeList.add(tree);
		}

	}

	@RequestMapping("getCmdRemote")
	@ResponseBody
	public DataResponse getCmdRemote() {

		List<SshGroupEntity> groups = groupService.getListByParent(null);
		List<Remote> remotes = remoteService.getListByParent(null);

		List<Tree> treeList = new ArrayList<>();
		fillTreeRemote(groups, remotes, treeList);

		Tree tree = new Tree();
		tree.setName("m.get('remoteStr.local')");
		tree.setValue("m.get('remoteStr.local')");

		treeList.add(0, tree);

		return DataResponse.success(treeList);
	}

	private void fillTreeRemote(List<SshGroupEntity> groups, List<Remote> remotes, List<Tree> treeList) {
		for (SshGroupEntity group : groups) {
			Tree tree = new Tree();
			tree.setName(group.getName());
			tree.setValue(group.getId());

			List<Tree> treeSubList = new ArrayList<>();

			fillTreeRemote(groupService.getListByParent(group.getId()), remoteService.getListByParent(group.getId()), treeSubList);

			tree.setChildren(treeSubList);

			treeList.add(tree);
		}

		for (Remote remote : remotes) {
			Tree tree = new Tree();
			tree.setName(remote.getIp() + "【" + remote.getDescr() + "】");
			tree.setValue(remote.getId());

			treeList.add(tree);
		}

	}

	@RequestMapping("cmdOver")
	@ResponseBody
	public DataResponse cmdOver(String[] remoteId, String cmd) throws SQLException {
		if (remoteId == null || remoteId.length == 0) {
			return DataResponse.success("未选择服务器");
		}

		StringBuilder rs = new StringBuilder();
		for (String id : remoteId) {
			DataResponse jsonResult = null;
			if (id.equals("local")) {
				if (cmd.contentEquals("check")) {
					jsonResult = confController.check(null, null, null);
				}
				if (cmd.contentEquals("reload")) {
					jsonResult = confController.reload(null, null, null);
				}
				if (cmd.contentEquals("start")) {
					jsonResult = confController.start(null, null, null);
				}
				if (cmd.contentEquals("stop")) {
					jsonResult = confController.stop(null, null);
				}
				if (cmd.contentEquals("update")) {
					jsonResult = DataResponse.error(1, "m.get('remoteStr.notAllow')");
				}
				rs.append("<span class='blue'>" + "m.get('remoteStr.local')" + "> </span>");
			} else {
				Remote remote = sessionFactory.findById(Remote.class, id);
				rs.append("<span class='blue'>").append(remote.getIp()).append("> </span>");

				try {
					String json = HttpUtil.get(remote.getProtocol() + "://" + remote.getIp() + ":" + remote.getPort() + "/conf/" + cmd + "?creditKey=" + remote.getCreditKey());
					jsonResult = JSONUtil.toBean(json, DataResponse.class);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (jsonResult != null) {
				if (jsonResult.isSuccess()) {
					rs.append(jsonResult.getData().toString());
				} else {
					rs.append(jsonResult.getMessage());
				}
			}
			rs.append("<br>");
		}

		return DataResponse.success(rs.toString());
	}

	@RequestMapping("asyc")
	@ResponseBody
	public DataResponse asyc(String fromId, String[] remoteId) throws SQLException {
		if (StrUtil.isEmpty(fromId) || remoteId == null || remoteId.length == 0) {
			return DataResponse.success("m.get('remoteStr.noChoice')");
		}

		Remote remoteFrom = sessionFactory.findById(Remote.class, fromId);
		String json;
		if (remoteFrom == null) {
			// 本地
			json = getAsycPack();
		} else {
			// 远程
			json = HttpUtil.get(remoteFrom.getProtocol() + "://" + remoteFrom.getIp() + ":" + remoteFrom.getPort() + "/remote/getAsycPack?creditKey=" + remoteFrom.getCreditKey(), 1000);
		}

		for (String remoteToId : remoteId) {
			if (remoteToId.equals("local")) {
				setAsycPack(json);
			} else {
				Remote remoteTo = sessionFactory.findById(Remote.class, remoteToId);
				try {
					String version = HttpUtil.get(remoteTo.getProtocol() + "://" + remoteTo.getIp() + ":" + remoteTo.getPort() + "/remote/version?creditKey=" + remoteTo.getCreditKey(),
							1000);
					if (StrUtil.isNotEmpty(version)) {
						// 在线
						Map<String, Object> map = new HashMap<>();
						map.put("json", json);
						HttpUtil.post(remoteTo.getProtocol() + "://" + remoteTo.getIp() + ":" + remoteTo.getPort() + "/remote/setAsycPack?creditKey=" + remoteTo.getCreditKey(), map);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
		return DataResponse.success();
	}

	@RequestMapping("getAsycPack")
	@ResponseBody
	public String getAsycPack() {
		AsycPack asycPack = confService.getAsycPack();

		return JSONUtil.toJsonPrettyStr(asycPack);
	}

	@RequestMapping("setAsycPack")
	@ResponseBody
	public DataResponse setAsycPack(String json) {
		AsycPack asycPack = JSONUtil.toBean(json, AsycPack.class);

		confService.setAsycPack(asycPack);

		return DataResponse.success();
	}

	@RequestMapping("addOver")
	@ResponseBody
	public DataResponse addOver(Remote remote) throws SQLException {
		remote.setIp(remote.getIp().trim());

		if (remoteService.hasSame(remote)) {
			return DataResponse.error(1, "m.get('remoteStr.sameIp')");
		}

		remoteService.getCreditKey(remote);

		if (StrUtil.isNotEmpty(remote.getCreditKey())) {
			sessionFactory.insert(remote);
			return DataResponse.success();
		} else {
			return DataResponse.error(2, "m.get('remoteStr.noAuth')");
		}

	}

	@RequestMapping("detail")
	@ResponseBody
	public DataResponse detail(String id) throws SQLException {
		return DataResponse.success(sessionFactory.findById(Remote.class, id));
	}

	@RequestMapping("del")
	@ResponseBody
	public DataResponse del(String id) throws SQLException {
		sessionFactory.deleteById(Remote.class, id);
		return DataResponse.success();
	}

	@RequestMapping("content")
	@ResponseBody
	public DataResponse content(String id) throws SQLException {

//		Remote remote = sqlHelper.findById(id, Remote.class);
		Remote remote = sessionFactory.findById(Remote.class, id);
		String rs = HttpUtil.get(remote.getProtocol() + "://" + remote.getIp() + ":" + remote.getPort() + "/remote/readContent?creditKey=" + remote.getCreditKey());

		return DataResponse.success(rs);
	}

	@RequestMapping("readContent")
	@ResponseBody
	public String readContent() {

		String nginxPath = settingService.get("nginxPath");

		if (FileUtil.exist(nginxPath)) {
			return FileUtil.readString(nginxPath, StandardCharsets.UTF_8);
		} else {
			return "m.get('remoteStr.noFile')";
		}

	}

	@RequestMapping("change")
	@ResponseBody
	public DataResponse change(String id, HttpSession httpSession) throws SQLException {
		Remote remote = sessionFactory.findById(Remote.class, id);

		if (remote == null) {
			httpSession.setAttribute("localType", "local");
			httpSession.removeAttribute("remote");
		} else {
			httpSession.setAttribute("localType", "remote");
			httpSession.setAttribute("remote", remote);
		}

		return DataResponse.success();
	}

	@RequestMapping("nginxStatus")
	@ResponseBody
	public DataResponse nginxStatus(HttpSession httpSession) {
		Map<String, String> map = new HashMap<>();
		map.put("mail", settingService.get("mail"));

		String nginxMonitor = settingService.get("nginxMonitor");
		map.put("nginxMonitor", nginxMonitor != null ? nginxMonitor : "false");

		return DataResponse.success(map);
	}

	@RequestMapping("nginxOver")
	@ResponseBody
	public DataResponse nginxOver(String mail, String nginxMonitor) {
		settingService.set("mail", mail);
		settingService.set("nginxMonitor", nginxMonitor);

		return DataResponse.success();
	}

	@RequestMapping("setMonitor")
	@ResponseBody
	public DataResponse setMonitor(String id, Integer monitor) throws SQLException {
		if (!"local".equals(id)) {
			Remote remote = new Remote();
			remote.setId(id);
			remote.setMonitor(monitor);
//			sqlHelper.updateById(remote);
			sessionFactory.update(remote);
		} else {
			settingService.set("monitorLocal", monitor.toString());
		}

		return DataResponse.success();
	}
}