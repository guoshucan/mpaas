package ghost.framework.web.login.plugin;

import ghost.framework.context.locale.ILanguage;
import ghost.framework.context.locale.Language;
import ghost.framework.context.utils.AssemblyUtil;
import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.bind.annotation.RequestParam;
import ghost.framework.web.context.bind.annotation.RestController;
import ghost.framework.web.context.http.responseContent.DataResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * package: ghost.framework.web.login.plugin
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:登录操作控制器
 * @Date: 2020/2/15:10:11
 */
@RestController("/api/login")
public class OpenLoginRestController {


    /**
     * 获取模块地址
     * 在登录完成后进行跳转到的模块地址路径
     *
     * @return
     */
    @RequestMapping("/ade5697f-ef65-4981-b310-e305d2bd8ab7")
    public DataResponse getInitModuleUrl() {
        DataResponse response = new DataResponse();

        return response;
    }

    /**
     * 获取语言地图
     *
     * @return
     */
    @RequestMapping("/e09b2ff4-9713-450e-a185-1b3f2ec29daa")
    public DataResponse getLanguageMap() {
        DataResponse response = new DataResponse();
        List<ILanguage> languages = new ArrayList<>();
        languages.add(new Language("zh-CN", "简体中文", true));
        languages.add(new Language("zh-TW", "繁体中文"));
        languages.add(new Language("en", "English"));
        response.setData(languages);
        return response;
    }

    /**
     * 获取i18n的json文件语言包
     *
     * @param language
     * @param request
     * @return
     */
    @RequestMapping("/5f3586ac-bbd8-4fe7-af1e-33ac37387ed9")
    public DataResponse getLanguageMessage(@RequestParam String language, HttpServletRequest request) {
        DataResponse response = new DataResponse();
        try (InputStream stream = AssemblyUtil.getResourceStream(this.getClass().getProtectionDomain().getCodeSource().getLocation(), "i18n/" + language + ".json")) {
            Reader reader = new InputStreamReader(stream, request.getCharacterEncoding());
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            response.setData(sb.toString());
        } catch (IOException e) {
            response.setError(-1, e.getMessage());
        }
        return response;
    }
}