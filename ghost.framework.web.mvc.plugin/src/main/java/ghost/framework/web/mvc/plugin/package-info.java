/**
 * package: ghost.framework.web.mvc.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/5/27:12:09
 */
@PluginPackage(loadClass = {
        ViewResolverContainer.class,
        ViewResolverInterfaceBeanFactory.class,
        ClassControllerAnnotationBeanFactory.class,
        RequestMethodArgumentModelAttributeAnnotationResolver.class,
        RequestMethodArgumentModelClassResolver.class,
        RequestMethodViewReturnValueResolver.class
})
package ghost.framework.web.mvc.plugin;

import ghost.framework.beans.plugin.bean.annotation.PluginPackage;
import ghost.framework.web.mvc.plugin.bean.factory.ClassControllerAnnotationBeanFactory;
import ghost.framework.web.mvc.plugin.bean.factory.ViewResolverInterfaceBeanFactory;
import ghost.framework.web.mvc.plugin.http.request.method.argument.RequestMethodArgumentModelAttributeAnnotationResolver;
import ghost.framework.web.mvc.plugin.http.request.method.argument.RequestMethodArgumentModelClassResolver;
import ghost.framework.web.mvc.plugin.http.request.method.returnValue.RequestMethodViewReturnValueResolver;
import ghost.framework.web.mvc.plugin.servlet.view.ViewResolverContainer;