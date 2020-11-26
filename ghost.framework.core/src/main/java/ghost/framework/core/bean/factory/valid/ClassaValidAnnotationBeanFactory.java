package ghost.framework.core.bean.factory.valid;

import ghost.framework.beans.annotation.bean.factory.ClassAnnotationBeanFactory;
import ghost.framework.beans.annotation.tags.AnnotationTag;
import ghost.framework.beans.annotation.valid.AnnotationValidFactory;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.AbstractClassAnnotationBeanFactory;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.context.bean.factory.valid.IClassaValidAnnotationBeanFactory;
import ghost.framework.context.valid.IAnnotationValidFactoryContainer;
import ghost.framework.context.valid.AnnotationValidBeanFactory;

import java.lang.annotation.Annotation;

/**
 * package: ghost.framework.core.bean.factory.valid
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/29:14:09
 */
@ClassAnnotationBeanFactory(single = true, tag = AnnotationTag.AnnotationTags.Invoke)
public class ClassaValidAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V extends AnnotationValidBeanFactory
                >
        extends AbstractClassAnnotationBeanFactory<O, T, E, V>
        implements IClassaValidAnnotationBeanFactory<O, T, E, V> {
    @Override
    public String toString() {
        return "ClassaValidAnnotationBeanFactory{" +
                "annotation=" + annotation +
                '}';
    }

    /**
     * 事件工厂注释
     */
    private final Class<? extends Annotation> annotation = AnnotationValidFactory.class;

    /**
     * 获取绑定注释类型
     *
     * @return
     */
    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return annotation;
    }

    /**
     * 添加绑定后事件
     *
     * @param event 事件对象
     */
    @Override
    public void loader(E event) {
        event.getExecuteOwner().getBean(IAnnotationValidFactoryContainer.class).add(event.getValue());
    }

    /**
     * 删除绑定后事件
     *
     * @param event 事件对象
     */
    @Override
    public void unloader(E event) {

    }
}