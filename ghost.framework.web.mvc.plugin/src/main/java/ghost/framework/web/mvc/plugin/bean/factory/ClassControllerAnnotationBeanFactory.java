package ghost.framework.web.mvc.plugin.bean.factory;

import ghost.framework.beans.annotation.bean.factory.ClassAnnotationBeanFactory;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.util.StringUtils;
import ghost.framework.web.context.bens.factory.AbstractWebClassAnnotationBeanFactory;
import ghost.framework.web.context.http.IHttpControllerContainer;
import ghost.framework.web.mvc.context.bean.factory.IClassControllerAnnotationBeanFactory;
import ghost.framework.web.mvc.context.bind.annotation.Controller;

import java.lang.annotation.Annotation;
/**
 * package: ghost.framework.web.mvc.plugin.bean.factory
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:默认处理 {@link Controller} 注释事件工厂类
 * @Date: 13:21 2020/1/31
 */
@ClassAnnotationBeanFactory(single = true)
public class ClassControllerAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V extends Object
                >
        extends AbstractWebClassAnnotationBeanFactory<O, T, E, V>
        implements IClassControllerAnnotationBeanFactory<O, T, E, V> {

    @Override
    public String toString() {
        return "ClassControllerAnnotationBeanFactory{" +
                "annotation=" + annotation.toString() +
                '}';
    }
    /**
     * 注释类型
     */
    private final Class<? extends Annotation> annotation = Controller.class;

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
        //判断是否注册http控制器注释类型，如果未注册控制器类型时注册
        this.getWebModule().getBean(IHttpControllerContainer.class).addIfAbsent(annotation);
        //获取注入注释对象
        Controller controller = this.getAnnotation(event);
        //构建类型实例
        //判断是否使用代理创建控制器实例
        if (controller.proxy()) {
            this.newCglibInstance(event);
        } else {
            this.newInstance(event);
        }
        //判断服务注释是否指定名称
        if (controller.name().equals("")) {
            //未指定绑定服务名称
        } else {
            //指定绑定的服务名称
            event.setName(controller.name());
        }
        //绑定入容器中，不管代码来自哪个模块都将往当前web模块Bean
        if (StringUtils.isEmpty(event.getName())) {
            this.getWebModule().addBean(event.getValue());
            if (this.getLog().isDebugEnabled()) {
                this.getLog().debug("add Bean RestController>class:" + event.getTarget().getName());
            }
        } else {
            this.getWebModule().addBean(event.getName(), event.getValue());
            if (this.getLog().isDebugEnabled()) {
                this.getLog().debug("add Bean RestController>name:" + event.getName() + ">class:" + event.getTarget().getName());
            }
        }
    }
}