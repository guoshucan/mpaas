package ghost.framework.web.mvc.nginx.ui.plugin.service;
import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Service;
import ghost.framework.data.hibernate.ISessionFactory;
import ghost.framework.web.mvc.nginx.ui.plugin.entity.Setting;

import java.sql.SQLException;

@Service
public class SettingService {
	@Autowired
	@Application
	private ISessionFactory sessionFactory;
	public void set(String key, String value) throws SQLException {
		Setting setting = sqlHelper.findOneByQuery(new ConditionAndWrapper().eq("key", key), Setting.class);
		if (setting == null) {
			setting = new Setting();
			setting.setKey(key);
			setting.setValue(value);
			sessionFactory.insert(setting);
			return;
		}
		setting.setKey(key);
		setting.setValue(value);
		sessionFactory.update(setting);
	}

	public String get(String key) {
		Setting setting = sqlHelper.findOneByQuery(new ConditionAndWrapper().eq("key", key), Setting.class);

		if (setting == null) {
			return null;
		} else {
			return setting.getValue();
		}
	}
}
