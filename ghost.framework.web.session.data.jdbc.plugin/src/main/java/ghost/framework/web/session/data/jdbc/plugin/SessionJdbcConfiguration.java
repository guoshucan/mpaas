package ghost.framework.web.session.data.jdbc.plugin;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ghost.framework.beans.annotation.bean.Bean;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.injection.Value;
import ghost.framework.beans.annotation.invoke.Invoke;
import ghost.framework.beans.annotation.module.Module;
import ghost.framework.beans.annotation.order.Order;
import ghost.framework.beans.annotation.stereotype.Configuration;
import ghost.framework.context.module.IModule;
import ghost.framework.context.utils.AssemblyUtil;
import ghost.framework.data.jdbc.support.JdbcUtils;
import ghost.framework.data.jdbc.support.MetaDataAccessException;
import ghost.framework.data.jdbc.support.lob.DefaultLobHandler;
import ghost.framework.data.jdbc.support.lob.LobHandler;
import ghost.framework.data.jdbc.template.JdbcTemplate;
import ghost.framework.transaction.TransactionDefinition;
import ghost.framework.transaction.TransactionOperations;
import ghost.framework.transaction.support.PlatformTransactionManager;
import ghost.framework.transaction.support.TransactionTemplate;
import ghost.framework.util.StringUtils;
import ghost.framework.web.session.core.*;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.util.Timer;
import java.util.TimerTask;
/**
 * jdbc配置
 */
@Configuration
public class SessionJdbcConfiguration {
	public SessionJdbcConfiguration() {

	}
	/**
	 * 注入web模块容器接口
	 */
	@Module("ghost.framework.web.module")
	@Autowired
	private IModule module;
	private Logger logger = Logger.getLogger(SessionJdbcConfiguration.class);
	/**
	 * 定时清理
	 */
	static final String DEFAULT_CLEANUP_CRON = "0 * * * * *";
	/**
	 * 默认会话超时
	 */
	private Integer maxInactiveIntervalInSeconds = MapSession.DEFAULT_MAX_INACTIVE_INTERVAL_SECONDS;
	/**
	 * 会话数据库表名称
	 */
	private String tableName = JdbcIndexedSessionRepository.Constant.DEFAULT_TABLE_NAME;

	private String cleanupCron = DEFAULT_CLEANUP_CRON;
	/**
	 * 刷新模式
	 */
	private FlushMode flushMode = FlushMode.NotTimely;
	/**
	 * 保存模式
	 */
	private SaveMode saveMode = SaveMode.ON_SET_ATTRIBUTE;
	/**
	 * 平台事务
	 */
	private PlatformTransactionManager transactionManager;

	private TransactionOperations transactionOperations;
	private LobHandler lobHandler = new DefaultLobHandler();

	/**
	 * @param sessionRepository
	 */
	@Invoke
	@Order(4)
	public void setSessionRepositoryFilter(@Autowired JdbcIndexedSessionRepository sessionRepository) {
		//获取会话过滤器接口
		ISessionRepositoryFilter sessionRepositoryFilter = this.module.getBean(ISessionRepositoryFilter.class);
		//设置会话仓库
		sessionRepositoryFilter.setSessionRepository(sessionRepository);
	}

	@Order(1)
	@Bean
	public JdbcIndexedSessionRepository sessionRepository(@Autowired DataSource dataSource) {
		JdbcTemplate jdbcTemplate = createJdbcTemplate(dataSource);
//		if (this.transactionOperations == null) {
//			this.transactionOperations = createTransactionTemplate(this.transactionManager);
//		}
		//初始化数据库
		//获取当前数据库类型数据库脚本文件
		if (!jdbcTemplate.isTableExist(this.tableName)) {
			jdbcTemplate.execute(AssemblyUtil.getResourceString(this.getClass(), "sql/schema-" + this.productName + ".sql"));
		}
		//
		JdbcIndexedSessionRepository sessionRepository = new JdbcIndexedSessionRepository(this.module, jdbcTemplate, this.transactionOperations);
		if (StringUtils.hasText(this.tableName)) {
			sessionRepository.setTableName(this.tableName);
		}
		sessionRepository.setDefaultMaxInactiveInterval(this.maxInactiveIntervalInSeconds);
		sessionRepository.setFlushMode(this.flushMode);
		sessionRepository.setSaveMode(this.saveMode);
		if (this.lobHandler != null) {
			sessionRepository.setLobHandler(this.lobHandler);
		} else if (requiresTemporaryLob(dataSource)) {
			DefaultLobHandler lobHandler = new DefaultLobHandler();
			lobHandler.setCreateTemporaryLob(true);
			sessionRepository.setLobHandler(lobHandler);
		}
		return sessionRepository;
	}

	/**
	 * 判断是否为Oracle数据库
	 *
	 * @param dataSource
	 * @return
	 */
	private static boolean requiresTemporaryLob(DataSource dataSource) {
		try {
			String productName = JdbcUtils.extractDatabaseMetaData(dataSource, "getDatabaseProductName");
			return "Oracle".equalsIgnoreCase(JdbcUtils.commonDatabaseName(productName));
		} catch (MetaDataAccessException ex) {
			return false;
		}
	}

	public void setMaxInactiveIntervalInSeconds(Integer maxInactiveIntervalInSeconds) {
		this.maxInactiveIntervalInSeconds = maxInactiveIntervalInSeconds;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setCleanupCron(String cleanupCron) {
		this.cleanupCron = cleanupCron;
	}

	public void setFlushMode(FlushMode flushMode) {
		this.flushMode = flushMode;
	}

	public void setSaveMode(SaveMode saveMode) {
		this.saveMode = saveMode;
	}


	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public void setTransactionOperations(TransactionOperations transactionOperations) {
		this.transactionOperations = transactionOperations;
	}

	public void setLobHandler(LobHandler lobHandler) {
		this.lobHandler = lobHandler;
	}

	/**
	 * 创建数据源
	 *
	 * @param driverClass
	 * @param url
	 * @param user
	 * @param pwd
	 * @param poolName
	 * @param idleTimeout
	 * @param connectionTimeout
	 * @param maximumPoolSize
	 * @return
	 */
	@Order
	@Bean
	public HikariDataSource createDataSource(
//			@Value("ghost.framework.session.datasource.type") String type,
			@Value("ghost.framework.session.datasource.driver-class-name") String driverClass,
			@Value("ghost.framework.session.datasource.url") String url,
			@Value("ghost.framework.session.datasource.username") String user,
			@Value("ghost.framework.session.datasource.password") String pwd,
			@Value("ghost.framework.session.hikari.poolName") String poolName,
			@Value("ghost.framework.session.hikari.idleTimeout") int idleTimeout,
			@Value("ghost.framework.session.hikari.connectionTimeout") int connectionTimeout,
			@Value("ghost.framework.session.hikari.maximumPoolSize") int maximumPoolSize) {
		HikariConfig config = new HikariConfig();
		HikariDataSource dataSource = null;
		try {
//			config.setDataSourceClassName(type);
			config.setDriverClassName(driverClass);
			config.setJdbcUrl(url);
			config.setUsername(user);
			config.setPassword(pwd);
			config.setPoolName(poolName);
			config.setIdleTimeout(idleTimeout);
			config.setConnectionTimeout(connectionTimeout);
			config.setMaximumPoolSize(maximumPoolSize);
			dataSource = new HikariDataSource(config);
			this.productName = JdbcUtils.extractDatabaseMetaData(dataSource, "getDatabaseProductName").toString().toLowerCase();
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				e.printStackTrace();
				logger.debug(e.getMessage(), e);
			} else {
				logger.error(e.getMessage(), e);
			}
		}
		return dataSource;
	}

	private String productName;

	private JdbcTemplate createJdbcTemplate(DataSource dataSource) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.afterPropertiesSet();
		return jdbcTemplate;
	}

	private static TransactionTemplate createTransactionTemplate(PlatformTransactionManager transactionManager) {
		TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
		transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		transactionTemplate.afterPropertiesSet();
		return transactionTemplate;
	}

	/**
	 * 创建定时器清理会话数据
	 *
	 * @param sessionRepository
	 * @return
	 */
	@Order(3)
	@Bean
	public Timer createTimer(@Autowired SessionRepository<Session> sessionRepository) {
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				try {
					sessionRepository.cleanUpExpiredSessions();
					logger.debug("cleanUpExpiredSessions");
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		};
		// 定义开始等待时间  --- 等待 5 秒
		// 1000ms = 1s
		long delay = 5000;
		// 定义每次执行的间隔时间
		long intevalPeriod = 5 * 1000;
		timer.scheduleAtFixedRate(task, delay, intevalPeriod);
		return timer;
	}
}