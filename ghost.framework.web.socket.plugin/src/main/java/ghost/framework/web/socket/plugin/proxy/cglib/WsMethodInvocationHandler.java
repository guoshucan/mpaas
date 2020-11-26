package ghost.framework.web.socket.plugin.proxy.cglib;

import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.proxy.IMethodInvocationHandler;

import java.lang.reflect.Method;

/**
 * package: ghost.framework.web.socket.plugin.proxy.cglib
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:websocket代理回调类
 * @Date: 2020/5/12:21:59
 */
@Component
public final class WsMethodInvocationHandler<T extends Object> implements IMethodInvocationHandler<T> {

    @Override
    public T getTarget() {
        return null;
    }

    @Override
    public void setTarget(T target) {

    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        return method.invoke(o, objects);
    }
}
