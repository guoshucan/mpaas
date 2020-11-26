package ghost.framework.web.module.bean.factory;

import ghost.framework.beans.annotation.bean.factory.ClassAnnotationBeanFactory;
import ghost.framework.beans.annotation.tags.AnnotationTag;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.web.context.bens.factory.AbstractWebClassAnnotationBeanFactory;
import ghost.framework.web.context.bens.factory.IClassWebListenerAnnotationBeanFactory;

import javax.servlet.annotation.WebListener;
import java.lang.annotation.Annotation;

/**
 * package: ghost.framework.web.module.bean.factory
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:类型 {@link WebListener} 注释绑定工厂
 * @Date: 2020/5/2:16:34
 */
@ClassAnnotationBeanFactory(single = true, tag = AnnotationTag.AnnotationTags.StereoType)
public class ClassWebListenerAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V extends Object
                >
        extends AbstractWebClassAnnotationBeanFactory<O, T, E, V>
        implements IClassWebListenerAnnotationBeanFactory<O, T, E, V> {

    /**
     * 注释类型
     */
    private final Class<? extends Annotation> annotation = WebListener.class;

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
        //获取注入注释对象
        WebListener webListener = this.getAnnotation(event);
        //构建类型实例
        this.newInstance(event);
        //绑定入容器中，不管代码来自哪个模块都将往当前web模块Bean
        this.getWebModule().addBean(event.getValue());
        if (this.getLog().isDebugEnabled()) {
            this.getLog().debug("add Bean WebListener>class:" + event.getTarget().getName());
        }
    }
}