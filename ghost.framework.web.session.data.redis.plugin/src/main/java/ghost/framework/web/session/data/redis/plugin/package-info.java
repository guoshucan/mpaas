/**
 * package: ghost.framework.web.session.data.redis.plugin
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/20:14:23
 */
@MavenDependencys(
        {
                //redis依赖二选一，io.lettuce或redis.clients
//                @MavenDependency(groupId = "io.lettuce", artifactId = "lettuce-core", version = "5.2.2.RELEASE")
//                @MavenDependency(groupId = "redis.clients", artifactId = "jedis", version = "3.2.0")
        })
@PluginPackage
package ghost.framework.web.session.data.redis.plugin;

import ghost.framework.beans.maven.annotation.MavenDependency;
import ghost.framework.beans.maven.annotation.MavenDependencys;
import ghost.framework.beans.plugin.bean.annotation.PluginPackage;