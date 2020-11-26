package ghost.framework.core.bean.factory;

import ghost.framework.beans.annotation.ParentPriority;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassBeanTargetHandle;
import ghost.framework.context.maven.ExecuteOwnerBeanTargetHandle;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:类型绑定目标处理类
 * @Date: 13:10 2020/1/10
 * @param <O> 发起方类型
 * @param <T> 处理目标类型
 * @param <V> 处理返回值类型
 */
public class ClassBeanTargetHandle
        <O extends ICoreInterface, T extends Class<?>, V extends Object, S extends String, P extends Object>
        extends ExecuteOwnerBeanTargetHandle<O, T>
        implements IClassBeanTargetHandle<O, T, V, S, P> {
    /**
     * 初始化注释绑定事件目标处理类
     *
     * @param owner  发起方对象
     * @param target 处理目标对象
     */
    public ClassBeanTargetHandle(O owner, T target) {
        super(owner, target);
    }

    /**
     * 初始化注释绑定事件目标处理类
     *
     * @param owner  发起方对象
     * @param target 处理目标对象
     * @param name   绑定名称
     */
    public ClassBeanTargetHandle(O owner, T target, S name) {
        super(owner, target);
        this.name = name;
    }

    /**
     * 初始化注释绑定事件目标处理类
     *
     * @param owner  发起方对象
     * @param target 处理目标对象
     * @param name   绑定名称
     * @param value  值
     */
    public ClassBeanTargetHandle(O owner, T target, S name, V value) {
        super(owner, target);
        this.value = value;
        this.parameters = parameters;
    }

    /**
     * 初始化注释绑定事件目标处理类
     *
     * @param owner      发起方对象
     * @param target     处理目标对象
     * @param name       绑定名称
     * @param parameters 构建参数
     */
    public ClassBeanTargetHandle(O owner, T target, S name, P[] parameters) {
        super(owner, target);
        this.name = name;
        this.parameters = parameters;
    }

    /**
     * 初始化注释绑定事件目标处理类
     *
     * @param owner      发起方对象
     * @param target     处理目标对象
     * @param name       绑定名称
     * @param parameters 构建参数
     */
    public ClassBeanTargetHandle(O owner, T target, S name, V value, P[] parameters) {
        super(owner, target);
        this.name = name;
        this.value = value;
        this.parameters = parameters;
    }

    private S name;

    @Override
    public S getName() {
        return name;
    }

    @Override
    public void setName(S name) {
        this.name = name;
    }

    @Override
    public P[] getParameters() {
        return parameters;
    }

    @Override
    public void setParameters(P[] parameters) {
        this.parameters = parameters;
    }

    private P[] parameters;
    private V value;

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public ParentPriority getParentPriority() {
        return null;
    }
}