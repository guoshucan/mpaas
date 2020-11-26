package ghost.framework.data.core;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:数据库配置
 * @Date: 18:49 2019-05-26
 */
public class DataBasePropertie implements IDataBaseProperties {
    private String id;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    private String dataBaseName;

    @Override
    public String getDataBaseName() {
        return dataBaseName;
    }

    @Override
    public void setDataBaseName(String dataBaseName) {
        this.dataBaseName = dataBaseName;
    }

    private String providerClass;

    @Override
    public String getProviderClass() {
        return providerClass;
    }

    private boolean useQueryCache;

    @Override
    public boolean isUseQueryCache() {
        return useQueryCache;
    }

    private boolean useSecondLevelCache;

    @Override
    public boolean isUseSecondLevelCache() {
        return useSecondLevelCache;
    }

    @Override
    public void setProviderClass(String providerClass) {
        this.providerClass = providerClass;
    }

    @Override
    public void setUseQueryCache(boolean useQueryCache) {
        this.useQueryCache = useQueryCache;
    }

    @Override
    public void setUseSecondLevelCache(boolean useSecondLevelCache) {
        this.useSecondLevelCache = useSecondLevelCache;
    }

    private String factoryClass;

    @Override
    public String getFactoryClass() {
        return factoryClass;
    }

    @Override
    public void setFactoryClass(String factoryClass) {
        this.factoryClass = factoryClass;
    }

    @Override
    public String getUsername() {
        return username;
    }

    private String username;

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    private String password;

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getDriver() {
        return driver;
    }

    private String driver;

    @Override
    public void setDriver(String driver) {
        this.driver = driver;
    }

    private String dialect;

    @Override
    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    @Override
    public String getDialect() {
        return dialect;
    }

    @Override
    public String getIp() {
        return ip;
    }

    private String ip;

    @Override
    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public int getPort() {
        return port;
    }

    private int port;

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public boolean isShowSql() {
        return showSql;
    }

    private boolean showSql;

    @Override
    public void setShowSql(boolean showSql) {
        this.showSql = showSql;
    }

    @Override
    public int getPoolSize() {
        return poolSize;
    }

    @Override
    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    private int poolSize;

    @Override
    public boolean isFormatSql() {
        return formatSql;
    }

    private boolean formatSql;

    @Override
    public void setFormatSql(boolean formatSql) {
        this.formatSql = formatSql;
    }

    @Override
    public boolean isGenerateStatistics() {
        return generateStatistics;
    }

    private boolean generateStatistics;

    @Override
    public void setGenerateStatistics(boolean generateStatistics) {
        this.generateStatistics = generateStatistics;
    }

    @Override
    public boolean isUseIdentifierRollback() {
        return useIdentifierRollback;
    }

    private boolean useIdentifierRollback;

    @Override
    public void setUseIdentifierRollback(boolean useIdentifierRollback) {
        this.useIdentifierRollback = useIdentifierRollback;
    }

    @Override
    public boolean isUseSqlComments() {
        return useSqlComments;
    }

    private boolean useSqlComments;

    @Override
    public void setUseSqlComments(boolean useSqlComments) {
        this.useSqlComments = useSqlComments;
    }

    private boolean orderUpdates;

    @Override
    public boolean isOrderUpdates() {
        return orderUpdates;
    }

    @Override
    public void setOrderUpdates(boolean orderUpdates) {
        this.orderUpdates = orderUpdates;
    }

    @Override
    public String getDefaultEntityMode() {
        return defaultEntityMode;
    }

    private String defaultEntityMode;

    @Override
    public void setDefaultEntityMode(String defaultEntityMode) {
        this.defaultEntityMode = defaultEntityMode;
    }

    @Override
    public int getDefaultBatchFetchSize() {
        return defaultBatchFetchSize;
    }

    private int defaultBatchFetchSize;

    @Override
    public void setDefaultBatchFetchSize(int defaultBatchFetchSize) {
        this.defaultBatchFetchSize = defaultBatchFetchSize;
    }

    @Override
    public int getMaxFetchDepth() {
        return maxFetchDepth;
    }

    private int maxFetchDepth;

    @Override
    public void setMaxFetchDepth(int maxFetchDepth) {
        this.maxFetchDepth = maxFetchDepth;
    }

    @Override
    public String getSessionFactoryName() {
        return sessionFactoryName;
    }

    private String sessionFactoryName;

    @Override
    public void setSessionFactoryName(String sessionFactoryName) {
        this.sessionFactoryName = sessionFactoryName;
    }

    @Override
    public String getDefaultCatalog() {
        return defaultCatalog;
    }

    private String defaultCatalog;

    @Override
    public void setDefaultCatalog(String defaultCatalog) {
        this.defaultCatalog = defaultCatalog;
    }

    @Override
    public String getHbm2ddlAuto() {
        return hbm2ddlAuto;
    }

    private String hbm2ddlAuto;

    @Override
    public void setHbm2ddlAuto(String hbm2ddlAuto) {
        this.hbm2ddlAuto = hbm2ddlAuto;
    }

    @Override
    public String getCharacterEncoding() {
        return characterEncoding;
    }

    private String characterEncoding = "UTF-8";

    @Override
    public void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }

    @Override
    public boolean isUseSSL() {
        return useSSL;
    }

    private boolean useSSL;

    @Override
    public void setUseSSL(boolean useSSL) {
        this.useSSL = useSSL;
    }

    @Override
    public boolean isUseUnicode() {
        return useUnicode;
    }

    private boolean useUnicode;

    @Override
    public void setUseUnicode(boolean useUnicode) {
        this.useUnicode = useUnicode;
    }

    @Override
    public String getCurrentSessionContextClass() {
        return currentSessionContextClass;
    }

    private String currentSessionContextClass;

    @Override
    public void setCurrentSessionContextClass(String currentSessionContextClass) {
        this.currentSessionContextClass = currentSessionContextClass;
    }
}
