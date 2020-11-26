package ghost.framework.context.bean.factory;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.IBeanDefinition;

import java.lang.reflect.Method;

/**
 * package: ghost.framework.core.event
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/7:16:10
 */
public interface IOperatingBeanBeanTargetHandle<O extends ICoreInterface, T extends IBeanDefinition> extends IMethodBeanTargetHandle<O, T, Method> {

}