package ghost.framework.core.proxy.jvm;


import ghost.framework.core.bean.BeanDefinition;

/**
 * package: ghost.framework.core.proxy.jvm
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:jvm代理绑定定义类
 * @Date: 2019/12/31:20:15
 */
public class JvmBeanDefinition extends BeanDefinition {
    public JvmBeanDefinition(String name, Object object) throws IllegalArgumentException {
        super(name, object);
    }

    public JvmBeanDefinition(Object object) {
        super(object);
    }
}
