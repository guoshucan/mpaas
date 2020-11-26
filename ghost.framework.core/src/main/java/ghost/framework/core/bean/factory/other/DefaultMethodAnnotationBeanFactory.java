package ghost.framework.core.bean.factory.other;

import ghost.framework.beans.annotation.event.module.MethodAnnotationEventFactory;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.annotation.IAnnotationExecutionChain;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.AbstractClassAnnotationBeanFactory;
import ghost.framework.context.bean.factory.IClassAnnotationBeanFactory;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.core.annotation.AnnotationRootExecutionChain;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * package: ghost.framework.core.event.bean.operating.factory.lastOrder
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:操作绑定 {@link MethodAnnotationEventFactory} 列表容器事件工厂类
 * @Date: 2020/1/31:17:51
 */
@Component
public class DefaultMethodAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V extends Object,
                AK extends Class<? extends Annotation>,
                AV extends IAnnotationExecutionChain<AK, AV, AL>,
                AL extends List<AV>
                >
        extends AbstractClassAnnotationBeanFactory<O, T, E, V>
        implements IClassAnnotationBeanFactory<O, T, E, V> {
    /**
     * 事件工厂注释
     */
    private final Class<? extends Annotation> annotation = MethodAnnotationEventFactory.class;

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
    public void loader(E event) {
        //获取容器注释
        MethodAnnotationEventFactory annotationEventFactory = this.getAnnotation(event);
        //获取注释链接口
        AnnotationRootExecutionChain<AK, AV, AL> chainMap = this.getApp().getBean(AnnotationRootExecutionChain.class);
        //获取类型注释
        IClassAnnotationBeanFactory eventFactory = (IClassAnnotationBeanFactory) event.getValue();
        //添加注释事件工厂注释类型
        AV av = chainMap.add((AK) eventFactory.getAnnotationClass(), annotationEventFactory.single(), annotationEventFactory.tag(), event.getExecuteOwner());
    }

    @Override
    public void unloader(E event) {

    }
}