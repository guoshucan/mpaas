package ghost.framework.core.proxy.cglib;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.ICglibBeanFactory;

/**
 * package: ghost.framework.core.proxy.cglib
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/11:21:40
 */
public interface ICglibClassAnnotationEventFactory extends ICglibBeanFactory {
    /**
     * 创建代理对象
     *
     * @param c
     * @return 返回代理对象
     */
    void createObject(ICoreInterface coreInterface, Class<?> c);
}