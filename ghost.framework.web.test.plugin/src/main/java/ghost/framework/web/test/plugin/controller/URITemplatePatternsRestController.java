package ghost.framework.web.test.plugin.controller;

import ghost.framework.web.context.bind.annotation.PathVariable;
import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.bind.annotation.RequestMethod;
import ghost.framework.web.context.bind.annotation.RestController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * package: ghost.framework.web.test.plugin.controller
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * spring mcv 的 {@link AntPathMatcher} 作为解析匹配路径的核心类
 * @Date: 2020/2/3:22:51
 */
@RestController("/URITemplatePatterns")
public class URITemplatePatternsRestController {
     private Log log = LogFactory.getLog(DownloadFileRestController.class);

    @RequestMapping(value = "/owners/{ownerId}", method = RequestMethod.GET)
    public String findOwner(@PathVariable String ownerId) {

        return "displayOwner";
    }
    //@RequestMapping("/spring-web/{symbolicName:[a-z-]+}-{version:\d\.\d\.\d}.{extension:\.[a-z]}")
    public void handle(@PathVariable String version, @PathVariable String extension) {
        // ...
    }

    @RequestMapping("/user/{username:[a-zA-Z0-9_]+}/blog/{blogId}")
    public void handle(@PathVariable String username) {
        // ...
    }
}