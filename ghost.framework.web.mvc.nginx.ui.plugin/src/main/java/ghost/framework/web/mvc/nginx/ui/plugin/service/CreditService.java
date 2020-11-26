package ghost.framework.web.mvc.nginx.ui.plugin.service;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Service;
import ghost.framework.data.hibernate.ISessionFactory;
import ghost.framework.web.mvc.nginx.ui.plugin.entity.Credit;

import java.sql.SQLException;

@Service
public class CreditService {
	/**
	 * 注入会话工厂
	 */
	@Autowired
	@Application
	private ISessionFactory sessionFactory;

	
	public String make() throws SQLException {
		Credit credit = new Credit();
		credit.setKey(UUID.randomUUID().toString());

		sessionFactory.insert(credit);
		
		return credit.getKey();
	}

	public boolean check(String key) {
		if(StrUtil.isEmpty(key)) {
			return false;
		}
		
		Credit credit = sqlHelper.findOneByQuery(new ConditionAndWrapper().eq("key", key), Credit.class);

		if (credit == null) {
			return false;
		} else {
			return true;
		}
	}
}
