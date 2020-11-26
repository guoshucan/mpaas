package ghost.framework.core.maven.annotation.reader;

import ghost.framework.beans.maven.annotation.module.MavenModuleDependency;
import ghost.framework.beans.maven.annotation.module.MavenModuleDependencys;
import ghost.framework.core.application.reader.IApplicationReader;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:注释 {@link MavenModuleDependency} 读取器接口
 * @Date: 19:23 2020/1/12
 */
public interface IAnnotationModuleMavenDependencyReader extends IApplicationReader {
    /**
     * 获取注释列表
     * @param c
     * @return
     */
    default List<MavenModuleDependency> getList(Class<?> c) {
        List<MavenModuleDependency> list = new ArrayList<>();
        //获取启动类注释的模块依赖包列表
        if (c.isAnnotationPresent(MavenModuleDependencys.class)) {
            for (MavenModuleDependency dependency : c.getAnnotation(MavenModuleDependencys.class).value()) {
                list.add(this.getApp().getProxyAnnotationObject(dependency));
            }
        }
        if (c.isAnnotationPresent(MavenModuleDependency.class)) {
            list.add(this.getApp().getProxyAnnotationObject(c.getAnnotation(MavenModuleDependency.class)));
        }
        return list;
    }
}