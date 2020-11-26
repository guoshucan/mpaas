package ghost.framework.web.mvc.nginx.ui.plugin.controller;

import cn.hutool.core.util.StrUtil;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.web.angular1x.context.controller.ControllerBase;
import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.http.responseContent.DataResponse;
import ghost.framework.web.mvc.context.ModelAndView;
import ghost.framework.web.mvc.context.bind.annotation.Controller;
import ghost.framework.web.mvc.context.bind.annotation.ResponseBody;
import ghost.framework.web.mvc.nginx.ui.plugin.entity.Basic;
import ghost.framework.web.mvc.nginx.ui.plugin.service.BasicService;

import java.sql.SQLException;
import java.util.List;

//@Controller("/adminPage/basic")
@Controller("/api")
public class BasicController extends ControllerBase {
	@Autowired
	BasicService basicService;
	@RequestMapping("")
	public ModelAndView index(ModelAndView modelAndView) {
//		List<Basic> basicList = sqlHelper.findAll(new Sort("seq", Direction.ASC), Basic.class);
		List<Basic> basicList = this.sessionFactory.findAll(Basic.class);
		modelAndView.addObject("basicList", basicList);
		modelAndView.setViewName("/basic/index");
		return modelAndView;
	}

	@RequestMapping("addOver")
	@ResponseBody
	public DataResponse addOver(Basic base) throws SQLException{
		if (StrUtil.isEmpty(base.getId())) {
			base.setSeq(basicService.buildOrder());
		}
//		sqlHelper.insertOrUpdate(base);
		this.sessionFactory.update(Basic.class, base);
		return DataResponse.success();
	}

	@RequestMapping("setOrder")
	@ResponseBody
	public DataResponse setOrder(String id, Integer count) {
		basicService.setSeq(id, count);

		return DataResponse.success();
	}
	
	@RequestMapping("detail")
	@ResponseBody
	public DataResponse detail(String id) throws SQLException{
//		return renderSuccess(sqlHelper.findById(id, Basic.class));
		return DataResponse.success(sessionFactory.findById(Basic.class, id));
	}

	@RequestMapping("del")
	@ResponseBody
	public DataResponse del(String id) throws SQLException {
//		sqlHelper.deleteById(id, Basic.class);
		sessionFactory.deleteById(Basic.class, id);
		return DataResponse.success();
	}

}
