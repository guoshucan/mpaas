package ghost.framework.web.socket.plugin.bean.factory;

import ghost.framework.beans.annotation.bean.factory.ClassAnnotationBeanFactory;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.tags.AnnotationTag;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.context.module.IModule;

import javax.websocket.ClientEndpoint;
import java.lang.annotation.Annotation;

/**
 * package: ghost.framework.web.socket.plugin.bean.factory
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:类型 {@link ClientEndpoint} 注释绑定工厂
 * @Date: 2020/5/2:16:16
 */
@ClassAnnotationBeanFactory(single = true, tag = AnnotationTag.AnnotationTags.StereoType)
public class ClassClientEndpointAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V extends Object
                >
        implements IClassClientEndpointAnnotationBeanFactory<O, T, E, V> {
    /**
     * 初始化应用注释注入基础类
     *
     * @param app 设置应用接口
     */
    public ClassClientEndpointAnnotationBeanFactory(@Autowired IApplication app) {
        this.app = app;
    }

    /**
     * 应用接口
     */
    private IApplication app;

    /**
     * 获取应用接口
     *
     * @return
     */
    @Override
    public IApplication getApp() {
        return app;
    }

    /**
     * 注释类型
     */
    private final Class<? extends Annotation> annotation = ClientEndpoint.class;

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
     * 注入模块
     */
    @Autowired
    private IModule module;

    /**
     * 加载
     *
     * @param event 事件对象
     */
    @Override
    public void loader(E event) {
        //获取注入注释对象
        ClientEndpoint endpoint = this.getAnnotation(event);
        //构建类型实例
        this.newInstance(event);
        //绑定入容器中，不管代码来自哪个模块都将往当前web模块Bean
        this.module.addBean(event.getValue());
        if (this.getLog().isDebugEnabled()) {
            this.getLog().debug("add Bean ClientEndpoint>class:" + event.getTarget().getName());
        }
    }
}
