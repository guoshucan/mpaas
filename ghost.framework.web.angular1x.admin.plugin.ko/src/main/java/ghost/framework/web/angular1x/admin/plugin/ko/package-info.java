/**
 * package: ghost.framework.web.angular1x.admin.plugin.ko
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:管理员插件包韩国语言包
 * @Date: 2020/6/10:17:52
 */
@LocalePackage(
        //注释包唯一id
        uuid = "08f1a34c-33d8-4654-bb77-37964a133837",
        //注释约束包关系
        dependentPackage = "ghost.framework.web.angular1x.admin.plugin")
@I18n(
        //注释国际化语言
        localeNames = "ko",
        //注释国家化语言标题
        localeTitles = "Korea")
//注释web资源
@Resource(value = {
        //注释web资源路径
        "static.html"
})
package ghost.framework.web.angular1x.admin.plugin.ko;

import ghost.framework.beans.annotation.locale.I18n;
import ghost.framework.beans.annotation.locale.LocalePackage;
import ghost.framework.beans.resource.annotation.Resource;