package ghost.framework.core.event.classs;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.core.event.NameValueParametersEventTargetHandle;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:类型注释事件目标处理类
 * @Date: 13:10 2020/1/10
 * @param <O> 发起方类型
 * @param <T> 处理目标类型
 * @param <V> 处理返回值类型
 */
public class ClassEventTargetHandle
        <O extends ICoreInterface, T extends Class<?>, V extends Object, S extends String, P extends Object>
        extends NameValueParametersEventTargetHandle<O, T, S, V, P>
        implements IClassEventTargetHandle<O, T, V, S, P> {

    /**
     * 初始化注释绑定事件目标处理类
     *
     * @param owner  发起方对象
     * @param target 处理目标对象
     */
    public ClassEventTargetHandle(O owner, T target) {
        super(owner, target);
    }

    /**
     * 初始化注释绑定事件目标处理类
     *
     * @param owner  发起方对象
     * @param target 处理目标对象
     * @param name   绑定名称
     */
    public ClassEventTargetHandle(O owner, T target, S name) {
        super(owner, target, name);
    }

    /**
     * 初始化注释绑定事件目标处理类
     *
     * @param owner  发起方对象
     * @param target 处理目标对象
     * @param name   绑定名称
     * @param value  值
     */
    public ClassEventTargetHandle(O owner, T target, S name, V value) {
        super(owner, target, name, value);
    }
    /**
     * 初始化注释绑定事件目标处理类
     *
     * @param owner      发起方对象
     * @param target     处理目标对象
     * @param name       绑定名称
     * @param parameters 构建参数
     */
    public ClassEventTargetHandle(O owner, T target, S name, P[] parameters) {
        super(owner, target, parameters);
        this.setName(name);
    }

    /**
     * 初始化注释绑定事件目标处理类
     *
     * @param owner      发起方对象
     * @param target     处理目标对象
     * @param name       绑定名称
     * @param parameters 构建参数
     */
    public ClassEventTargetHandle(O owner, T target, S name, V value, P[] parameters) {
        super(owner, target, name, value, parameters);
    }
}