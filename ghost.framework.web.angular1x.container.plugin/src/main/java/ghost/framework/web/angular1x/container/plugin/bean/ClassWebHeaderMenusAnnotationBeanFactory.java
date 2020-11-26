package ghost.framework.web.angular1x.container.plugin.bean;

import ghost.framework.beans.annotation.bean.factory.ClassAnnotationBeanFactory;
import ghost.framework.beans.annotation.tags.AnnotationTag;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.web.angular1x.context.menu.IWebHeaderMenuContainer;
import ghost.framework.web.angular1x.context.menu.WebHeaderMenuItem;
import ghost.framework.web.angular1x.context.menu.annotation.WebHeaderMenu;
import ghost.framework.web.angular1x.context.menu.annotation.WebHeaderMenus;
import ghost.framework.web.context.bens.factory.AbstractWebClassAnnotationBeanFactory;
import ghost.framework.web.context.bens.factory.IWebClassAnnotationBeanFactory;

import java.lang.annotation.Annotation;

/**
 * package: ghost.framework.web.angular1x.container.plugin.bean
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:上边菜单注释事件工厂
 * @Date: 2020/3/15:15:00
 */
@ClassAnnotationBeanFactory(tag = AnnotationTag.AnnotationTags.Container)
public class ClassWebHeaderMenusAnnotationBeanFactory
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
    private final Class<? extends Annotation> annotation = WebHeaderMenus.class;

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
        WebHeaderMenus headerMenus = this.getAnnotation(event);
        //获取web顶部菜单容器接口
        IWebHeaderMenuContainer headerMenuContainer = this.getWebModule().getBean(IWebHeaderMenuContainer.class);
        //注册资源路由
        for (WebHeaderMenu headerMenu : headerMenus.value()) {
            //注册头部菜单
            WebHeaderMenuItem item = new WebHeaderMenuItem(event.getValue(), headerMenu);
            headerMenuContainer.add(item);
            if (getLog().isDebugEnabled()) {
                getLog().debug("add WebHeaderMenu:" + item.getDomain().toString() + "->position:" + item.getPosition() + ">template:" + item.getTemplateUrl());
            }
        }
    }

    @Override
    public void unloader(E event) {

    }
}