package ghost.framework.core.event.proxy.cglib.factory;
import ghost.framework.context.bean.factory.IBeanTargetHandle;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:cglib代理事件接口
 * @Date: 23:36 2020/1/1
 * @param <O> 事件拥有者对象
 * @param <T> 事件目标对象
 * @param <R> 事件返回对象类型
 */
public interface ICglibProxyEventTargetHandle<O, T, R> extends IBeanTargetHandle<O, T> {
    /**
     * 获取返回对象
     *
     * @return
     */
    R getReturnObject();

    /**
     * 设置返回对象
     *
     * @param obj
     */
    void setReturnObject(R obj);

    Method getMethod();

    Object[] getObjects();

    MethodProxy getMethodProxy();
}