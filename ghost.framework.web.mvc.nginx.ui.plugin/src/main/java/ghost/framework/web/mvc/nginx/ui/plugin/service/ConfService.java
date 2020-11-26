package ghost.framework.web.mvc.nginx.ui.plugin.service;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.github.odiszapc.nginxparser.NgxBlock;
import com.github.odiszapc.nginxparser.NgxConfig;
import com.github.odiszapc.nginxparser.NgxDumper;
import com.github.odiszapc.nginxparser.NgxParam;
import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Service;
import ghost.framework.data.hibernate.ISessionFactory;
import ghost.framework.web.mvc.nginx.ui.plugin.config.InitConfig;
import ghost.framework.web.mvc.nginx.ui.plugin.entity.*;
import ghost.framework.web.mvc.nginx.ui.plugin.ext.AsycPack;
import ghost.framework.web.mvc.nginx.ui.plugin.ext.ConfExt;
import ghost.framework.web.mvc.nginx.ui.plugin.ext.ConfFile;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ConfService {
	@Autowired
	private UpstreamService upstreamService;
	@Autowired
	private SettingService settingService;
	@Autowired
	private ServerService serverService;
	@Autowired
	private LocationService locationService;
	@Autowired
	private ParamService paramService;
	/**
	 * 注入会话工厂
	 */
	@Autowired
	@Application
	private ISessionFactory sessionFactory;
	@Autowired
	private TemplateService templateService;

//	public ConfService(TemplateService templateService,UpstreamService upstreamService, SettingService settingService, ServerService serverService, LocationService locationService, ParamService paramService, SqlHelper sqlHelper) {
//		this.upstreamService = upstreamService;
//		this.settingService = settingService;
//		this.serverService = serverService;
//		this.locationService = locationService;
//		this.paramService = paramService;
//		this.sqlHelper = sqlHelper;
//		this.templateService = templateService;
//	}

	public synchronized ConfExt buildConf(Boolean decompose) throws SQLException {
		ConfExt confExt = new ConfExt();
		confExt.setFileList(new ArrayList<>());

		String nginxPath = settingService.get("nginxPath");
		try {

			NgxConfig ngxConfig = new NgxConfig();

			// 获取基本参数
			List<Basic> basicList = sqlHelper.findAll(new Sort("seq", Direction.ASC), Basic.class);
			for (Basic basic : basicList) {
				NgxParam ngxParam = new NgxParam();
				ngxParam.addValue(basic.getName().trim() + " " + basic.getValue().trim());
				ngxConfig.addEntry(ngxParam);
			}

			// 获取http
			List<Http> httpList = sqlHelper.findAll(new Sort("seq", Direction.ASC), Http.class);
			boolean hasHttp = false;
			NgxBlock ngxBlockHttp = new NgxBlock();
			ngxBlockHttp.addValue("http");
			for (Http http : httpList) {
				NgxParam ngxParam = new NgxParam();
				ngxParam.addValue(http.getName().trim() + " " + http.getValue().trim());
				ngxBlockHttp.addEntry(ngxParam);

				hasHttp = true;
			}

			// 添加upstream
			NgxParam ngxParam;
			List<Upstream> upstreams = upstreamService.getListByProxyType(0);

			for (Upstream upstream : upstreams) {
				NgxBlock ngxBlockServer = new NgxBlock();
				ngxBlockServer.addValue("upstream " + upstream.getName().trim());

				if (StrUtil.isNotEmpty(upstream.getTactics())) {
					ngxParam = new NgxParam();
					ngxParam.addValue(upstream.getTactics());
					ngxBlockServer.addEntry(ngxParam);
				}

				List<UpstreamServer> upstreamServers = upstreamService.getUpstreamServers(upstream.getId());
				for (UpstreamServer upstreamServer : upstreamServers) {
					ngxParam = new NgxParam();
					ngxParam.addValue("server " + buildNodeStr(upstreamServer, upstream.getProxyType()));
					ngxBlockServer.addEntry(ngxParam);
				}

				// 自定义参数
				List<Param> paramList = paramService.getListByTypeId(upstream.getId(), "upstream");
				for (Param param : paramList) {
					setSameParam(param, ngxBlockServer);
				}

				hasHttp = true;

				if (decompose) {
					addConfFile(confExt, "upstreams." + upstream.getName() + ".conf", ngxBlockServer);

					ngxParam = new NgxParam();
					ngxParam.addValue("include " + nginxPath.replace("nginx.conf", "conf.d/upstreams." + upstream.getName() + ".conf"));
					ngxBlockHttp.addEntry(ngxParam);

				} else {
					ngxBlockHttp.addEntry(ngxBlockServer);
				}

			}

			// 添加server
			List<Server> servers = serverService.getListByProxyType(0);
			for (Server server : servers) {
				if (server.getEnable() == null || !server.getEnable()) {
					continue;
				}

				NgxBlock ngxBlockServer = new NgxBlock();
				ngxBlockServer.addValue("server");

				// 监听域名
				if (StrUtil.isNotEmpty(server.getServerName())) {
					ngxParam = new NgxParam();
					ngxParam.addValue("server_name " + server.getServerName());
					ngxBlockServer.addEntry(ngxParam);
				}

				// 监听端口
				ngxParam = new NgxParam();
				String value = "listen " + server.getListen();
				if (server.getDef() == 1) {
					value += " default";
				}

				if (server.getSsl() != null && server.getSsl() == 1) {
					value += " ssl";
					if (server.getHttp2() != null && server.getHttp2() == 1) {
						value += " http2";
					}
				}
				ngxParam.addValue(value);
				ngxBlockServer.addEntry(ngxParam);

				// ssl配置
				if (server.getSsl() == 1) {
					if (StrUtil.isNotEmpty(server.getPem()) && StrUtil.isNotEmpty(server.getKey())) {
						ngxParam = new NgxParam();
						ngxParam.addValue("ssl_certificate " + server.getPem());
						ngxBlockServer.addEntry(ngxParam);

						ngxParam = new NgxParam();
						ngxParam.addValue("ssl_certificate_key " + server.getKey());
						ngxBlockServer.addEntry(ngxParam);

						ngxParam = new NgxParam();
//						ngxParam.addValue("ssl_protocols TLSv1 TLSv1.1 TLSv1.2 TLSv1.3");
						ngxParam.addValue("ssl_protocols TLSv1 TLSv1.1 TLSv1.2");
						ngxBlockServer.addEntry(ngxParam);
					}

					// https添加80端口重写
					if (server.getRewrite() == 1) {
						ngxParam = new NgxParam();
						ngxParam.addValue("listen 80");
						ngxBlockServer.addEntry(ngxParam);

						NgxBlock ngxBlock = new NgxBlock();
						ngxBlock.addValue("if ($scheme = http)");
						ngxParam = new NgxParam();
//						ngxParam.addValue("rewrite ^(.*) https://$server_name$1 permanent");
						ngxParam.addValue("return 301 https://$host$request_uri");
						ngxBlock.addEntry(ngxParam);

						ngxBlockServer.addEntry(ngxBlock);

					}
				}

				// 自定义参数
				List<Param> paramList = paramService.getListByTypeId(server.getId(), "server");
				for (Param param : paramList) {
					setSameParam(param, ngxBlockServer);
				}

				List<Location> locationList = serverService.getLocationByServerId(server.getId());

				// location参数配置
				for (Location location : locationList) {
					NgxBlock ngxBlockLocation = new NgxBlock();
					if (location.getType() == 0 || location.getType() == 2) { // location或负载均衡
						// 添加location
						ngxBlockLocation.addValue("location");
						ngxBlockLocation.addValue(location.getPath());

						if (location.getType() == 0) {
							ngxParam = new NgxParam();
							ngxParam.addValue("proxy_pass " + location.getValue());
							ngxBlockLocation.addEntry(ngxParam);
						} else if (location.getType() == 2) {
							Upstream upstream = sessionFactory.findById(Upstream.class, location.getUpstreamId());
							if (upstream != null) {
								ngxParam = new NgxParam();
								ngxParam.addValue("proxy_pass http://" + upstream.getName() + (location.getUpstreamPath() != null ? location.getUpstreamPath() : ""));
								ngxBlockLocation.addEntry(ngxParam);
							}
						}

						if (location.getHeader() == 1) { // 设置header
							ngxParam = new NgxParam();
							ngxParam.addValue("proxy_set_header Host $host:$server_port");
							ngxBlockLocation.addEntry(ngxParam);

							ngxParam = new NgxParam();
							ngxParam.addValue("proxy_set_header X-Real-IP $remote_addr");
							ngxBlockLocation.addEntry(ngxParam);

							ngxParam = new NgxParam();
							ngxParam.addValue("proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for");
							ngxBlockLocation.addEntry(ngxParam);

							ngxParam = new NgxParam();
							ngxParam.addValue("proxy_set_header X-Forwarded-Proto $scheme");
							ngxBlockLocation.addEntry(ngxParam);
						}

						if (server.getSsl() == 1 && server.getRewrite() == 1) { // redirect http转https
							ngxParam = new NgxParam();
							ngxParam.addValue("proxy_redirect http:// https://");
							ngxBlockLocation.addEntry(ngxParam);
						}

					} else if (location.getType() == 1) { // 静态html
						ngxBlockLocation.addValue("location");
						ngxBlockLocation.addValue(location.getPath());

						if (location.getRootType() != null && location.getRootType().equals("alias")) {
							ngxParam = new NgxParam();
							ngxParam.addValue("alias " + location.getRootPath());
							ngxBlockLocation.addEntry(ngxParam);
						} else {
							ngxParam = new NgxParam();
							ngxParam.addValue("root " + location.getRootPath());
							ngxBlockLocation.addEntry(ngxParam);
						}

						if (StrUtil.isNotEmpty(location.getRootPage())) {
							ngxParam = new NgxParam();
							ngxParam.addValue("index " + location.getRootPage());
							ngxBlockLocation.addEntry(ngxParam);
						}

					} else if (location.getType() == 3) { // 空白location

						ngxBlockLocation.addValue("location");
						ngxBlockLocation.addValue(location.getPath());

					}

					// 自定义参数
					paramList = paramService.getListByTypeId(location.getId(), "location");
					for (Param param : paramList) {
						setSameParam(param, ngxBlockLocation);
					}

					ngxBlockServer.addEntry(ngxBlockLocation);

				}
				hasHttp = true;

				// 是否需要分解
				if (decompose) {
					String name = "all";
					if (StrUtil.isNotEmpty(server.getServerName())) {
						name = server.getServerName();
					}

					addConfFile(confExt, name + ".conf", ngxBlockServer);

					ngxParam = new NgxParam();
					ngxParam.addValue("include " + nginxPath.replace("nginx.conf", "conf.d/" + name + ".conf"));
					ngxBlockHttp.addEntry(ngxParam);

				} else {
					ngxBlockHttp.addEntry(ngxBlockServer);
				}

			}
			if (hasHttp) {
				ngxConfig.addEntry(ngxBlockHttp);
			}

			// TCP转发
			// 创建stream
			List<Stream> streamList = sqlHelper.findAll(new Sort("seq", Direction.ASC), Stream.class);
			boolean hasStream = false;
			NgxBlock ngxBlockStream = new NgxBlock();
			ngxBlockStream.addValue("stream");
			for (Stream stream : streamList) {
				ngxParam = new NgxParam();
				ngxParam.addValue(stream.getName() + " " + stream.getValue());
				ngxBlockStream.addEntry(ngxParam);

				hasStream = true;
			}

			// 添加upstream
			upstreams = upstreamService.getListByProxyType(1);
			for (Upstream upstream : upstreams) {
				NgxBlock ngxBlockServer = new NgxBlock();
				ngxBlockServer.addValue("upstream " + upstream.getName());

				if (StrUtil.isNotEmpty(upstream.getTactics())) {
					ngxParam = new NgxParam();
					ngxParam.addValue(upstream.getTactics());
					ngxBlockServer.addEntry(ngxParam);
				}

				List<UpstreamServer> upstreamServers = upstreamService.getUpstreamServers(upstream.getId());
				for (UpstreamServer upstreamServer : upstreamServers) {
					ngxParam = new NgxParam();
					ngxParam.addValue("server " + buildNodeStr(upstreamServer, upstream.getProxyType()));
					ngxBlockServer.addEntry(ngxParam);
				}

				// 自定义参数
				List<Param> paramList = paramService.getListByTypeId(upstream.getId(), "upstream");
				for (Param param : paramList) {
					setSameParam(param, ngxBlockServer);
				}

				if (decompose) {
					addConfFile(confExt, "upstreams." + upstream.getName() + ".conf", ngxBlockServer);

					ngxParam = new NgxParam();
					ngxParam.addValue("include " + nginxPath.replace("nginx.conf", "conf.d/upstreams." + upstream.getName() + ".conf"));
					ngxBlockStream.addEntry(ngxParam);
				} else {
					ngxBlockStream.addEntry(ngxBlockServer);
				}

				hasStream = true;
			}

			// 添加server
			servers = serverService.getListByProxyType(1);
			for (Server server : servers) {
				if (server.getEnable() == null || !server.getEnable()) {
					continue;
				}

				NgxBlock ngxBlockServer = new NgxBlock();
				ngxBlockServer.addValue("server");

				// 监听端口
				ngxParam = new NgxParam();
				ngxParam.addValue("listen " + server.getListen());
				ngxBlockServer.addEntry(ngxParam);

				// 指向负载均衡
				Upstream upstream = sessionFactory.findById(Upstream.class, server.getProxyUpstreamId());
				if (upstream != null) {
					ngxParam = new NgxParam();
					ngxParam.addValue("proxy_pass " + upstream.getName());
					ngxBlockServer.addEntry(ngxParam);
				}

				// 自定义参数
				List<Param> paramList = paramService.getListByTypeId(server.getId(), "server");
				for (Param param : paramList) {
					setSameParam(param, ngxBlockServer);
				}

				if (decompose) {
					addConfFile(confExt, "stream." + server.getListen() + ".conf", ngxBlockServer);

					ngxParam = new NgxParam();
					ngxParam.addValue("include " + nginxPath.replace("nginx.conf", "conf.d/stream." + server.getListen() + ".conf"));
					ngxBlockStream.addEntry(ngxParam);
				} else {
					ngxBlockStream.addEntry(ngxBlockServer);
				}

				hasStream = true;
			}

			if (hasStream) {
				ngxConfig.addEntry(ngxBlockStream);
			}

			String conf = new NgxDumper(ngxConfig).dump().replace("};", "  }");

			confExt.setConf(conf);

			return confExt;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public String buildNodeStr(UpstreamServer upstreamServer, Integer proxyType) {
		String status = "";
		if (!"none".equals(upstreamServer.getStatus())) {
			status = upstreamServer.getStatus();
		}

		return upstreamServer.getServer() + ":" + upstreamServer.getPort() //
				+ " weight=" + upstreamServer.getWeight() //
				+ " fail_timeout=" + upstreamServer.getFailTimeout() + "s"//
				+ " max_fails=" + upstreamServer.getMaxFails() //
				+ " " + status;

	}

	private void setSameParam(Param param, NgxBlock ngxBlock) {
		if (StrUtil.isEmpty(param.getTemplateValue())) {
			NgxParam ngxParam = new NgxParam();
			ngxParam.addValue(param.getName().trim() + " " + param.getValue().trim());
			ngxBlock.addEntry(ngxParam);
		} else {
			List<Param> params = templateService.getParamList(param.getTemplateValue());
			for (Param paramSub : params) {
				NgxParam ngxParam = new NgxParam();
				ngxParam.addValue(paramSub.getName().trim() + " " + paramSub.getValue().trim());
				ngxBlock.addEntry(ngxParam);
			}
		}
	}

	private void addConfFile(ConfExt confExt, String name, NgxBlock ngxBlockServer) {
		boolean hasSameName = false;
		for (ConfFile confFile : confExt.getFileList()) {
			if (confFile.getName().equals(name)) {
				confFile.setConf(confFile.getConf() + "\n" + buildStr(ngxBlockServer));
				hasSameName = true;
			}
		}

		if (!hasSameName) {
			ConfFile confFile = new ConfFile();
			confFile.setName(name);
			confFile.setConf(buildStr(ngxBlockServer));
			confExt.getFileList().add(confFile);
		}
	}

	private String buildStr(NgxBlock ngxBlockServer) {

		NgxConfig ngxConfig = new NgxConfig();
		ngxConfig.addEntry(ngxBlockServer);

		return new NgxDumper(ngxConfig).dump().replace("};", "  }");
	}

	public void replace(String nginxPath, String nginxContent, List<String> subContent, List<String> subName) {
		String date = DateUtil.format(new Date(), "yyyy-MM-dd_HH-mm-ss");
		// 备份主文件
		FileUtil.mkdir(InitConfig.home + "bak");
		FileUtil.copy(nginxPath, InitConfig.home + "bak/nginx.conf." + date + ".bak", true);
		// 备份conf.d文件夹
		String confd = nginxPath.replace("nginx.conf", "conf.d/");
		if (!FileUtil.exist(confd)) {
			FileUtil.mkdir(confd);
		}
		ZipUtil.zip(confd, InitConfig.home + "bak/nginx.conf." + date + ".zip");

		// 删除conf.d下全部文件
		FileUtil.del(confd);
		FileUtil.mkdir(confd);

		// 写入主文件
		FileUtil.writeString(nginxContent, nginxPath, StandardCharsets.UTF_8);
		String decompose = settingService.get("decompose");

		if ("true".equals(decompose)) {
			// 写入conf.d文件
			if (subContent != null) {
				for (int i = 0; i < subContent.size(); i++) {
					String tagert = nginxPath.replace("nginx.conf", "conf.d/" + subName.get(i));
					FileUtil.writeString(subContent.get(i), tagert, StandardCharsets.UTF_8); // 清空
				}
			}
		}

	}

	public AsycPack getAsycPack() {
		AsycPack asycPack = new AsycPack();
		asycPack.setHttpList(sessionFactory.findAll(Http.class));
		List<Server> serverList = sessionFactory.findAll(Server.class);
		for (Server server : serverList) {
			if (StrUtil.isNotEmpty(server.getPem()) && FileUtil.exist(server.getPem())) {
				server.setPemStr(FileUtil.readString(server.getPem(), StandardCharsets.UTF_8));
			}

			if (StrUtil.isNotEmpty(server.getKey()) && FileUtil.exist(server.getKey())) {
				server.setKeyStr(FileUtil.readString(server.getKey(), StandardCharsets.UTF_8));
			}
		}
		asycPack.setServerList(serverList);

		asycPack.setLocationList(sessionFactory.findAll(Location.class));
		asycPack.setUpstreamList(sessionFactory.findAll(Upstream.class));
		asycPack.setUpstreamServerList(sessionFactory.findAll(UpstreamServer.class));
		asycPack.setStreamList(sessionFactory.findAll(Stream.class));

		asycPack.setParamList(sessionFactory.findAll(Param.class));

		String nginxPath = settingService.get("nginxPath");
		String decompose = settingService.get("decompose");

		ConfExt confExt = buildConf(StrUtil.isNotEmpty(decompose) && decompose.equals("true"));

		if (FileUtil.exist(nginxPath)) {
			String orgStr = FileUtil.readString(nginxPath, StandardCharsets.UTF_8);
			confExt.setConf(orgStr);

			for (ConfFile confFile : confExt.getFileList()) {
				confFile.setConf("");

				String filePath = nginxPath.replace("nginx.conf", "conf.d/" + confFile.getName());
				if (FileUtil.exist(filePath)) {
					confFile.setConf(FileUtil.readString(filePath, StandardCharsets.UTF_8));
				}
			}
		}

		asycPack.setDecompose(decompose);
		asycPack.setConfExt(confExt);
		return asycPack;
	}

//	@Transactional
	public synchronized void setAsycPack(AsycPack asycPack) throws SQLException{
		// 不要同步Cert表
		sqlHelper.deleteByQuery(new ConditionAndWrapper(), Http.class);
		sqlHelper.deleteByQuery(new ConditionAndWrapper(), Server.class);
		sqlHelper.deleteByQuery(new ConditionAndWrapper(), Location.class);
		sqlHelper.deleteByQuery(new ConditionAndWrapper(), Upstream.class);
		sqlHelper.deleteByQuery(new ConditionAndWrapper(), UpstreamServer.class);
		sqlHelper.deleteByQuery(new ConditionAndWrapper(), Stream.class);
		sqlHelper.deleteByQuery(new ConditionAndWrapper(), Param.class);

		sessionFactory.insertAll(asycPack.getHttpList());
		sessionFactory.insertAll(asycPack.getServerList());
		sessionFactory.insertAll(asycPack.getLocationList());
		sessionFactory.insertAll(asycPack.getUpstreamList());
		sessionFactory.insertAll(asycPack.getUpstreamServerList());
		sessionFactory.insertAll(asycPack.getStreamList());
		sessionFactory.insertAll(asycPack.getParamList());

		for (Server server : asycPack.getServerList()) {
			if (StrUtil.isNotEmpty(server.getPem()) && StrUtil.isNotEmpty(server.getPemStr())) {
				FileUtil.writeString(server.getPemStr(), server.getPem(), StandardCharsets.UTF_8);
			}
			if (StrUtil.isNotEmpty(server.getKey()) && StrUtil.isNotEmpty(server.getKeyStr())) {
				FileUtil.writeString(server.getKeyStr(), server.getKey(), StandardCharsets.UTF_8);
			}
		}

		settingService.set("decompose", asycPack.getDecompose());

		ConfExt confExt = asycPack.getConfExt();

		String nginxPath = settingService.get("nginxPath");

		if (FileUtil.exist(nginxPath)) {

			List<String> subContent = new ArrayList<>();
			List<String> subName = new ArrayList<>();

			for (ConfFile confFile : confExt.getFileList()) {
				subContent.add(confFile.getConf());
				subName.add(confFile.getName());
			}

			replace(nginxPath, confExt.getConf(), subContent, subName);
		}
	}
}