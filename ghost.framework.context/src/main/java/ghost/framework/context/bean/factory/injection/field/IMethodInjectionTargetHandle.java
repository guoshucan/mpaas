package ghost.framework.context.bean.factory.injection.field;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IAnnotationBeanTargetHandle;
import ghost.framework.context.bean.factory.IValueBeanTargetHandle;

import java.lang.reflect.Method;

/**
 * package: ghost.framework.context.bean.factory.injection.field
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/7/22:22:50
 */
public interface IMethodInjectionTargetHandle <O extends ICoreInterface, T extends Object, F extends Method, V extends Object>
        extends IValueBeanTargetHandle<O, T, V>, IAnnotationBeanTargetHandle<O, T> {
    /**
     * 获取注入声明函数
     *
     * @return
     */
    F getMethod();
}