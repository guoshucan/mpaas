/**
 * package: ghost.framework.data.hibernate.plugin
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/23:19:16
 */
@PluginPackage(loadClass = {

        HibernateGlobalEmptyInterceptor.class,
        HibernateBuilder.class,
        ClassEntityAnnotationEventFactory.class})
package ghost.framework.data.hibernate.plugin;

import ghost.framework.beans.plugin.bean.annotation.PluginPackage;
import ghost.framework.data.hibernate.HibernateGlobalEmptyInterceptor;
import ghost.framework.data.hibernate.plugin.event.annotation.ClassEntityAnnotationEventFactory;