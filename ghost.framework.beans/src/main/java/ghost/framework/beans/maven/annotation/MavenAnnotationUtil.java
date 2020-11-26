package ghost.framework.beans.maven.annotation;

import ghost.framework.beans.annotation.configuration.properties.ConfigurationProperties;
import ghost.framework.beans.annotation.configuration.properties.ConfigurationPropertiess;
import ghost.framework.beans.annotation.application.ApplicationDependency;
import ghost.framework.beans.annotation.application.ApplicationDependencys;
import ghost.framework.beans.maven.annotation.module.MavenModuleDependency;
import ghost.framework.beans.maven.annotation.module.MavenModuleDependencys;
import ghost.framework.beans.annotation.plugin.PluginDependency;
import ghost.framework.maven.FileArtifact;

import java.util.*;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:注释工具
 * @Date: 14:27 2019/12/8
 */
public final class MavenAnnotationUtil {
    /**
     * 获取注释maven依赖列表
     *
     * @param c
     * @return
     */
    public static List<MavenDependency> getAnnotationMavenDependencyList(Class<?> c) {
        List<MavenDependency> list = new ArrayList<>();
        //获取注释模块引用列表
        if (c.isAnnotationPresent(MavenDependencys.class)) {
            for (MavenDependency dependency : c.getAnnotation(MavenDependencys.class).value()) {
                list.add(dependency);
            }
        }
        if (c.isAnnotationPresent(MavenDependency.class)) {
            list.add(c.getAnnotation(MavenDependency.class));
        }
        return list;
    }

    /**
     * 获取注释maven仓库列表
     *
     * @param c
     * @return
     */
    public static List<MavenDepository> getAnnotationMavenDepositoryList(Class<?> c) {
        List<MavenDepository> list = new ArrayList<>();
        //获取注释模块引用列表
        if (c.isAnnotationPresent(MavenDepositorys.class)) {
            for (MavenDepository dependency : c.getAnnotation(MavenDepositorys.class).value()) {
                list.add(dependency);
            }
        }
        if (c.isAnnotationPresent(MavenDepository.class)) {
            list.add(c.getAnnotation(MavenDepository.class));
        }
        return list;
    }

    /**
     * 获取注释应用maven依赖列表
     *
     * @param c
     * @return
     */
    public static List<ApplicationDependency> getAnnotationMavenApplicationDependencysList(Class<?> c) {
        List<ApplicationDependency> list = new ArrayList<>();
        //获取注释模块引用列表
        if (c.isAnnotationPresent(ApplicationDependencys.class)) {
            for (ApplicationDependency dependency : c.getAnnotation(ApplicationDependencys.class).value()) {
                list.add(dependency);
            }
        }
        if (c.isAnnotationPresent(ApplicationDependency.class)) {
            list.add(c.getAnnotation(ApplicationDependency.class));
        }
        return list;
    }

    /**
     * @param c
     * @return
     */
    public static List<MavenModuleDependency> getAnnotationModuleMavenDependencyList(Class<?> c) {
        List<MavenModuleDependency> list = new ArrayList<>();
        //获取启动类注释的模块依赖包列表
        if (c.isAnnotationPresent(MavenModuleDependencys.class)) {
            for (MavenModuleDependency dependency : c.getAnnotation(MavenModuleDependencys.class).value()) {
                list.add(dependency);
            }
        }
        if (c.isAnnotationPresent(MavenModuleDependency.class)) {
            list.add(c.getAnnotation(MavenModuleDependency.class));
        }
        return list;
    }

//    public static List<PluginDependency> getAnnotationMavenPluginDependencyList(Class<?> c) {
//        List<PluginDependency> list = new ArrayList<>();
//        //获取启动类注释的模块依赖包列表
//        if (c.isAnnotationPresent(PluginDependencys.class)) {
//            for (PluginDependency dependency : c.getAnnotation(PluginDependencys.class).value()) {
//                list.add(dependency);
//            }
//        }
//        if (c.isAnnotationPresent(PluginDependency.class)) {
//            list.add(c.getAnnotation(PluginDependency.class));
//        }
//        return list;
//    }

    /**
     * 获取指定模块插件列表
     *
     * @param module 指定要获取插件的模块
     * @param c
     * @return
     */
    public static Map<FileArtifact, List<FileArtifact>> getAnnotationMavenPluginDependencyList(FileArtifact module, Class<?> c) {
        Map<FileArtifact, List<FileArtifact>> map = new LinkedHashMap<>();
        for (MavenModuleDependency dependency : getAnnotationModuleMavenDependencyList(c)) {
            if (module.getGroupId().equals(dependency.groupId()) &&
                    module.getArtifactId().equals(dependency.artifactId()) &&
                    module.getVersion().equals(dependency.version())) {
                for (PluginDependency pluginDependency : dependency.plugin()) {
                    map.put(new FileArtifact(pluginDependency.groupId(), pluginDependency.artifactId(), pluginDependency.version()), new ArrayList<>());
                }
                break;
            }
        }
        return map;
    }

    /**
     * 获取类型注释的配置列表
     * @param c
     * @return
     */
    public static List<ConfigurationProperties> getAnnotationConfigurationPropertiesList(Class<?> c) {
        List<ConfigurationProperties> list = new ArrayList<>();
        //获取启动类注释的模块依赖包列表
        if (c.isAnnotationPresent(ConfigurationPropertiess.class)) {
            for (ConfigurationProperties dependency : c.getAnnotation(ConfigurationPropertiess.class).value()) {
                list.add(dependency);
            }
        }
        if (c.isAnnotationPresent(ConfigurationProperties.class)) {
            list.add(c.getAnnotation(ConfigurationProperties.class));
        }
        getOrderList(list);
        return list;
    }
    /**
     * 排列对象从小开始到大开始位置列表。
     *
     * @param list
     * @return
     */
    public static List<ConfigurationProperties> getOrderList(ConfigurationProperties[] list) {
        List<ConfigurationProperties> minList = new ArrayList();
        for (ConfigurationProperties properties : list) {
            minList.add(properties);
        }
        getOrderList(minList);
        return minList;
    }

    /**
     * 排列对象从小开始到大开始位置列表。
     *
     * @param list
     */
    public static void getOrderList(List<ConfigurationProperties> list) {
        list.sort(new Comparator<ConfigurationProperties>() {
            @Override
            public int compare(ConfigurationProperties a, ConfigurationProperties b) {
                return a.order() - b.order();
            }
        });
    }
}