package ghost.framework.web.angular1x.container.plugin.bean;

import ghost.framework.beans.annotation.bean.factory.ClassAnnotationBeanFactory;
import ghost.framework.beans.annotation.tags.AnnotationTag;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.web.angular1x.context.menu.annotation.WebNavMenu;
import ghost.framework.web.angular1x.context.menu.annotation.WebNavMenus;
import ghost.framework.web.angular1x.context.router.annotation.WebRouter;
import ghost.framework.web.angular1x.context.router.*;
import ghost.framework.web.context.bens.factory.AbstractWebClassAnnotationBeanFactory;
import ghost.framework.web.context.bens.factory.IWebClassAnnotationBeanFactory;

import java.lang.annotation.Annotation;

/**
 * package: ghost.framework.web.angular1x.container.plugin.bean
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:左边菜单注释事件工厂
 * @Date: 2020/3/15:15:00
 */
@ClassAnnotationBeanFactory(tag = AnnotationTag.AnnotationTags.Container)
public class ClassWebNavMenusAnnotationBeanFactory
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
    private final Class<? extends Annotation> annotation = WebNavMenus.class;

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
        WebNavMenus navMenus = this.getAnnotation(event);
        //获取web路由容器接口
        IWebMenuRouterContainer routerContainer = this.getWebModule().getBean(IWebMenuRouterContainer.class);
        for (WebNavMenu navMenu : navMenus.value()) {
            IWebRouterNavMenuGroup group;
            //创建路由左边菜单标题
            IWebRouterNavMenuTitle title;
            //判断路由菜单组是否存在
            if (routerContainer.containsKey(navMenu.group())) {
                //路由菜单组存在获取组
                group = (IWebRouterNavMenuGroup) routerContainer.get(navMenu.group());
                if (group.containsKey(navMenu.title())) {
                    title = group.get(navMenu.title());
                } else {
                    title = new WebRouterNavMenuTitle(event.getValue(), navMenu.title(), navMenu.icon());
                    group.put(navMenu.title(), title);
                }
            } else {
                //路由菜单组不存在创建新的
                group = new WebRouterNavMenuGroup(event.getValue(), navMenu.group());
                title = new WebRouterNavMenuTitle(event.getValue(), navMenu.title(), navMenu.icon());
                group.put(navMenu.title(), title);
                routerContainer.put(navMenu.group(), group);
            }
            if (getLog().isDebugEnabled()) {
                getLog().debug("add RouterNavMenuTitle:" + title.getTitle());
            }
            //注册资源路由
            for (WebRouter router : navMenu.router()) {
                WebRouterItem item = new WebRouterItem(router);
                //判断虚拟路径
//            if (navMenu.virtualPath().equals("")) {
//                //未指定虚拟路径，使用域名称作为虚拟路径
//                item.setUrl(((IGetName)event.getValue()).getName());
//            } else {
//                item.setUrl(navMenu.virtualPath());
//            }
                item.setUrl(router.url());
                title.put(router.name(), item);
                if (getLog().isDebugEnabled()) {
                    getLog().debug("add WebRouter:" + router.scope() + "." + router.name() + ">template:" + router.templateUrl());
                }
            }
        }
    }

    @Override
    public void unloader(E event) {

    }
}