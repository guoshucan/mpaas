package ghost.framework.core.maven.annotation.reader;

import ghost.framework.beans.annotation.application.ApplicationDependency;
import ghost.framework.beans.annotation.application.ApplicationDependencys;
import ghost.framework.core.application.reader.IApplicationReader;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:注释 {@link ApplicationDependency} 读取器类
 * @Date: 19:35 2020/1/12
 */
public interface IAnnotationApplicationMavenDependencyReader extends IApplicationReader {
    /**
     * 获取注释列表
     *
     * @param c
     * @return
     */
    default List<ApplicationDependency> getList(Class<?> c) {
        List<ApplicationDependency> list = new ArrayList<>();
        //获取启动类注释的模块依赖包列表
        if (c.isAnnotationPresent(ApplicationDependencys.class)) {
            for (ApplicationDependency dependency : c.getAnnotation(ApplicationDependencys.class).value()) {
                list.add(this.getApp().getProxyAnnotationObject(dependency));
            }
        }
        if (c.isAnnotationPresent(ApplicationDependency.class)) {
            list.add(c.getAnnotation(ApplicationDependency.class));
        }
        return list;
    }
}