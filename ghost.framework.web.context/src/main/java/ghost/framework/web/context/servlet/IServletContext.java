package ghost.framework.web.context.servlet;

import javax.servlet.ServletContext;
import java.util.EventListener;

/**
 * package: ghost.framework.web.context.servlet
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:ServletContext扩展接口
 * 目标增加ServletContext事件监听删除函数支持
 * @Date: 2020/5/3:19:54
 */
public interface IServletContext extends ServletContext {
    /**
     * 删除监听
     *
     * @param t   删除监听事件对象
     * @param <T>
     */
    <T extends EventListener> void removeListener(T t);

    /**
     * 删除监听
     *
     * @param listenerClass 监听类型
     */
    void removeListener(Class<? extends EventListener> listenerClass);

    /**
     * 删除监听
     *
     * @param className 监听类型名称
     */
    void removeListener(String className);
}