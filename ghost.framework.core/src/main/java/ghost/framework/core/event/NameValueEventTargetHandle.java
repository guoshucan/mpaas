package ghost.framework.core.event;

import ghost.framework.context.bean.factory.INameValueBeanTargetHandle;
import ghost.framework.context.bean.factory.IValueBeanTargetHandle;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 23:40 2020/1/14
 */
public class NameValueEventTargetHandle<O, T, S, V> extends NameBeanTargetHandle<O, T, S> implements INameValueBeanTargetHandle<O, T, S, V>, IValueBeanTargetHandle<O, T, V> {
    public NameValueEventTargetHandle(O owner, T target) {
        super(owner, target);
    }

    public NameValueEventTargetHandle(O owner, T target, S name) {
        super(owner, target, name);
    }

    public NameValueEventTargetHandle(O owner, T target, S name, V value) {
        super(owner, target, name);
        this.value = value;
    }
    private V value;

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public void setValue(V value) {
        this.value = value;
    }
}