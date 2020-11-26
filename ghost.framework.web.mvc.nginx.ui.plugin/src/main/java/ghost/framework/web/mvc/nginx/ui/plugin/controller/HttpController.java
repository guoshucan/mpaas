package ghost.framework.web.mvc.nginx.ui.plugin.controller;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.web.angular1x.context.controller.ControllerBase;
import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.http.responseContent.DataResponse;
import ghost.framework.web.mvc.context.ModelAndView;
import ghost.framework.web.mvc.context.bind.annotation.Controller;
import ghost.framework.web.mvc.context.bind.annotation.ResponseBody;
import ghost.framework.web.mvc.nginx.ui.plugin.config.InitConfig;
import ghost.framework.web.mvc.nginx.ui.plugin.entity.Http;
import ghost.framework.web.mvc.nginx.ui.plugin.entity.LogInfo;
import ghost.framework.web.mvc.nginx.ui.plugin.service.HttpService;
import ghost.framework.web.mvc.nginx.ui.plugin.service.SettingService;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

@Controller("/api")
//@RequestMapping("/adminPage/http")
public class HttpController extends ControllerBase {
	@Autowired
	HttpService httpService;
	@Autowired
	SettingService settingService;

	@RequestMapping("")
	public ModelAndView index(HttpSession httpSession, ModelAndView modelAndView) {
//		List<Http> httpList = sqlHelper.findAll(new Sort("seq", Direction.ASC), Http.class);
		List<Http> httpList = sessionFactory.findAll(Http.class);

		modelAndView.addObject("httpList", httpList);
		modelAndView.setViewName("/http/index");
		return modelAndView;
	}

	@RequestMapping("addOver")
	@ResponseBody
	public DataResponse addOver(Http http) throws SQLException {
		if (StrUtil.isEmpty(http.getId())) {
			http.setSeq(httpService.buildOrder());
		}
//		sqlHelper.insertOrUpdate(http);
		sessionFactory.insert(http);
		return DataResponse.success();
	}

	@RequestMapping("detail")
	@ResponseBody
	public DataResponse detail(String id) throws SQLException {
		return DataResponse.success(sessionFactory.findById(Http.class, id));
	}

	@RequestMapping("del")
	@ResponseBody
	public DataResponse del(String id) throws SQLException {
//		sqlHelper.deleteById(id, Http.class);
		sessionFactory.deleteById(Http.class, id);
		return DataResponse.success();
	}

	@RequestMapping("addGiudeOver")
	@ResponseBody
	public DataResponse addGiudeOver(String json, Boolean logStatus, Boolean webSocket) {
		List<Http> https = JSONUtil.toList(JSONUtil.parseArray(json), Http.class);

		if (logStatus) {
			Http http = new Http();
			http.setName("log_format");
			http.setValue("main escape=json '" + buildLogFormat() + "'");
			http.setUnit("");
			https.add(http);

			http = new Http();
			http.setName("access_log");
			http.setValue(InitConfig.home + "log/access.log main");
			http.setUnit("");
			https.add(http);

		}

		if (webSocket) {
			Http http = new Http();
			http.setName("map");
			http.setValue("$http_upgrade $connection_upgrade {\r\n" + "    default upgrade;\r\n" + "    '' close;\r\n" + "}\r\n" + "");
			http.setUnit("");
			https.add(http);
		}

		httpService.setAll(https);

		return DataResponse.success();
	}

	private String buildLogFormat() {
		LogInfo logInfo = new LogInfo();
		logInfo.setRemoteAddr("$remote_addr");
		logInfo.setRemoteUser("$remote_user");
		logInfo.setTimeLocal("$time_local");
		logInfo.setRequest("$request");
		logInfo.setHttpHost("$http_host");
		logInfo.setStatus("$status");
		logInfo.setRequestLength("$request_length");
		logInfo.setBodyBytesDent("$body_bytes_sent");
		logInfo.setHttpReferer("$http_referer");
		logInfo.setHttpUserAgent("$http_user_agent");
		logInfo.setRequestTime("$request_time");
		logInfo.setUpstreamResponseTime("$upstream_response_time");

		return JSONUtil.toJsonStr(logInfo);
	}

	@RequestMapping("setOrder")
	@ResponseBody
	public DataResponse setOrder(String id, Integer count) {
		httpService.setSeq(id, count);
		return DataResponse.success();
	}
}