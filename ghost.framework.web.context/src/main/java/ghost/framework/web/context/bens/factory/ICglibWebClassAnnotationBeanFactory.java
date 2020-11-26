package ghost.framework.web.context.bens.factory;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassAnnotationBeanFactoryContainer;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.web.context.http.request.HttpRequestMethodProxy;

/**
 * package: ghost.framework.web.module.event.annotation
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:web类型注释事件工厂基础接口
 * @Date: 2020/2/28:10:45
 */
public interface ICglibWebClassAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V
                >
        extends IWebClassAnnotationBeanFactory<O, T, E, V> {
    /**
     * 创建实例
     *
     * @param event 事件对象
     */
    default void newCglibInstance(E event) {
        //构建类型实例
        event.getExecuteOwner().getBean(IClassAnnotationBeanFactoryContainer.class).newCglibInstance(event, new HttpRequestMethodProxy(this.getWebModule()));
    }
}