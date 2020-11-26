package ghost.framework.web.context.servlet;

import java.util.EventListener;

/**
 * package: ghost.framework.web.context.servlet
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:ServletContext监听容器基础接口
 * 主要实现删除监听类型函数支持
 * @Date: 2020/5/3:20:09
 */
public interface IServletContextListenerContainerBase {
    /**
     * 删除监听
     *
     * @param listenerClass 监听类型
     * @return
     */
    boolean remove(Class<? extends EventListener> listenerClass);

    /**
     * 删除监听
     *
     * @param className 监听类型名称
     * @return
     */
    boolean remove(String className);
}