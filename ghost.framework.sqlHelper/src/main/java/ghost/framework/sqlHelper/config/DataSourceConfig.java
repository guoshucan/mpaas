package ghost.framework.sqlHelper.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import java.io.File;
import java.io.InputStream;

@Configuration
public class DataSourceConfig {
	@Value("${spring.datasource.url:}")
	String url;
	@Value("${spring.datasource.username:}")
	String username;
	@Value("${spring.datasource.password:}")
	String password;
	@Value("${spring.database.type}")
	String database;

	@Value("${spring.database.sqlite-path:}")
	String sqlitePath;

	@Bean
	public DataSource dataSource() throws Exception {
		HikariDataSource dataSource = new HikariDataSource();
		if (database.toLowerCase().equals("mysql")) {
			dataSource.setJdbcUrl(url);
			dataSource.setUsername(username);// 用户名
			dataSource.setPassword(password);// 密码
			dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		} else if (database.toLowerCase().equals("postgresql")) {
			dataSource.setJdbcUrl(url);
			dataSource.setUsername(username);// 用户名
			dataSource.setPassword(password);// 密码
			dataSource.setDriverClassName("org.postgresql.Driver");
		} else if (database.toLowerCase().equals("sqlite")) {
			if (StrUtil.isEmpty(sqlitePath)) {
				sqlitePath = FileUtil.getUserHomePath() + File.separator + "sqlite.db";
			}
			
			if(!FileUtil.exist(sqlitePath)) {
				ClassPathResource resource = new ClassPathResource("sqlite.db");
				InputStream inputStream = resource.getInputStream();
				FileUtil.writeFromStream(inputStream, sqlitePath);
				System.out.println("release:" + sqlitePath);
			}
			
			dataSource.setJdbcUrl("jdbc:sqlite:" + sqlitePath);
			dataSource.setDriverClassName("org.sqlite.JDBC");
		} else {
			throw new Exception("数据库类型配置错误");
		}

		return dataSource;
	}
}
