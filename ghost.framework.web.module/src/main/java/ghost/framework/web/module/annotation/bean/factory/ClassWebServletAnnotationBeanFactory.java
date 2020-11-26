package ghost.framework.web.module.annotation.bean.factory;
import ghost.framework.beans.annotation.bean.factory.ClassAnnotationBeanFactory;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.context.exception.AnnotationClassException;
import javax.servlet.Servlet;
import javax.servlet.annotation.WebServlet;
import java.lang.annotation.Annotation;
/**
 * package: ghost.framework.web.module.annotation.bean.factory
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/1/30:21:53
 */
@ClassAnnotationBeanFactory(single = true)
public class ClassWebServletAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V extends Object
                >
        implements IClassWebServletAnnotationBeanFactory<O, T, E, V> {
    /**
     * 初始化应用注释注入基础类
     *
     * @param app 设置应用接口
     */
    public ClassWebServletAnnotationBeanFactory(@Autowired IApplication app) {
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
    private final Class<? extends Annotation> annotation = WebServlet.class;

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
        this.getLog().info("loader:" + event.toString());
        //判断注释类型是否有效继承过滤器接口
        if (!Servlet.class.isAssignableFrom(event.getTarget())) {
            throw new AnnotationClassException(this.annotation, event.getTarget());
        }
        //获取注释对象
        WebServlet servlet = this.getAnnotation(event);
        //创建类型对象
        this.newInstance(event);
        //将Servlett添加入容器
//        IServletContextInitializerMapping initializerMapping = (IServletContextInitializerMapping) event.getExecuteOwner().getBean(IServletContextInitializerContainer.class).iterator().next();
//        initializerMapping.addServlet(event.getName(), (Servlet) event.getValue());
    }
}