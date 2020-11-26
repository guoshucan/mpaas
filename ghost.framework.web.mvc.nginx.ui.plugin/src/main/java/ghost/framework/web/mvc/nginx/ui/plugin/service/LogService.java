package ghost.framework.web.mvc.nginx.ui.plugin.service;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.json.JSONUtil;
import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Service;
import ghost.framework.data.hibernate.ISessionFactory;
import ghost.framework.data.jdbc.template.JdbcTemplate;
import ghost.framework.web.mvc.nginx.ui.plugin.entity.Log;
import ghost.framework.web.mvc.nginx.ui.plugin.entity.LogInfo;
import ghost.framework.web.mvc.nginx.ui.plugin.ext.DataGroup;
import ghost.framework.web.mvc.nginx.ui.plugin.ext.KeyValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class LogService {
	/**
	 * 注入会话工厂
	 */
	@Autowired
	@Application
	private ISessionFactory sessionFactory;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	//	@Transactional
	public synchronized DataGroup buildDataGroup(String path) throws SQLException {
		if (!new File(path).exists()) {
			return null;
		}
		insertIntoDb(path);

		DataGroup dataGroup = new DataGroup();
		// pvuv
		dataGroup.setPv(jdbcTemplate.query("select hour as name,count(1) as value FROM log_info group by hour order by name", new BeanPropertyRowMapper<KeyValue>(KeyValue.class)));
		dataGroup.setUv(jdbcTemplate.query("SELECT name, COUNT(value) as value " + //
				"FROM ( " + //
				"	SELECT hour AS name, COUNT(remote_addr) AS value " + //
				"	FROM log_info " + //
				"	GROUP BY hour, remote_addr " + //
				"	ORDER BY name " + //
				") " + //
				"GROUP BY name", new BeanPropertyRowMapper<KeyValue>(KeyValue.class)));

		// 状态
		dataGroup.setStatus(jdbcTemplate.query("select status as name,count(1) as value FROM log_info group by status", new BeanPropertyRowMapper<KeyValue>(KeyValue.class)));

		// 系统
		dataGroup.setBrowser(new ArrayList<KeyValue>());
		String[] browsers = new String[]{"Android", "iPhone", "Windows", "Macintosh"};
		Integer allCount = 0;
		for (String browser : browsers) {
			KeyValue keyValue = new KeyValue();
			keyValue.setName(browser);
			keyValue.setValue(jdbcTemplate.queryForObject("select count(1) from log_info where http_user_agent like '%" + browser + "%'", Integer.class));
			dataGroup.getBrowser().add(keyValue);
			allCount += keyValue.getValue();
		}

		KeyValue keyValue = new KeyValue();
		keyValue.setName("Other");
		keyValue.setValue(sessionFactory.count(LogInfo.class).intValue() - allCount);
		dataGroup.getBrowser().add(keyValue);

		// 域名
		List<KeyValue> httpReferer = jdbcTemplate.query("select http_host as name,count(1) as value FROM log_info group by http_host order by value DESC limit 10",
				new BeanPropertyRowMapper<KeyValue>(KeyValue.class));
		Collections.reverse(httpReferer);
		dataGroup.setHttpReferer(httpReferer);

		saveLog(dataGroup, path);


		return dataGroup;
	}

	public void clearDb() {
		sqlHelper.deleteByQuery(new ConditionAndWrapper(), LogInfo.class);
		jdbcTemplate.execute("vacuum;"); // 缩小sqlite.db大小
	}

	private void insertIntoDb(String path) {
		BufferedReader reader = null;
		try {
			File zipFile = new File(path);
			File outFile = new File(path.replace(".zip", "") + File.separator + zipFile.getName().replace(".zip", ".log"));
			ZipUtil.unzip(zipFile);

			sqlHelper.deleteByQuery(new ConditionAndWrapper(), LogInfo.class);

			Long count = 0l;

			reader = FileUtil.getReader(outFile, "UTF-8");
			List<Object> list = new ArrayList<Object>();
			while (true) {
				String json = reader.readLine();
				if (StrUtil.isEmpty(json)) {
					sessionFactory.insertAll(list);
					count += list.size();
					list.clear();

					break;
				}

				json = json.replace("\\x", "");
				if (JSONUtil.isJson(json)) {
					LogInfo logInfo = JSONUtil.toBean(json, LogInfo.class);
					String[] str = logInfo.getTimeLocal().split(":");
					logInfo.setHour(str[1]);
					logInfo.setMinute(str[2]);
					logInfo.setSecond(str[3].split(" ")[0]);

					list.add(logInfo);
				} else {
					System.err.println(json);
				}

				if (list.size() == 1000) {
					sessionFactory.insertAll(list);
					count += list.size();
					list.clear();
				}
			}

			logger.info("插入LogInfo:" + count + "条");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IoUtil.close(reader);
			FileUtil.del(path.replace(".zip", "") + File.separator);
		}
	}

	private void saveLog(DataGroup dataGroup, String path) throws SQLException {
		Log log = new Log();
		File file = new File(path);
		log.setDate(file.getName().replace("access.", "").replace(".zip", ""));
		log.setJson(JSONUtil.toJsonStr(dataGroup));
		log.setPath(path);

		Log logOrg = sqlHelper.findOneByQuery(new ConditionAndWrapper().eq("date", log.getDate()), Log.class);
		if (logOrg != null) {
			DataGroup dataGroupOrg = JSONUtil.toBean(logOrg.getJson(), DataGroup.class);
			sum(dataGroupOrg, dataGroup);
			logOrg.setJson(JSONUtil.toJsonStr(dataGroupOrg));

			sessionFactory.update(logOrg);
		} else {
			sessionFactory.insert(log);
		}
	}

	private void sum(DataGroup dataGroupOrg, DataGroup dataGroup) {
		addSum(dataGroupOrg.getPv(), dataGroup.getPv());
		addSum(dataGroupOrg.getUv(), dataGroup.getUv());
		addSum(dataGroupOrg.getStatus(), dataGroup.getStatus());
		addSum(dataGroupOrg.getBrowser(), dataGroup.getBrowser());
		addSum(dataGroupOrg.getHttpReferer(), dataGroup.getHttpReferer());
	}

	private void addSum(List<KeyValue> keyValuesOrg, List<KeyValue> keyValues) {
		for (KeyValue keyValue : keyValues) {
			boolean hasSame = false;
			for (KeyValue keyValueOrg : keyValuesOrg) {
				if (keyValueOrg.getName().equals(keyValue.getName())) {
					keyValueOrg.setValue(keyValueOrg.getValue() + keyValue.getValue());
					hasSame = true;
				}
			}

			if (!hasSame) {
				keyValuesOrg.add(keyValue);
			}
		}
	}

	public Page search(Page page) {
		page = sqlHelper.findPage(page, Log.class);
		return page;
	}
}