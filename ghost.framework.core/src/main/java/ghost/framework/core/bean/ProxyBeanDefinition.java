package ghost.framework.core.bean;

import ghost.framework.context.bean.IProxyBeanDefinition;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:代理绑定定义类
 * @Date: 0:48 2020/1/11
 */
public class ProxyBeanDefinition extends BeanDefinition implements IProxyBeanDefinition {
    /**
     * 初始化代理绑定定义类
     *
     * @param name   绑定名称
     * @param object 绑定对象
     * @throws IllegalArgumentException
     */
    public ProxyBeanDefinition(String name, Object object) throws IllegalArgumentException {
        super(name, object);
    }

    /**
     * 初始化代理绑定定义类
     *
     * @param object 绑定对象
     */
    public ProxyBeanDefinition(Object object) {
        super(object);
    }

    /**
     * 初始化代理绑定定义类
     *
     * @param name        绑定名称
     * @param object      绑定对象
     * @param proxyObject 绑定代理对象
     * @throws IllegalArgumentException
     */
    public ProxyBeanDefinition(String name, Object object, Object proxyObject) throws IllegalArgumentException {
        super(name, object);
        this.proxyObject = proxyObject;
    }

    /**
     * 初始化代理绑定定义类
     *
     * @param object      绑定对象
     * @param proxyObject 绑定代理对象
     */
    public ProxyBeanDefinition(Object object, Object proxyObject) {
        super(object);
        this.proxyObject = proxyObject;
    }

    /**
     * 代理对象
     */
    private Object proxyObject;

    /**
     * 获取代理对象
     *
     * @return
     */
    @Override
    public Object getProxyObject() {
        return proxyObject;
    }
}