package ghost.framework.web.module.annotation.bean.factory;
import ghost.framework.beans.annotation.bean.factory.ClassAnnotationBeanFactory;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.context.exception.AnnotationClassException;

import javax.servlet.Filter;
import javax.servlet.annotation.WebFilter;
import java.lang.annotation.Annotation;
/**
 * package: ghost.framework.web.module.annotation.bean.factory
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:默认处理 {@link javax.servlet.annotation.WebFilter} 注释事件工厂类
 * @Date: 2020/1/8:20:11
 * @param <O> 发起方对象
 * @param <T> 目标类型
 * @param <E> 类型绑定事件目标处理类型
 * @param <V> 返回类型
 */
@ClassAnnotationBeanFactory(single = true)
public class ClassWebFilterAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V extends Object
                >
        implements IClassWebFilterAnnotationBeanFactory<O, T, E, V> {
    /**
     * 初始化应用注释注入基础类
     *
     * @param app 设置应用接口
     */
    public ClassWebFilterAnnotationBeanFactory(@Autowired IApplication app) {
        this.app = app;
    }

    private IApplication app;

    @Override
    public IApplication getApp() {
        return app;
    }

    /**
     * 注释类型
     */
    private final Class<? extends Annotation> annotation = WebFilter.class;

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
//        if (this.isLoader(event)) {
//            return;
//        }
        this.getLog().info("loader:" + event.toString());
        //判断注释类型是否有效继承过滤器接口
        if (!Filter.class.isAssignableFrom(event.getTarget())) {
            throw new AnnotationClassException(this.annotation, event.getTarget());
        }
        //获取注释对象
//        WebFilter filter = this.getApp().getProxyAnnotationObject(event.getTarget().getAnnotation(this.annotation));
        //定位拥有者
//        this.positionOwner(event);
        //创建类型对象
        this.newInstance(event);
        //将Servlett添加入容器
//        IServletContextInitializerMapping initializerMapping = (IServletContextInitializerMapping) event.getExecuteOwner().getBean(IServletContextInitializerContainer.class).iterator().next();
//        initializerMapping.addFilter(event.getName(), (Filter) event.getValue());
        //添加加载注释排除
//        event.getExcludeAnnotationList().add(this.annotation);
        //返回已经处理
//        event.setHandle(true);
    }
}