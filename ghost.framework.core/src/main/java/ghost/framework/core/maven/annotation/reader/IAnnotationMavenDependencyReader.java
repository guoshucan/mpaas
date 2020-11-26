package ghost.framework.core.maven.annotation.reader;

import ghost.framework.beans.maven.annotation.MavenDependency;
import ghost.framework.beans.maven.annotation.MavenDependencys;
import ghost.framework.core.application.reader.IApplicationReader;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:注释 {@link ghost.framework.beans.maven.annotation.MavenDependency} 读取器接口
 * @Date: 19:45 2020/1/12
 */
public interface IAnnotationMavenDependencyReader extends IApplicationReader {
    /**
     * 获取注释列表
     * @param c
     * @return
     */
    default List<MavenDependency> getList(Class<?> c) {
        List<MavenDependency> list = new ArrayList<>();
        //获取启动类注释的模块依赖包列表
        if (c.isAnnotationPresent(MavenDependencys.class)) {
            for (MavenDependency dependency : c.getAnnotation(MavenDependencys.class).value()) {
                list.add(this.getApp().getProxyAnnotationObject(dependency));
            }
        }
        if (c.isAnnotationPresent(MavenDependency.class)) {
            list.add(c.getAnnotation(MavenDependency.class));
        }
        return list;
    }
}
