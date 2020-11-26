package ghost.framework.web.module.bean.factory;

import ghost.framework.beans.annotation.bean.factory.ClassAnnotationBeanFactory;
import ghost.framework.beans.annotation.tags.AnnotationTag;
import ghost.framework.beans.resource.annotation.Resource;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.context.io.IResourceDomain;
import ghost.framework.context.io.ResourceDomain;
import ghost.framework.web.context.bens.factory.AbstractWebClassAnnotationBeanFactory;
import ghost.framework.web.context.bens.factory.IClassWebResourceAnnotationBeanFactory;
import ghost.framework.web.context.io.WebIResourceLoader;

import java.lang.annotation.Annotation;

/**
 * package: ghost.framework.web.module.event.annotation.resource
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块或插件资源注释绑定工厂
 * @Date: 2020/3/13:18:31
 */
@ClassAnnotationBeanFactory(tag = AnnotationTag.AnnotationTags.Container)
public final class ClassWebResourceAnnotationBeanFactory
        <
        O extends ICoreInterface,
        T extends Class<?>,
        E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
        V extends Object
        >
        extends AbstractWebClassAnnotationBeanFactory<O, T, E, V>
        implements IClassWebResourceAnnotationBeanFactory<O, T, E, V> {

    /**
     * 注释类型
     */
    private final Class<? extends Annotation> annotation = Resource.class;

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
        //获取注入注释对象
        Resource resource = this.getAnnotation(event);
        //获取web资源加载器接口
        WebIResourceLoader<IResourceDomain> resourceLoader = this.getWebModule().getBean(WebIResourceLoader.class);
        //遍历注册路径
        for (String path : resource.value()) {
            //添加web资源加载
            resourceLoader.add(new ResourceDomain(event.getValue(), this.annotation, event.getTarget().getProtectionDomain().getCodeSource().getLocation(), path, null));
            if (getLog().isDebugEnabled()) {
                getLog().debug("put WebResourcePath:" + path);
            }
        }
    }

    @Override
    public void unloader(E event) {

    }
}