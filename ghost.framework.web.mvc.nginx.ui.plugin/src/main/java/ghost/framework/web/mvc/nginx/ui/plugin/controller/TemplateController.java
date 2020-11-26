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
import ghost.framework.web.mvc.nginx.ui.plugin.entity.Param;
import ghost.framework.web.mvc.nginx.ui.plugin.entity.Template;
import ghost.framework.web.mvc.nginx.ui.plugin.ext.TemplateExt;
import ghost.framework.web.mvc.nginx.ui.plugin.service.TemplateService;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller("/api")
//@RequestMapping("/adminPage/template")
public class TemplateController extends ControllerBase {
	@Autowired
	TemplateService templateService;

	@RequestMapping("")
	public ModelAndView index(HttpSession httpSession, ModelAndView modelAndView) {
//		List<Template> templateList = sqlHelper.findAll(Template.class);
		List<Template> templateList = sessionFactory.findAll(Template.class);
		List<TemplateExt> extList = new ArrayList<>();
		for (Template template : templateList) {
			TemplateExt templateExt = new TemplateExt();
			templateExt.setTemplate(template);

			templateExt.setParamList(templateService.getParamList(template.getId()));
			templateExt.setCount(templateExt.getParamList().size());

			extList.add(templateExt);
		}

		modelAndView.addObject("templateList", extList);
		modelAndView.setViewName("/template/index");
		return modelAndView;
	}

	@RequestMapping("addOver")
	@ResponseBody
	public DataResponse addOver(Template template, String paramJson) {

		if (StrUtil.isEmpty(template.getId())) {
			Long count = templateService.getCountByName(template.getName());
			if (count > 0) {
				return DataResponse.error(1, "m.get('templateStr.sameName')");
			}
		} else {
			Long count = templateService.getCountByNameWithOutId(template.getName(), template.getId());
			if (count > 0) {
				return DataResponse.error(2, "m.get('templateStr.sameName')");
			}
		}

		List<Param> params = JSONUtil.toList(JSONUtil.parseArray(paramJson), Param.class);

		templateService.addOver(template, params);

		return DataResponse.success();
	}

	@RequestMapping("detail")
	@ResponseBody
	public DataResponse detail(String id) throws SQLException {
//		Template template = sqlHelper.findById(id, Template.class);
		Template template = sessionFactory.findById(Template.class, id);
		TemplateExt templateExt = new TemplateExt();
		templateExt.setTemplate(template);

		templateExt.setParamList(templateService.getParamList(template.getId()));
		templateExt.setCount(templateExt.getParamList().size());

		return DataResponse.success(templateExt);
	}

	@RequestMapping("del")
	@ResponseBody
	public DataResponse del(String id) {

		templateService.del(id);
		return DataResponse.success();
	}

	@RequestMapping("getTemplate")
	@ResponseBody
	public DataResponse getTemplate() {
		return DataResponse.success(sessionFactory.findAll(Template.class));
	}
}
