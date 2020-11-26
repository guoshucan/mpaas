package ghost.framework.core.event.proxy.cglib.factory.environment;

import ghost.framework.beans.configuration.environment.annotation.EnvironmentExclude;
import ghost.framework.beans.configuration.environment.bind.annotation.BindEnvironmentProperties;
import ghost.framework.beans.configuration.environment.bind.annotation.BindEnvironmentValue;
import ghost.framework.core.event.proxy.cglib.factory.AbstractCglibProxyEventFactory;
import ghost.framework.core.event.proxy.cglib.factory.ICglibProxyEventTargetHandle;
import ghost.framework.core.event.proxy.cglib.factory.ICglibProxyExceptionEventTargetHandle;
import ghost.framework.util.ReflectUtil;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.util.Locale;

/**
 * package: ghost.framework.core.event.proxy.cglib.factory.environment
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2019/12/31:22:47
 */
public abstract class AbstractBindEnvironmentCglibProxyEventFactory<
        O extends Object,
        T extends Object,
        C extends Class<?>,
        E extends ICglibProxyEventTargetHandle<O, T, R>,
        EE extends ICglibProxyExceptionEventTargetHandle<O, T, R>,
        R extends Object>
        extends AbstractCglibProxyEventFactory<O, T, C, E, EE, R>
        implements IAbstractBindEnvironmentCglibProxyEventFactory<O, T, C, R> {
    protected Logger log = Logger.getLogger(this.getClass());

    /**
     * 绑定后调用初始化函数
     */
    private void init() {
//        this.onInit();
    }

    /**
     * 初始化函数
     */
//    protected abstract void onInit();

    @Override
    public R create(C c) throws Exception {
        R r = super.create(c);

        return r;
    }

    @Override
    public R create(C c, Object[] arguments) throws Exception {
        R r = super.create(c, arguments);

        return r;
    }

    /**
     * 调用函数前事件
     *
     * @param event 代理事件
     * @throws Exception
     */
    @Override
    protected void before(E event) throws Exception {

    }

    /**
     * 调用函数后事件
     *
     * @param event 代理事件
     * @throws Exception
     */
    @Override
    protected void after(E event) throws Exception {
        //获取绑定env对象类型
        Class<?> c = event.getTarget().getClass();
        //获取调用函数名称
        String name = event.getMethod().getName().toLowerCase(Locale.ENGLISH).substring(3);
        //获取函数对象的属性
        Field field = ReflectUtil.findField(c, name);
        if (field == null) {
            throw new IllegalArgumentException(name);
        }
        //获取绑定注释
        BindEnvironmentProperties environmentProperties = c.getAnnotation(BindEnvironmentProperties.class);
        //验证排除属性
        for (String e : environmentProperties.exclude()) {
            if (e.equals(name)) {
                return;
            }
        }
        //判断是否有排除注释
        if(c.isAnnotationPresent(EnvironmentExclude.class)){
            //验证排除属性
            for (String e : c.getAnnotation(EnvironmentExclude.class).value()) {
                if (e.equals(name)) {
                    return;
                }
            }
        }
        //获取属性是否有注释绑定env值字段
        String updateName = environmentProperties.prefix() + "." + name;
        if (field.isAnnotationPresent(BindEnvironmentValue.class)) {
            //指定绑定更新字段
            updateName = environmentProperties.prefix() + "." + field.getAnnotation(BindEnvironmentValue.class).value();
        }
        //判断属性是否未基本类型字段
        if (field.getType().isPrimitive()) {
            //更新env
            this.getEnvironment().setString(updateName, field.get(event.getTarget()).toString());
        }
    }

    /**
     * 调用函数错误事件
     *
     * @param exceptionEvent 代理事件
     * @throws Exception
     */
    @Override
    protected void exception(EE exceptionEvent) throws Exception {
        throw new IllegalArgumentException(exceptionEvent.getMethod().getName() + ":" + exceptionEvent.getException().getMessage());
    }
}