package ghost.framework.web.test.plugin.controller;

import ghost.framework.web.context.bind.annotation.PathVariable;
import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.bind.annotation.RestController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * package: ghost.framework.web.test.plugin.controller
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/3/5:9:30
 */
@RestController("/path")
public class PathVariableRestController {
     private Log log = LogFactory.getLog(PathVariableRestController.class);

    @RequestMapping("/get0/{name}")
    public String get0(@PathVariable("name") String date) {
        log.info(date);
        return date;
    }

    @RequestMapping("/get1/{name}/{adada}/{intssss}")
    public String get1(@PathVariable("name") String date, @PathVariable String adada, @PathVariable("intssss") int intssss) {
        log.info(date);
        log.info(adada);
        log.info(intssss);
        return date;
    }
}