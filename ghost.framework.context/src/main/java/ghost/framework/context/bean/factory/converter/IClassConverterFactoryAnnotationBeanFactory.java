package ghost.framework.context.bean.factory.converter;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassAnnotationBeanFactory;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.context.converter.TypeConverter;

/**
 * package: ghost.framework.context.bean.factory.converter
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/3/21:17:16
 */
public interface IClassConverterFactoryAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V extends TypeConverter<?, ?>
                >
        extends IClassAnnotationBeanFactory<O, T, E, V> {
}