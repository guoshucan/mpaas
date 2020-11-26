package ghost.framework.web.mvc.nginx.ui.plugin.service;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Service;
import ghost.framework.data.hibernate.ISessionFactory;
import ghost.framework.web.mvc.nginx.ui.plugin.entity.Param;
import ghost.framework.web.mvc.nginx.ui.plugin.entity.Template;

import java.sql.SQLException;
import java.util.List;

@Service
public class ParamService {

	/**
	 * 注入会话工厂
	 */
	@Autowired
	@Application
	private ISessionFactory sessionFactory;

	public String getJsonByTypeId(String id, String type) throws SQLException {
		List<Param> list = sqlHelper.findListByQuery(new ConditionAndWrapper().eq(type + "Id", id), Param.class);
		for (Param param : list) {
			if (StrUtil.isNotEmpty(param.getTemplateValue())) {
				Template template = sessionFactory.findById(Template.class, param.getTemplateValue());
				param.setTemplateName(template.getName());
			}

		}
		return JSONUtil.toJsonStr(list);
	}


	public List<Param> getListByTypeId(String id, String type) {
		List<Param> list = sqlHelper.findListByQuery(new ConditionAndWrapper().eq(type + "Id", id), Param.class);
		return list;
	}
}