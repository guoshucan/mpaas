package ghost.framework.context.bean;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:代理绑定定义接口
 * @Date: 0:48 2020/1/11
 */
public interface IProxyBeanDefinition extends IBeanDefinition {
    /**
     * 获取代理对象
     *
     * @return
     */
    Object getProxyObject();
}