//package ghost.framework.core.maven.annotation.reader;
//
//import ghost.framework.beans.maven.annotation.module.MavenModuleBeanDependency;
//import ghost.framework.beans.maven.annotation.module.MavenModuleBeanDependencys;
//import ghost.framework.core.application.reader.IApplicationReader;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:注释 {@link MavenModuleBeanDependency} 读取器接口
// * @Date: 19:43 2020/1/12
// */
//public interface IAnnotationBeanMavenDependencyReader extends IApplicationReader {
//    /**
//     * 获取注释列表
//     * @param c
//     * @return
//     */
//    default List<MavenModuleBeanDependency> getList(Class<?> c) {
//        List<MavenModuleBeanDependency> list = new ArrayList<>();
//        //获取启动类注释的模块依赖包列表
//        if (c.isAnnotationPresent(MavenModuleBeanDependencys.class)) {
//            for (MavenModuleBeanDependency dependency : c.getAnnotation(MavenModuleBeanDependencys.class).value()) {
//                list.add(this.getApp().getProxyAnnotationObject(dependency));
//            }
//        }
//        if (c.isAnnotationPresent(MavenModuleBeanDependency.class)) {
//            list.add(c.getAnnotation(MavenModuleBeanDependency.class));
//        }
//        return list;
//    }
//}
