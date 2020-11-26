package ghost.framework.web.mvc.nginx.ui.plugin.service;

import cn.craccd.sqlHelper.utils.ConditionAndWrapper;
import cn.craccd.sqlHelper.utils.SqlHelper;
import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Service;
import ghost.framework.data.hibernate.ISessionFactory;
import ghost.framework.web.mvc.nginx.ui.plugin.entity.Param;
import ghost.framework.web.mvc.nginx.ui.plugin.entity.Template;

import java.sql.SQLException;
import java.util.List;

@Service
public class TemplateService {
	@Autowired
	@Application
	private ISessionFactory sessionFactory;

//	@Transactional
	public void addOver(Template template, List<Param> params) throws SQLException {
		sessionFactory.insert(template);

		sqlHelper.deleteByQuery(new ConditionAndWrapper().eq("templateId", template.getId()), Param.class);

		for (Param param : params) {
			param.setTemplateId(template.getId());
			sessionFactory.update(param);
		}
	}

	public List<Param> getParamList(String templateId) {
		return sqlHelper.findListByQuery(new ConditionAndWrapper().eq("templateId", templateId), Param.class);
	}

//	@Transactional
	public synchronized void del(String id) throws SQLException{
		sessionFactory.deleteById(Template.class, id);
		sqlHelper.deleteByQuery(new ConditionAndWrapper().eq("templateId", id), Param.class);
		sqlHelper.deleteByQuery(new ConditionAndWrapper().eq("templateValue", id), Param.class);
	}

	public Long getCountByName(String name) {
		return sqlHelper.findCountByQuery(new ConditionAndWrapper().eq("name", name), Template.class);
	}

	public Long getCountByNameWithOutId(String name, String id) {
		return sqlHelper.findCountByQuery(new ConditionAndWrapper().eq("name", name).ne("id", id), Template.class);
	}

}
