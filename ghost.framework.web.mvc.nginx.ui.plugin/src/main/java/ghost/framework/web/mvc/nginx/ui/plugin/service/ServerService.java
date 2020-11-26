package ghost.framework.web.mvc.nginx.ui.plugin.service;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.github.odiszapc.nginxparser.NgxBlock;
import com.github.odiszapc.nginxparser.NgxConfig;
import com.github.odiszapc.nginxparser.NgxEntry;
import com.github.odiszapc.nginxparser.NgxParam;
import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Service;
import ghost.framework.data.hibernate.ISessionFactory;
import ghost.framework.web.mvc.nginx.ui.plugin.entity.Location;
import ghost.framework.web.mvc.nginx.ui.plugin.entity.Param;
import ghost.framework.web.mvc.nginx.ui.plugin.entity.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class ServerService {
	private final static Logger log = LoggerFactory.getLogger(ServerService.class);
	/**
	 * 注入会话工厂
	 */
	@Autowired
	@Application
	private ISessionFactory sessionFactory;

//	@Value("${project.home}")
//	private String tmpPath;

	public Page search(Page page, String sortColum, String direction, String keywords) {
		ConditionAndWrapper conditionAndWrapper = new ConditionAndWrapper();
		if (StrUtil.isNotEmpty(keywords)) {
			conditionAndWrapper.and(new ConditionOrWrapper().like("serverName", keywords.trim()).like("listen", keywords.trim()));
		}

		Sort sort = null;
		if (StrUtil.isNotEmpty(sortColum)) {
			sort = new Sort(sortColum, "asc".equalsIgnoreCase(direction) ? Direction.ASC : Direction.DESC);
		}

		page = sqlHelper.findPage(conditionAndWrapper, sort, page, Server.class);

		return page;
	}

//	@Transactional
	public synchronized void deleteById(String id) throws SQLException {
		sessionFactory.deleteById(Server.class, id);
		sqlHelper.deleteByQuery(new ConditionAndWrapper().eq("serverId", id), Location.class);
	}

	public List<Location> getLocationByServerId(String serverId) {
		return sqlHelper.findListByQuery(new ConditionAndWrapper().eq("serverId", serverId), Location.class);
	}

//	@Transactional
	public void addOver(Server server, String serverParamJson, List<Location> locations) throws SQLException {

		if (server.getDef() == 1) {
			clearDef();
		}

		sessionFactory.insert(server);

		List<Param> paramList = new ArrayList<Param>();
		if (StrUtil.isNotEmpty(serverParamJson) && JSONUtil.isJson(serverParamJson)) {
			paramList = JSONUtil.toList(JSONUtil.parseArray(serverParamJson), Param.class);
		}
		List<String> locationIds = sqlHelper.findIdsByQuery(new ConditionAndWrapper().eq("serverId", server.getId()), Location.class);
		sqlHelper.deleteByQuery(new ConditionOrWrapper().eq("serverId", server.getId()).in("locationId", locationIds), Param.class);

		// 反向插入,保证列表与输入框对应
		Collections.reverse(paramList);
		for (Param param : paramList) {
			param.setServerId(server.getId());
			sessionFactory.insert(param);
		}

		sqlHelper.deleteByQuery(new ConditionAndWrapper().eq("serverId", server.getId()), Location.class);

		if (locations != null) {
			// 反向插入,保证列表与输入框对应
			Collections.reverse(locations);

			for (Location location : locations) {
				location.setServerId(server.getId());

				sessionFactory.insert(location);

				paramList = new ArrayList<Param>();
				if (StrUtil.isNotEmpty(location.getLocationParamJson()) && JSONUtil.isJson(location.getLocationParamJson())) {
					paramList = JSONUtil.toList(JSONUtil.parseArray(location.getLocationParamJson()), Param.class);
				}

				// 反向插入,保证列表与输入框对应
				Collections.reverse(paramList);
				for (Param param : paramList) {
					param.setLocationId(location.getId());
					sessionFactory.insert(param);
				}
			}
		}
	}

	private void clearDef() throws SQLException{
		List<Server> servers = sqlHelper.findListByQuery(new ConditionAndWrapper().eq("def", 1), Server.class);
		for (Server server : servers) {
			server.setDef(0);
			sessionFactory.update(server);
		}
	}

//	@Transactional
	public synchronized void addOverTcp(Server server, String serverParamJson) throws SQLException{
		sessionFactory.insert(server);

		List<String> locationIds = sqlHelper.findIdsByQuery(new ConditionAndWrapper().eq("serverId", server.getId()), Location.class);
		sqlHelper.deleteByQuery(new ConditionOrWrapper().eq("serverId", server.getId()).in("locationId", locationIds), Param.class);
		List<Param> paramList = new ArrayList<Param>();
		if (StrUtil.isNotEmpty(serverParamJson) && JSONUtil.isJson(serverParamJson)) {
			paramList = JSONUtil.toList(JSONUtil.parseArray(serverParamJson), Param.class);
		}

		for (Param param : paramList) {
			param.setServerId(server.getId());
			sessionFactory.insert(param);
		}

		sqlHelper.deleteByQuery(new ConditionAndWrapper().eq("serverId", server.getId()), Location.class);
	}

	public List<Server> getListByProxyType(Integer proxyType) {
		return sqlHelper.findListByQuery(new ConditionAndWrapper().eq("proxyType", proxyType), Server.class);
	}

//	@Transactional
	public void clone(String id) throws SQLException{
		Server server = sessionFactory.findById(Server.class, id);

		List<Location> locations = sqlHelper.findListByQuery(new ConditionAndWrapper().eq("serverId", server.getId()), Location.class);
		List<Param> params = sqlHelper.findListByQuery(new ConditionAndWrapper().eq("serverId", server.getId()), Param.class);

		server.setId(null);
		sessionFactory.update(server);
		for (Param param : params) {
			param.setId(null);
			param.setServerId(server.getId());
			sessionFactory.insert(param);
		}

		for (Location location : locations) {
			params = sqlHelper.findListByQuery(new ConditionAndWrapper().eq("locationId", location.getId()), Param.class);

			location.setId(null);
			location.setServerId(server.getId());
			sessionFactory.insert(location);

			for (Param param : params) {
				param.setId(null);
				param.setLocationId(location.getId());
				sessionFactory.insert(param);
			}
		}

	}

	public void importServer(String nginxPath) throws Exception {
		String initNginxPath = initNginx(nginxPath);
		NgxConfig conf = null;
		try {
			conf = NgxConfig.read(initNginxPath);
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("文件读取失败");
		}

		List<NgxEntry> servers = conf.findAll(NgxConfig.BLOCK, "server");
		servers.addAll(conf.findAll(NgxConfig.BLOCK, "http", "server"));

		// 翻转一下,便于插入顺序和生成时一样
		Collections.reverse(servers);

		for (NgxEntry ngxEntry : servers) {
			NgxBlock serverNgx = (NgxBlock) ngxEntry;
			NgxParam serverName = serverNgx.findParam("server_name");
			Server server = new Server();
			if (serverName == null) {
				server.setServerName("");
			} else {
				server.setServerName(serverName.getValue());
			}

			server.setProxyType(0);

			// 设置server
			List<NgxEntry> listens = serverNgx.findAll(NgxConfig.PARAM, "listen");
			for (NgxEntry item : listens) {
				NgxParam param = (NgxParam) item;

//				System.err.println(param.getName());
//				System.err.println(param.getValue());
//				System.err.println(param.getTokens());
//				System.err.println(param.getValues());

				if (server.getListen() == null) {
					server.setListen((String) param.getValues().toArray()[0]);
				}

				if (param.getTokens().stream().anyMatch(item2 -> "ssl".equals(item2.getToken()))) {
					server.setSsl(1);
					NgxParam key = serverNgx.findParam("ssl_certificate_key");
					NgxParam perm = serverNgx.findParam("ssl_certificate");
					server.setKey(key == null ? "" : key.getValue());
					server.setPem(perm == null ? "" : perm.getValue());
				}

				if (param.getTokens().stream().anyMatch(item2 -> "http2".equals(item2.getToken()))) {
					server.setHttp2(1);
				}
			}

			long rewriteCount = serverNgx.getEntries().stream().filter(item -> {
				if (item instanceof NgxBlock) {
					NgxBlock itemNgx = (NgxBlock) item;
					if (itemNgx.getEntries().toString().contains("rewrite")) {
						return true;
					}
					return false;
				}
				return false;
			}).count();

			if (rewriteCount > 0) {
				server.setRewrite(1);
			} else {
				server.setRewrite(0);
			}

			// 设置location
			List<Location> locations = new ArrayList<>();
			List<NgxEntry> locationBlocks = serverNgx.findAll(NgxBlock.class, "location");
			for (NgxEntry item : locationBlocks) {
				Location location = new Location();
				// 目前只支持http段的 proxy_pass
				NgxParam proxyPassParam = ((NgxBlock) item).findParam("proxy_pass");

				location.setPath(((NgxBlock) item).getValue());
				// 如果没有proxy_pass type 0,说明可能是静态文件夹映射 type 1
				if (proxyPassParam != null) {
					location.setValue(proxyPassParam.getValue());
					location.setType(0);
				} else {
					NgxParam rootParam = ((NgxBlock) item).findParam("root");
					if (rootParam == null) {
						rootParam = ((NgxBlock) item).findParam("alias");
					}
					if (rootParam == null) {
						continue;
					}

					location.setRootType(rootParam.getName());
					location.setRootPath(rootParam.getValue());

					NgxParam indexParam = ((NgxBlock) item).findParam("index");
					if (indexParam != null) {
						location.setRootPage(indexParam.getValue());
					}

					location.setType(1);
				}
				location.setLocationParamJson(null);
				locations.add(location);
			}

			this.addOver(server, "", locations);
		}

		// 删除临时文件
		FileUtil.del(initNginxPath);
	}

	/**
	 * 重新创建配置文件，删除影响解析的行（比如#号开头，但是此行没有其他内容）
	 *
	 * @param nginxPath
	 * @return java.lang.String
	 * @author by yanglei 2020/7/5 21:17
	 */
	private String initNginx(String nginxPath) {
		// 删除一行内容（java本身没有删除的方法，本方法通过先读取文件的内容（需删除的行数除外），放到list中，在重新写入）
		List<String> lines = FileUtil.readLines(nginxPath, CharsetUtil.CHARSET_UTF_8);
		List<String> rs = new ArrayList<>();
		for (String str : lines) {
			if (str.trim().indexOf("#") == 0) {
				continue;
			}
			rs.add(str);
		}

		String initNginxPath = FileUtil.getTmpDirPath() + UUID.randomUUID().toString();
		FileUtil.writeLines(rs, initNginxPath, CharsetUtil.CHARSET_UTF_8);
		return initNginxPath;
	}
}