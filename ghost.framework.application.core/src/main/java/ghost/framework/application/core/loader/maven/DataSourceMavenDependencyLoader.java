//package ghost.framework.app.core.loader.maven;
//
//import ghost.framework.core.application.loader.maven.IDataSourceMavenDependencyLoader;
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.core.application.loader.maven.AbstractApplicationMavenLoader;
//import ghost.framework.context.base.ICoreInterface;
//import ghost.framework.context.event.IEventTargetHandle;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:应用数据源maven依赖加载器
// * @Date: 21:33 2019/12/19
// */
//public class DataSourceMavenDependencyLoader<O extends ICoreInterface, T, E extends IEventTargetHandle<O, T>>
//        extends AbstractApplicationMavenLoader<O, T, E>
//        implements IDataSourceMavenDependencyLoader<O ,T, E> {
//
//    public DataSourceMavenDependencyLoader(@Autowired IApplication app) {
//        super(app);
//    }
//
//
//    @Override
//    public void put(Object target, Class<?> c) {
//
//    }
//
//    @Override
//    public void loader(E event) {
//
//    }
//}