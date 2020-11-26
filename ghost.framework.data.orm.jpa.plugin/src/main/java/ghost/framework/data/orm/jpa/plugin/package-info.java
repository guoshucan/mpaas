/**
 * package: ghost.framework.data.orm.jpa.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/7/19:10:55
 */
//注释插件包
@PluginPackage(loadClass = {LocalContainerEntityManagerFactoryBean.class})
package ghost.framework.data.orm.jpa.plugin;
import ghost.framework.beans.plugin.bean.annotation.PluginPackage;
import ghost.framework.data.orm.jpa.LocalContainerEntityManagerFactoryBean;