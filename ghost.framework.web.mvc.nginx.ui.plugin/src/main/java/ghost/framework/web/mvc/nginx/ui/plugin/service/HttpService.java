package ghost.framework.web.mvc.nginx.ui.plugin.service;
import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Service;
import ghost.framework.data.hibernate.ISessionFactory;
import ghost.framework.web.mvc.nginx.ui.plugin.entity.Http;

import java.sql.SQLException;
import java.util.List;

@Service
public class HttpService {
	/**
	 * 注入会话工厂
	 */
	@Autowired
	@Application
	private ISessionFactory sessionFactory;

	public void setAll(List<Http> https) throws SQLException {
		Http logFormat = null;
		Http accessLog = null;
		for (Http http : https) {
			if (http.getName().equals("log_format")) {
				logFormat = http;
			}
			if (http.getName().equals("access_log")) {
				accessLog = http;
			}
		}
		if (logFormat != null) {
			https.remove(logFormat);
			https.add(logFormat);
		}
		if (accessLog != null) {
			https.remove(accessLog);
			https.add(accessLog);
		}

		for (Http http : https) {
			Http httpOrg = sqlHelper.findOneByQuery(new ConditionAndWrapper().eq("name", http.getName()), Http.class);

			if (httpOrg != null) {
				http.setId(httpOrg.getId());
			}

			http.setSeq(buildOrder());
			http.setValue(http.getValue() + http.getUnit());

			sessionFactory.update(http);

		}

	}

	public void setSeq(String httpId, Integer seqAdd) {
		Http http = sqlHelper.findById(httpId, Http.class);

		List<Http> httpList = sqlHelper.findAll(new Sort("seq", Direction.ASC), Http.class);
		if (httpList.size() > 0) {
			Http tagert = null;
			if (seqAdd < 0) {
				for (int i = 0; i < httpList.size(); i++) {
					if (httpList.get(i).getSeq() < http.getSeq()) {
						tagert = httpList.get(i);
					}
				}
			} else {
				for (int i = httpList.size() - 1; i >= 0; i--) {
					if (httpList.get(i).getSeq() > http.getSeq()) {
						tagert = httpList.get(i);
					}
				}
			}

			if (tagert != null) {
				// 交换seq
				Long seq = tagert.getSeq();
				tagert.setSeq(http.getSeq());
				http.setSeq(seq);

				sqlHelper.updateById(tagert);
				sqlHelper.updateById(http);
			}

		}

	}

	public Long buildOrder() {

		Http http = sqlHelper.findOneByQuery(new Sort("seq", Direction.DESC), Http.class);
		if (http != null) {
			return http.getSeq() + 1;
		}

		return 0l;
	}

}
