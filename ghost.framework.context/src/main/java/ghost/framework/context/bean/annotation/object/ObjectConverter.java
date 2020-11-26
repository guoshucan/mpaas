package ghost.framework.context.bean.annotation.object;

import ghost.framework.beans.annotation.constraints.ConstraintType;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.bean.factory.BeanTargetHandle;
import ghost.framework.context.converter.object.IObjectConverter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * package: ghost.framework.context.bean.annotation.object
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/20:22:58
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Component(proxy = false)//不使用代理构建
//@BeanSmartListContainer(value = IObjectConverterContainer.class)//注释安装容器
/**
 * 注释约束类型必须继承此接口，作为约束在加入 IObjectConverterContainer 容器前判断类型是否有效
 * {@link BeanTargetHandle#getTarget()}
 */
@ConstraintType(value = IObjectConverter.class)
public @interface ObjectConverter {
}