package ghost.framework.web.angular1x.top.locale.plugin;

import ghost.framework.locale.entity.LocaleEntity;
import ghost.framework.web.angular1x.context.controller.RestControllerBase;
import ghost.framework.web.context.WebConstant;
import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.bind.annotation.RequestParam;
import ghost.framework.web.context.bind.annotation.RestController;
import ghost.framework.web.context.http.responseContent.DataResponse;

import javax.servlet.http.HttpSession;

/**
 * package: ghost.framework.web.angular1x.top.locale.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/8:12:30
 */
@RestController("/api")
public class TopLocaleRestController extends RestControllerBase {
    @RequestMapping(value = "/ba2c32af9a854703bf617a60ef03e6b7")
    public DataResponse get(@RequestParam String id, HttpSession session) {
        DataResponse dr = new DataResponse();
        try {
            //获取会话区域
            Object locale = session.getAttribute(WebConstant.HttpSession.HTTP_SESSION_LOCALE_ATTRIBUTE);
            Object adminId = session.getAttribute(WebConstant.HttpSession.HTTP_SESSION_LOCALE_ATTRIBUTE);
            if (locale != null) {
                dr.setData(this.sessionFactory.openGet(LocaleEntity.class, "", id));
            }
        } catch (Exception e) {
            this.exception(e);
            dr.setError(-1, e.getMessage());
        }
        return dr;
    }
//    /**
//     * 获取语言地图
//     * 获取语言地图选择插件或模块2选一
//     *
//     * @param pluginName 插件名称
//     * @param moduleName 模块名称
//     * @return
//     */
//    @RequestMapping("/e09b2ff4-9713-450e-a185-1b3f2ec29daa")
//    public DataResponse getLanguageMap(@RequestParam(required = false) String pluginName, @RequestParam(required = false) String moduleName) {
//        DataResponse response = new DataResponse();
//        if (StringUtils.isEmpty(pluginName) && StringUtils.isEmpty(moduleName)) {
//            response.setError(-1, "语言参数错误！");
//        }
//        List<ILanguage> languages = new ArrayList<>();
//        languages.add(new Language("zh-CN", "简体中文", true));
//        languages.add(new Language("zh-TW", "繁体中文"));
//        languages.add(new Language("en", "English"));
//        response.setData(languages);
//        return response;
//    }
}