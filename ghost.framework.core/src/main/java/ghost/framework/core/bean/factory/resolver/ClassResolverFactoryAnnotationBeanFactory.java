package ghost.framework.core.bean.factory.resolver;

import ghost.framework.beans.annotation.bean.factory.ClassAnnotationBeanFactory;
import ghost.framework.beans.annotation.factory.ResolverFactory;
import ghost.framework.beans.annotation.tags.AnnotationTag;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.AbstractClassAnnotationBeanFactory;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.context.bean.factory.resolver.IClassResolverFactoryAnnotationBeanFactory;
import ghost.framework.context.resolver.IResolverFactory;
import ghost.framework.context.resolver.IResolverFactoryContainer;

import java.lang.annotation.Annotation;

/**
 * package: ghost.framework.core.bean.factory.resolver
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:类型解析器工厂注释
 * @Date: 2020/3/21:17:15
 */
@ClassAnnotationBeanFactory(tag = AnnotationTag.AnnotationTags.Container)
public class ClassResolverFactoryAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V extends IResolverFactory
                >
        extends AbstractClassAnnotationBeanFactory<O, T, E, V>
        implements IClassResolverFactoryAnnotationBeanFactory<O, T, E, V> {

    /**
     * 事件工厂注释
     */
    private final Class<? extends Annotation> annotation = ResolverFactory.class;

    /**
     * 获取绑定注释类型
     *
     * @return
     */
    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return annotation;
    }

    @Override
    public String toString() {
        return "ClassResolverFactoryAnnotationBeanFactory{" +
                "annotation=" + annotation +
                '}';
    }

    /**
     * 加载事件
     *
     * @param event 事件对象
     */
    @Override
    public void loader(E event) {
        event.getExecuteOwner().getBean(IResolverFactoryContainer.class).add(event.getValue());
    }

    /**
     * 卸载事件
     *
     * @param event 事件对象
     */
    @Override
    public void unloader(E event) {
        event.getExecuteOwner().getBean(IResolverFactoryContainer.class).remove(event.getValue());
    }
}