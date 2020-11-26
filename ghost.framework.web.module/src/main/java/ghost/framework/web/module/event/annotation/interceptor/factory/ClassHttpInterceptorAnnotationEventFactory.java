package ghost.framework.web.module.event.annotation.interceptor.factory;
import ghost.framework.beans.annotation.bean.factory.ClassAnnotationBeanFactory;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.context.exception.AnnotationClassException;
import ghost.framework.core.event.AbstractApplicationOwnerEventFactory;
import ghost.framework.web.context.bens.annotation.HttpInterceptor;
import ghost.framework.web.module.interceptors.IHttpInterceptor;
import ghost.framework.web.module.interceptors.IHttpInterceptorContainer;

import java.lang.annotation.Annotation;
/**
 * package: ghost.framework.web.module.event.annotation.interceptor
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:默认处理 {@link ghost.framework.web.context.bens.annotation.HttpInterceptor} 注释事件工厂类
 * @Date: 2020/2/1:19:35
 */
@ClassAnnotationBeanFactory(single = true)
public class ClassHttpInterceptorAnnotationEventFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V extends Object
                >
        extends AbstractApplicationOwnerEventFactory<O, T, E>
        implements IClassHttpInterceptorAnnotationEventFactory<O, T, E, V> {
    /**
     * 初始化应用注释注入基础类
     *
     * @param app 设置应用接口
     */
    public ClassHttpInterceptorAnnotationEventFactory(@Autowired IApplication app) {
        super(app);
    }

    /**
     * 注释类型
     */
    private final Class<? extends Annotation> annotation = HttpInterceptor.class;

    /**
     * 重写获取注释类型
     *
     * @return
     */
    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return annotation;
    }

    /**
     * 加载
     *
     * @param event 事件对象
     */
    @Override
    public void loader(E event) {
        //判断是否没有此注释或此注释已经在排除执行列表中
        if (this.isLoader(event)) {
            return;
        }
        this.getLog().info("loader:" + event.toString());
        //判断注释类型是否有效继承过滤器接口
        if (!IHttpInterceptor.class.isAssignableFrom(event.getTarget())) {
            throw new AnnotationClassException(this.annotation, event.getTarget());
        }
        //定位拥有者
        this.positionOwner(event);
        //创建类型对象
        this.newInstance(event);
        //将IHttpInterceptor添加入容器
        event.getExecuteOwner().getBean(IHttpInterceptorContainer.class).add(event.getValue());
        //添加加载注释排除
//        event.getExcludeAnnotationList().add(this.annotation);
        //返回已经处理
        event.setHandle(true);
    }
}