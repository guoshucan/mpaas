package ghost.framework.undertow.web.module.event.filter.factory;

import io.undertow.servlet.api.InstanceFactory;
import io.undertow.servlet.api.InstanceHandle;
import io.undertow.servlet.util.ImmediateInstanceHandle;

/**
 * package: ghost.framework.undertow.web.module.event.filter.factory
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:指定已经创建对象包装 {@link InstanceFactory} 接口
 * @Date: 0:31 2020/1/30
 */
public class ClassInstanceFactory<T> implements InstanceFactory<T> {
    /**
     * 包装对象
     */
    private T t;

    /**
     * 初始化指定已经创建对象包装 {@link InstanceFactory} 接口
     * @param t 包装对象
     */
    public ClassInstanceFactory(T t) {
        this.t = t;
    }

    /**
     * 获取创建对象
     * @return
     * @throws InstantiationException
     */
    @Override
    public InstanceHandle<T> createInstance() throws InstantiationException {
        return new ImmediateInstanceHandle(this.t);
    }
}
