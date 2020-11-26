package ghost.framework.web.socket.plugin.bean.factory;

import ghost.framework.beans.annotation.bean.factory.ClassAnnotationBeanFactory;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.tags.AnnotationTag;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.context.module.IModule;
import ghost.framework.web.socket.plugin.server.WsServerContainer;

import javax.websocket.DeploymentException;
import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpoint;
import java.lang.annotation.Annotation;

/**
 * package: ghost.framework.web.socket.plugin.bean.factory
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:类型 {@link javax.websocket.server.ServerEndpoint} 注释绑定工厂
 * @Date: 2020/5/2:16:16
 */
@ClassAnnotationBeanFactory(single = true, tag = AnnotationTag.AnnotationTags.StereoType)
public class ClassServerEndPointAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V extends Object
                >
        implements IClassServerEndPointAnnotationBeanFactory<O, T, E, V> {
    /**
     * 初始化应用注释注入基础类
     *
     * @param app 设置应用接口
     */
    public ClassServerEndPointAnnotationBeanFactory(@Autowired IApplication app) {
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
    private final Class<? extends Annotation> annotation = ServerEndpoint.class;

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
        ServerEndpoint endpoint = this.getAnnotation(event);
        //获取容器
        WsServerContainer serverContainer = (WsServerContainer) this.module.getBean(ServerContainer.class);
        try {
            //判断是否为已经添加的终结点
            if (serverContainer.containsEndpoint(event.getTarget())) {
                //构建 ServerEndpoint 注释可u下
                this.newInstance(event);
//                //将构建后的对象存放进 ServerEndpoint 注释对象的容器中
//                List<Object> container = (List) serverContainer.getServletContext().getAttribute(PojoConstants.SERVER_ENDPOINT_CONTAINER_ATTRIBUTE);
//                synchronized (container) {
//                    container.add(event.getValue());
//                }
            } else {
                //新添加的终结点类型
                serverContainer.addEndpoint(event.getTarget());
            }
        } catch (DeploymentException e) {
            e.printStackTrace();
        }
        if (this.getLog().isDebugEnabled()) {
            this.getLog().debug("add Bean ServerEndpoint>class:" + event.getTarget().getName());
        }
    }
}
