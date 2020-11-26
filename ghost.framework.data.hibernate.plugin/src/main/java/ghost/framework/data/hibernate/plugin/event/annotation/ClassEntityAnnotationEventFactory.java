package ghost.framework.data.hibernate.plugin.event.annotation;

import ghost.framework.beans.annotation.bean.factory.ClassAnnotationBeanFactory;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.tags.AnnotationTag;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassAnnotationBeanFactory;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.data.hibernate.IHibernateBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.Entity;
import java.lang.annotation.Annotation;
/**
 * package: ghost.framework.data.hibernate.plugin.event.classs
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/27:21:50
 */
@ClassAnnotationBeanFactory(tag = AnnotationTag.AnnotationTags.StereoType)
public class ClassEntityAnnotationEventFactory
        <
        O extends ICoreInterface,
        T extends Class<?>,
        E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
        V extends Object
        >
        implements IClassAnnotationBeanFactory<O, T, E, V> {
    /**
     * 初始化类型 {@link Entity} 注释事件工厂类
     *
     * @param app 应用接口
     */
    public ClassEntityAnnotationEventFactory(@Autowired IApplication app) {
        this.app = app;
    }

    private IApplication app;

    @Override
    public String toString() {
        return "ClassEntityAnnotationEventFactory{" +
                "annotation=" + annotation.toString() +
                '}';
    }

    @Override
    public IApplication getApp() {
        return app;
    }

    private Log log = LogFactory.getLog(ClassEntityAnnotationEventFactory.class);

    @Override
    public Log getLog() {
        return log;
    }

    /**
     * 注释类型
     */
    private final Class<? extends Annotation> annotation = Entity.class;

    /**
     * 重新注释类型
     *
     * @return
     */
    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return annotation;
    }

    /**
     * 注入
     */
    @Autowired
    private IHibernateBuilder hibernateBuilder;

    /**
     * @param event 事件对象
     */
    @Override
    public void loader(E event) {
        this.getLog().debug("loader>class:" + event.getTarget().getName());
        //获取注入注释对象
//        Entity entity = this.getAnnotation(event);
        //添加实体类型
        this.hibernateBuilder.getEntityList().add(event.getTarget());
    }

    /**
     * 删除绑定
     *
     * @param event 事件对象
     */
    @Override
    public void unloader(E event) {
        this.getLog().debug("loader>class:" + event.getTarget().getName());
        //获取注入注释对象
//        Entity entity = this.getAnnotation(event);
        //删除实体类型
        this.hibernateBuilder.getEntityList().remove(event.getTarget());
    }
}