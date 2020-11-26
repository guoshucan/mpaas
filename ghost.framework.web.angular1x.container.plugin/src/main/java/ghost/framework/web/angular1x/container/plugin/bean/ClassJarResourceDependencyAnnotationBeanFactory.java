package ghost.framework.web.angular1x.container.plugin.bean;

import ghost.framework.beans.annotation.bean.factory.ClassAnnotationBeanFactory;
import ghost.framework.beans.annotation.tags.AnnotationTag;
import ghost.framework.beans.resource.annotation.JarResourceDependency;
import ghost.framework.beans.resource.annotation.ResourceDependency;
import ghost.framework.context.assembly.IClassLoader;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.context.io.IResourceDomain;
import ghost.framework.context.io.ResourceDomain;
import ghost.framework.web.context.bens.factory.AbstractWebClassAnnotationBeanFactory;
import ghost.framework.web.context.bens.factory.IWebClassAnnotationBeanFactory;
import ghost.framework.web.context.io.WebIResourceLoader;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;

import java.lang.annotation.Annotation;
import java.net.URL;

/**
 * package: ghost.framework.web.angular1x.container.plugin.bean
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块或插件资源注释绑定工厂
 * {@link JarResourceDependency} 注释绑定工厂
 * @Date: 2020/3/13:18:31
 */
@ClassAnnotationBeanFactory(tag = AnnotationTag.AnnotationTags.Container)
public final class ClassJarResourceDependencyAnnotationBeanFactory
        <
        O extends ICoreInterface,
        T extends Class<?>,
        E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
        V extends Object
        >
        extends AbstractWebClassAnnotationBeanFactory<O, T, E, V>
        implements IWebClassAnnotationBeanFactory<O, T, E, V> {
    /**
     * 注释类型
     */
    private final Class<? extends Annotation> annotation = JarResourceDependency.class;

    /**
     * 重写获取注释类型
     *
     * @return
     */
    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return annotation;
    }

    /**
     * 加载
     *
     * @param event 事件对象
     */
    @Override
    public void loader(E event) {
        URL url;
        //获取当前web模块类加载器
        IClassLoader classLoader = this.getWebModule().getClassLoader();
        //获取注入注释对象
        JarResourceDependency resource = this.getAnnotation(event);
        //获取web资源加载器接口
        WebIResourceLoader<IResourceDomain> resourceLoader = this.getWebModule().getBean(WebIResourceLoader.class);
        //遍历注册路径
        for (ResourceDependency dependency : resource.value()) {
            //获取jar包本地路径
            String[] paths = new String[dependency.classPaths().length];
            int i = 0;
            for (String path : dependency.classPaths()) {
                paths[i] = path;
                i++;
            }
            //添加jar加载
            //初始化包信息
            Artifact artifact = new DefaultArtifact(dependency.groupId(), dependency.artifactId(), null, dependency.version());
            //存在此资源包
            url = classLoader.getURL(artifact);
            //
            if (url == null) {
                //不存在此资源包
                //从应用类型加载器判断获取
            } else {
                resourceLoader.add(new ResourceDomain(event.getValue(), ResourceDependency.class, url, paths, null));
            }
//            resourceLoader.put(new ResourceKey(event.getValue(), path.value()), new ResourceDomain(event.getValue(), event.getTarget().getProtectionDomain().getCodeSource().getLocation(), path.value()));
            if (getLog().isDebugEnabled()) {
                getLog().debug("put JarResourceDependency:" + dependency.toString());
            }
        }
    }

    @Override
    public void unloader(E event) {

    }
}