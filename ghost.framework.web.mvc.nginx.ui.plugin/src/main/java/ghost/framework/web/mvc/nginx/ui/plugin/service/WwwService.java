package ghost.framework.web.mvc.nginx.ui.plugin.service;
import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Service;
import ghost.framework.data.hibernate.ISessionFactory;
import ghost.framework.web.mvc.nginx.ui.plugin.entity.Www;

@Service
public class WwwService {
	@Autowired
	@Application
	private ISessionFactory sessionFactory;

	public Boolean hasName(String name) {
		return sqlHelper.findCountByQuery(new ConditionAndWrapper().eq("name", name), Www.class) > 0;
	}
}