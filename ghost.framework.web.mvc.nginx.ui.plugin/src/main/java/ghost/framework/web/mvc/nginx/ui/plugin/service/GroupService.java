package ghost.framework.web.mvc.nginx.ui.plugin.service;
import cn.hutool.core.util.StrUtil;
import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Service;
import ghost.framework.data.hibernate.ISessionFactory;
import ghost.framework.web.angular1x.ssh.plugin.entity.SshGroupEntity;
import ghost.framework.web.mvc.nginx.ui.plugin.entity.Remote;

import java.sql.SQLException;
import java.util.List;

@Service
public class GroupService {
	/**
	 * 注入会话工厂
	 */
	@Autowired
	@Application
	private ISessionFactory sessionFactory;


//	@Transactional
	public synchronized void delete(String id) throws SQLException {

		sessionFactory.deleteById(SshGroupEntity.class, id);

		List<Remote> remoteList = sqlHelper.findListByQuery(new ConditionAndWrapper().eq("parentId", id), Remote.class);
		for (Remote remote : remoteList) {
			remote.setParentId(null);
			sqlHelper.updateAllColumnById(remote);
		}

		List<SshGroupEntity> groupList = sqlHelper.findListByQuery(new ConditionAndWrapper().eq("parentId", id), SshGroupEntity.class);
		for (SshGroupEntity group : groupList) {
			group.setParentId(null);
			sqlHelper.updateAllColumnById(group);
		}

	}

	public List<SshGroupEntity> getListByParent(String id) {
		ConditionAndWrapper conditionAndWrapper = new ConditionAndWrapper();
		if (StrUtil.isEmpty(id)) {
			conditionAndWrapper.and(new ConditionOrWrapper().eq("parentId", "").isNull("parentId"));
		} else {
			conditionAndWrapper.eq("parentId", id);
		}

		return sqlHelper.findListByQuery(conditionAndWrapper, SshGroupEntity.class);
	}

}
