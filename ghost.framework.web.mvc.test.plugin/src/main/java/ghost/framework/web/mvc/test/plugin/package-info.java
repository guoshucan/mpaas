/**
 * package: ghost.framework.web.mvc.test.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/1:16:32
 */
@PluginPackage(loadClass = {
        MyConfiguration.class,
        ThymeleafObjects.class,
        ThymeleafConfig.class,
        ThymeleafTextTemplates.class
})
//注释web资源
@Resource(value = {"thymeleaf/templates", "thymeleaf/css"})
//注释到模块的资源包
@JarResourceDependency(
        value = {
                @ResourceDependency(groupId = "org.webjars", artifactId = "bootstrap", version = "3.3.7", classPaths = "META-INF.resources"),
                @ResourceDependency(groupId = "org.webjars", artifactId = "jquery", version = "3.2.1", classPaths = "META-INF.resources"),
                @ResourceDependency(groupId = "org.webjars", artifactId = "webjars-locator", version = "0.40", classPaths = "META-INF.resources")
        })
package ghost.framework.web.mvc.test.plugin;

import ghost.framework.beans.resource.annotation.JarResourceDependency;
import ghost.framework.beans.resource.annotation.Resource;
import ghost.framework.beans.resource.annotation.ResourceDependency;
import ghost.framework.beans.plugin.bean.annotation.PluginPackage;
