package ghost.framework.web.test.plugin.controller;

import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.bind.annotation.RequestParam;
import ghost.framework.web.context.bind.annotation.RestController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;

/**
 * package: ghost.framework.web.test.plugin.controller
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/3:22:52
 */
@RestController("/date")
public class DateRestController {
     private Log log = LogFactory.getLog(DateRestController.class);

    @RequestMapping("/get0")
    public Date get0(@RequestParam("name") Date date) {
        log.info(date);
        return date;
    }
    @RequestMapping("/get1")
    public Date get1(Date date) {
        log.info(date);
        return date;
    }
}