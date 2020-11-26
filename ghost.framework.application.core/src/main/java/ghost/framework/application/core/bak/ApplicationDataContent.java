//package ghost.framework.app.core;
//
//import ghost.framework.app.context.IApplicationDataContent;
//
//import javax.sql.DataSource;
//
///**
// * package: ghost.framework.app.core
// *
// * @Author: 郭树灿{guoshucan-pc}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:应用数据内容类
// * @Date: 2019-11-15:22:31
// */
//abstract class ApplicationDataContent extends ApplicationProxyContent implements IApplicationDataContent {
//    /**
//     * 初始化应用env基础类
//     * @param rootClass       引导类
//     * @throws Exception
//     */
//    protected ApplicationDataContent( Class<?> rootClass) throws Exception {
//        super(rootClass);
//        this.getLog().info("~ApplicationDataContent");
//    }
//
//    /**
//     * 初始化
//     *
//     * @throws Exception
//     */
//    @Override
//    protected void init() throws Exception {
//        this.getLog().info("~ApplicationDataContent.init->Before");
//        super.init();
////        //初始化数据源
////        String depend = null;
////        DefaultArtifact artifact = null;
////        //在启动类获取是否注释自定义数据源
////        if (this.getRootClass().isAnnotationPresent(CustomDataSource.class)) {
////            CustomDataSource ds = this.getRootClass().getAnnotation(CustomDataSource.class);
////            depend = ds.depend();
////            //下载依赖包
////            artifact = new DefaultArtifact(ds.dependency().groupId(), ds.dependency().artifactId(), null, ds.dependency().version());
////        } else {
////            DefaultDataSource ds = this.getClass().getAnnotation(DefaultDataSource.class);
////            depend = ds.depend();
////            artifact = new DefaultArtifact(ds.dependency().groupId(), ds.dependency().artifactId(), null, ds.dependency().version());
////        }
////        //下载依赖包
////        try {
////            List<URLArtifact> artifactList = Booter.getDependencyNodeDownloadURLArtifactList(
////                    this.getMavenLocalRepositoryFile(),
////                    this.getMavenRepositoryContainer(),
////                    artifact.toString());
////            for (URLArtifact a : artifactList) {
////                this.getClassLoader().loader(a.getFile());
////            }
////        } catch (Exception e) {
////            if (this.log.isDebugEnabled()) {
////                e.printStackTrace();
////                this.log.debug(e.getCause().toString());
////            } else {
////                this.log.error(e.getCause().toString());
////            }
////            return;
////        }
////        //获取数据源
////        this.dataSource = this.getBean(DataSource.class);
////        //判断是否没有绑定过数据源
////        if (this.dataSource == null) {
////            //使用应用类加载器初始化数据源构造器创建从配置文件获取的数据源对象
////            DataSourceBuilder builder = DataSourceBuilder.create(this.getClassLoader());
////            try {
////                this.dataSource = builder.build((DataSource) this.newModuleInstance(builder.getType()));
////            } catch (Exception e) {
////                System.out.println(e.getMessage());
////                //throw new DataSourceException(e);
////            }
////        }
////        this.bean(this.dataSource);
//        this.getLog().info("~ApplicationDataContent.init->After");
//    }
//
//    /**
//     * 数据源
//     */
//    private DataSource dataSource;
//
//    /**
//     * 获取数据源
//     *
//     * @return
//     */
//    @Override
//    public DataSource getDataSource() {
//        return dataSource;
//    }
//}