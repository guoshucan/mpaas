package ghost.framework.context.bean.factory.injection.field;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IValueBeanTargetHandle;
import ghost.framework.context.bean.factory.IAnnotationBeanTargetHandle;

import java.lang.reflect.Field;

/**
 * package: ghost.framework.core.bean.factory.injection.field
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:注入声明时间目标处理接口
 * @Date: 2020/1/7:16:59
 */
public interface IFieldInjectionTargetHandle
        <O extends ICoreInterface, T extends Object, F extends Field, V extends Object>
        extends IValueBeanTargetHandle<O, T, V>, IAnnotationBeanTargetHandle<O, T> {
    /**
     * 获取注入声明例
     * @return
     */
    F getField();
}