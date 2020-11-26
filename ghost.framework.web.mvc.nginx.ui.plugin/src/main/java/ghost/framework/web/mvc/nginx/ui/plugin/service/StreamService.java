package ghost.framework.web.mvc.nginx.ui.plugin.service;
import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Service;
import ghost.framework.data.hibernate.ISessionFactory;
import ghost.framework.web.mvc.nginx.ui.plugin.entity.Stream;

import java.sql.SQLException;
import java.util.List;

@Service
public class StreamService {
	@Autowired
	@Application
	private ISessionFactory sessionFactory;

	public void setSeq(String streamId, Integer seqAdd) throws SQLException {
		Stream http = sessionFactory.findById(Stream.class, streamId);

		List<Stream> httpList = sqlHelper.findAll(new Sort("seq", Direction.ASC), Stream.class);
		if (httpList.size() > 0) {
			Stream tagert = null;
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

				sessionFactory.update(tagert);
				sessionFactory.update(http);
			}

		}

	}

	public Long buildOrder() {

		Stream stream = sqlHelper.findOneByQuery(new Sort("seq", Direction.DESC), Stream.class);
		if (stream != null) {
			return stream.getSeq() + 1;
		}

		return 0l;
	}
}