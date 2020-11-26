package ghost.framework.web.error.plugin;

import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.bind.annotation.RestController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * package: ghost.framework.web.test.plugin.controller
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/3:22:52
 */
@RestController("/error")
public class ErrorRestController {
     private Log log = LogFactory.getLog(ErrorRestController.class);

    @RequestMapping("/404")
    public String error404() {
        this.log.info("404");
        return "404";
    }

    @RequestMapping("/500")
    public String error500() {
        this.log.info("500");
        return "500";
    }
}