package ghost.framework.web.mvc.nginx.ui.plugin.controller;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.data.commons.domain.Page;
import ghost.framework.web.angular1x.context.controller.ControllerBase;
import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.http.responseContent.DataResponse;
import ghost.framework.web.mvc.context.ModelAndView;
import ghost.framework.web.mvc.context.bind.annotation.Controller;
import ghost.framework.web.mvc.context.bind.annotation.ResponseBody;
import ghost.framework.web.mvc.nginx.ui.plugin.config.ScheduleTask;
import ghost.framework.web.mvc.nginx.ui.plugin.entity.Log;
import ghost.framework.web.mvc.nginx.ui.plugin.service.LogService;
import ghost.framework.web.mvc.nginx.ui.plugin.service.SettingService;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;

@Controller("/api")
//@RequestMapping("/adminPage/log")
public class LogController extends ControllerBase {
	@Autowired
	SettingService settingService;
	@Autowired
	LogService logService;
	@Autowired
	ScheduleTask scheduleTask;

	@RequestMapping("")
	public ModelAndView index(HttpSession httpSession, ModelAndView modelAndView, Page page) {
		page = logService.search(page);
		modelAndView.addObject("page", page);
		modelAndView.setViewName("/log/index");
		return modelAndView;
	}

	@RequestMapping("del")
	@ResponseBody
	public DataResponse del(String id) throws SQLException{
//		sqlHelper.deleteById(id, Log.class);
		sessionFactory.deleteById(Log.class, id);
		return DataResponse.success();
	}

	@RequestMapping("delAll")
	@ResponseBody
	public DataResponse delAll(String id) throws SQLException {
//		sqlHelper.deleteByQuery(null, Log.class);
		sessionFactory.delete(Log.class);
		return DataResponse.success();
	}

	@RequestMapping("detail")
	@ResponseBody
	public DataResponse detail(String id) throws SQLException {
//		Log log = sqlHelper.findById(id, Log.class);
		return DataResponse.success(sessionFactory.findById(Log.class, id));

	}


	@RequestMapping("analysis")
	@ResponseBody
	public DataResponse analysis() {
		scheduleTask.diviLog();
		return DataResponse.success();
	}
}