/**
 * package: ghost.framework.web.login.plugin.language
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/3/12:16:23
 */
@Plugin("ghost.framework.web.login.plugin")
@I18N(
        defaultLanguage = "",
        localeNames = {"en", "zh-CN", "zh-TW"},
        localeTitles = {"English", "简体中文", "繁體中文"})
@Language
package ghost.framework.web.login.plugin.language;
import ghost.framework.beans.annotation.locale.Language;
import ghost.framework.beans.locale.annotation.I18N;
import ghost.framework.beans.plugin.bean.annotation.Plugin;