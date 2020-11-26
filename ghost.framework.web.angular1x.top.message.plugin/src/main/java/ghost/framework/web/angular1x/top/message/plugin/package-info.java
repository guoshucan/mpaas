/**
 * package: ghost.framework.web.angular1x.top.message.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/7:22:40
 */
@PluginPackage(loadClass = {TopMessageConfig.class, TopMessageRestController.class})
//注释web资源
@Resource(value = {
        //注释web资源路径
        "static.html"
})
package ghost.framework.web.angular1x.top.message.plugin;

import ghost.framework.beans.resource.annotation.Resource;
import ghost.framework.beans.plugin.bean.annotation.PluginPackage;
