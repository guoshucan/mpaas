package ghost.framework.core.event.proxy.cglib.factory;

import ghost.framework.context.base.IInstance;
import ghost.framework.context.bean.factory.IBeanFactory;
import ghost.framework.util.ReflectUtil;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.io.Serializable;
import java.lang.reflect.Method;
/**
 * package: ghost.framework.core.event.proxy.cglib.factory
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2019/12/31:22:20
 * @param <O> 事件拥有者对象
 * @param <T> 事件目标对象
 * @param <C> 事件目标类型
 * @param <R> 代理返回对象类型
 */
public interface ICglibProxyEventFactory<O, T, C extends Class<?>, R> extends MethodInterceptor, IBeanFactory {
    /**
     * 创建代理对象
     *
     * @param c 需要创建代理对象的类型
     * @return 返回代理创建对象
     * @throws Exception
     */
    R create(C c) throws Exception;

    /**
     * 指定参数创建代理对象
     *
     * @param c         需要创建代理对象的类型
     * @param arguments 代理类型构建参数对象
     * @return 返回代理创建对象
     * @throws Exception
     */
    R create(C c, Object[] arguments) throws Exception;

    /**
     * 创建代理器
     *
     * @param c 需要创建代理对象的类型
     * @return 返回代理器
     * @throws Exception
     */
    default Enhancer createEnhancer(C c) throws Exception {
        //没有构建参数创建代理对象
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(c);
        if (c instanceof Serializable) {
            enhancer.setSerialVersionUID(this.getSerialVersionUID(c));
        }
        enhancer.setClassLoader(this.getClassLoader());
        enhancer.setCallback(this);
        return enhancer;
    }

    /**
     * 获取类型序列化值
     *
     * @param c
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    default Long getSerialVersionUID(C c) throws IllegalArgumentException, IllegalAccessException {
        return (Long) ReflectUtil.findStaticFieldValue(c, "serialVersionUID");
    }

    /**
     * 重写{@link net.sf.cglib.proxy.MethodInterceptor}代理接口
     *
     * @param o           代理对象
     * @param method      代理原始函数
     * @param objects     调用代理函数数组参数
     * @param methodProxy 增强函数代理调用
     * @return 代理调用函数返回对象
     * @throws Throwable
     */
    @Override
    R intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable;

    /**
     * 获取类加载器
     * 主要是给代理器调用
     */
    ClassLoader getClassLoader();

    /**
     * 获取构建接口
     * @return
     */
    IInstance getInstance();
}