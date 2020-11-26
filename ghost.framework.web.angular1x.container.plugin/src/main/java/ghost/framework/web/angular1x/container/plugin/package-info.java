/**
 * package: ghost.framework.web.container.plugin
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:存放前端模块容器内容包
 * @Date: 2020/3/12:20:27
 */
@PluginPackage(loadClass = {
        WebRouterContainer.class,
        WebMenuRouterContainer.class,
        WebHeaderMenuContainer.class,
        ClassJarResourceDependencyAnnotationBeanFactory.class,
        ClassWebHeaderMenuAnnotationBeanFactory.class,
        ClassWebHeaderMenusAnnotationBeanFactory.class,
        ClassWebNavMenuAnnotationBeanFactory.class,
        ClassWebNavMenusAnnotationBeanFactory.class,
        ClassWebRouterAnnotationBeanFactory.class,
        ClassWebRoutersAnnotationBeanFactory.class,
        HtmlContainerRestController.class})
//注释包资源路径
@Resource(value = {"static"})
//注册包i18n国际化语言
//@I18n
//@WebI18nContainer(localeNames = {"zh-CN","en","it-IT","de-DE"}, localeTitles = {"简体中文","English", "Italy", "Germany"}, value = "i18ncontainer")
package ghost.framework.web.angular1x.container.plugin;

import ghost.framework.beans.plugin.bean.annotation.PluginPackage;
import ghost.framework.beans.resource.annotation.Resource;
import ghost.framework.web.angular1x.container.plugin.bean.*;
import ghost.framework.web.angular1x.context.menu.WebHeaderMenuContainer;
import ghost.framework.web.angular1x.context.router.WebMenuRouterContainer;
import ghost.framework.web.angular1x.context.router.WebRouterContainer;
