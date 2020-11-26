package ghost.framework.core.event;

import ghost.framework.context.bean.factory.INameValueParametersBeanTargetHandle;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 23:50 2020/1/14
 */
public class NameValueParametersEventTargetHandle<O, T, S, V, P> extends NameValueEventTargetHandle<O, T, S, V> implements INameValueParametersBeanTargetHandle<O, T, S, V, P> {
    /**
     * 构建参数
     */
    private P[] parameters;

    public NameValueParametersEventTargetHandle(O owner, T target) {
        super(owner, target);
    }

    public NameValueParametersEventTargetHandle(O owner, T target, S name) {
        super(owner, target, name);
    }

    public NameValueParametersEventTargetHandle(O owner, T target, S name, V value) {
        super(owner, target, name, value);
    }

    public NameValueParametersEventTargetHandle(O owner, T target, S name, P[] parameters) {
        super(owner, target, name);
        this.parameters = parameters;
    }

    public NameValueParametersEventTargetHandle(O owner, T target, S name, V value, P[] parameters) {
        super(owner, target, name, value);
        this.parameters = parameters;
    }

    public NameValueParametersEventTargetHandle(O owner, T target, P[] parameters) {
        super(owner, target);
        this.parameters = parameters;
    }

    /**
     * 获取构建参数
     *
     * @return
     */
    @Override
    public P[] getParameters() {
        return parameters;
    }

    @Override
    public void setParameters(P[] parameters) {
        this.parameters = parameters;
    }
}