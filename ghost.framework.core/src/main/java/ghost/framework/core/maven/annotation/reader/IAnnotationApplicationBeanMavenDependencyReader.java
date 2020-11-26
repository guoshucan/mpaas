//package ghost.framework.core.maven.annotation.reader;
//
//import ghost.framework.beans.maven.annotation.application.ApplicationBeanMavenDependency;
//import ghost.framework.beans.maven.annotation.application.ApplicationBeanMavenDependencys;
//import ghost.framework.core.application.reader.IApplicationReader;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:注释 {@link ApplicationBeanMavenDependency} 读取器接口
// * @Date: 19:35 2020/1/12
// */
//public interface IAnnotationApplicationBeanMavenDependencyReader extends IApplicationReader {
//    /**
//     * 获取注释列表
//     *
//     * @param c
//     * @return
//     */
//    default List<ApplicationBeanMavenDependency> getList(Class<?> c) {
//        List<ApplicationBeanMavenDependency> list = new ArrayList<>();
//        //获取启动类注释的模块依赖包列表
//        if (c.isAnnotationPresent(ApplicationBeanMavenDependencys.class)) {
//            for (ApplicationBeanMavenDependency dependency : c.getAnnotation(ApplicationBeanMavenDependencys.class).value()) {
//                list.add(this.getApp().getProxyAnnotationObject(dependency));
//            }
//        }
//        if (c.isAnnotationPresent(ApplicationBeanMavenDependency.class)) {
//            list.add(c.getAnnotation(ApplicationBeanMavenDependency.class));
//        }
//        return list;
//    }
//}