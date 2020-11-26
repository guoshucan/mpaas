package ghost.framework.core.bean.factory.converter;

import ghost.framework.beans.annotation.bean.factory.ClassAnnotationBeanFactory;
import ghost.framework.beans.annotation.converter.ConverterFactory;
import ghost.framework.beans.annotation.tags.AnnotationTag;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.AbstractClassAnnotationBeanFactory;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.context.bean.factory.converter.IClassConverterFactoryAnnotationBeanFactory;
import ghost.framework.context.converter.TypeConverter;
import ghost.framework.context.converter.ConverterContainer;

import java.lang.annotation.Annotation;
/**
 * package: ghost.framework.core.bean.factory.converter
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description: {@link TypeConverter <?, ?>} 绑定工厂
 * @Date: 2020/3/1:11:08
 */
@ClassAnnotationBeanFactory(tag = AnnotationTag.AnnotationTags.Container)
public class ClassConverterFactoryAnnotationBeanFactory
        <
        O extends ICoreInterface,
        T extends Class<?>,
        E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
        V extends TypeConverter<?, ?>
        >
        extends AbstractClassAnnotationBeanFactory<O, T, E, V>
        implements IClassConverterFactoryAnnotationBeanFactory<O, T, E, V> {

    @Override
    public String toString() {
        return "ClassConverterFactoryAnnotationBeanFactory{" +
                "annotation=" + annotation.toString() +
                '}';
    }

    /**
     * 事件工厂注释
     */
    private final Class<? extends Annotation> annotation = ConverterFactory.class;

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
     * 加载事件
     *
     * @param event 事件对象
     */
    @Override
    public void loader(E event) {
        ConverterContainer container = event.getExecuteOwner().getBean(ConverterContainer.class);
        synchronized (container.getSyncRoot()) {
            container.add(event.getValue());
        }
    }

    /**
     * 卸载事件
     *
     * @param event 事件对象
     */
    @Override
    public void unloader(E event) {

    }
}