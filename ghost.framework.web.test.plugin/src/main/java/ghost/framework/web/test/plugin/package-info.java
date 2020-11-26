/**
 * package: plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:web测试插件包
 * @Date: 2020/2/3:21:34
 */
@PluginPackage(
        loadClass = {
                DownloadFileRestController.class,
                UploadFileRestController.class,
                ImageRestController.class,
                UploadImageRestController.class,
                GetRestController.class,
                PostRestController.class,
                ExceptionRestController.class,
                DateRestController.class,
                PathVariableRestController.class,
                CookieRestController.class,
                RestControllerHandler.class,
                PostFormRestController.class,
                LocaleMapParamRestController.class
        })
//@I18n
//@L10n
package ghost.framework.web.test.plugin;
import ghost.framework.beans.plugin.bean.annotation.PluginPackage;
import ghost.framework.web.test.plugin.controller.*;
