package ghost.framework.web.mvc.nginx.ui.plugin.service;
import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Service;
import ghost.framework.data.hibernate.ISessionFactory;
import ghost.framework.web.mvc.nginx.ui.plugin.entity.Basic;

import java.sql.SQLException;
import java.util.List;

@Service
public class BasicService {
	/**
	 * 注入会话工厂
	 */
	@Autowired
	@Application
	private ISessionFactory sessionFactory;

	public Long buildOrder() {

		Basic basic = sqlHelper.findOneByQuery(new Sort("seq", Direction.DESC), Basic.class);
		if (basic != null) {
			return basic.getSeq() + 1;
		}

		return 0l;
	}

//	@Transactional
	public void setSeq(String basicId, Integer seqAdd) throws SQLException {
		Basic basic = sessionFactory.findById(Basic.class, basicId);

		List<Basic> basicList = sqlHelper.findAll(new Sort("seq", Direction.ASC), Basic.class);
		if (basicList.size() > 0) {
			Basic tagert = null;
			if (seqAdd < 0) {
				for (int i = 0; i < basicList.size(); i++) {
					if (basicList.get(i).getSeq() < basic.getSeq()) {
						tagert = basicList.get(i);
					}
				}
			} else {
				for (int i = basicList.size() - 1; i >= 0; i--) {
					if (basicList.get(i).getSeq() > basic.getSeq()) {
						tagert = basicList.get(i);
					}
				}
			}

			if (tagert != null) {
				// 交换seq
				Long seq = tagert.getSeq();
				tagert.setSeq(basic.getSeq());
				basic.setSeq(seq);

//				sqlHelper.updateById(tagert);
//				sqlHelper.updateById(basic);

				sessionFactory.update(tagert);
				sessionFactory.update(basic);
			}

		}

	}

	public boolean contain(String content) {
		return sqlHelper.findCountByQuery(new ConditionOrWrapper().like("value", content).like("name", content), Basic.class) > 0;
	}

}
