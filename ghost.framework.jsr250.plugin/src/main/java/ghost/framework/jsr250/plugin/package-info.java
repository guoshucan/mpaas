/**
 * package: ghost.framework.jsr250.plugin
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:java jsr-250标准注释处理插件包
 * @Date: 2020/2/21:15:29
 */
@PluginPackage(
        //插件名称
//        name = "java-jsr-250-plugin",
        //插件依赖类型
        loadClass = {
        JSR250FieldResourceAnnotationInjectionFactory.class,
        JSR250ParameterResourceAnnotationInjectionFactory.class,
        JSR250MethodPostConstructAnnotationBeanFactory.class,
        JSR250MethodPreDestroyAnnotationBeanFactory.class
})
package ghost.framework.jsr250.plugin;
import ghost.framework.beans.plugin.bean.annotation.PluginPackage;