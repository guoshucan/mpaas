package ghost.framework.web.angular1x.container.plugin.bean;

import ghost.framework.beans.annotation.bean.factory.ClassAnnotationBeanFactory;
import ghost.framework.beans.annotation.tags.AnnotationTag;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.web.angular1x.context.router.annotation.WebRouter;
import ghost.framework.web.angular1x.context.router.annotation.WebRouters;
import ghost.framework.web.angular1x.context.router.IWebRouterContainer;
import ghost.framework.web.angular1x.context.router.WebRouterItem;
import ghost.framework.web.context.bens.factory.AbstractWebClassAnnotationBeanFactory;
import ghost.framework.web.context.bens.factory.IWebClassAnnotationBeanFactory;

import java.lang.annotation.Annotation;

/**
 * package: ghost.framework.web.angular1x.container.plugin.bean
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/5/22:21:14
 */
@ClassAnnotationBeanFactory(tag = AnnotationTag.AnnotationTags.Container)
public class ClassWebRoutersAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V extends Object
                >
        extends AbstractWebClassAnnotationBeanFactory<O, T, E, V>
        implements IWebClassAnnotationBeanFactory<O, T, E, V> {

    /**
     * 注释类型
     */
    private final Class<? extends Annotation> annotation = WebRouters.class;

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
        WebRouters routers = this.getAnnotation(event);
        //获取web路由容器接口
        IWebRouterContainer routerContainer = this.getWebModule().getBean(IWebRouterContainer.class);
        //遍历注释路由
        for (WebRouter router : routers.value()) {
            //注册路由
            WebRouterItem item = new WebRouterItem(router);
            //添加路由
            routerContainer.put(router.scope() + "." + router.url(), item);
            if (getLog().isDebugEnabled()) {
                getLog().debug("put WebRouter:" + item.toString());
            }
        }
    }

    @Override
    public void unloader(E event) {

    }
}