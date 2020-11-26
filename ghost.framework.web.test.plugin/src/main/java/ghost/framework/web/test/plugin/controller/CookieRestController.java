package ghost.framework.web.test.plugin.controller;

import ghost.framework.web.context.bind.annotation.CookieValue;
import ghost.framework.web.context.bind.annotation.CookieValues;
import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.bind.annotation.RestController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * package: ghost.framework.web.test.plugin.controller
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/3/5:12:55
 */
@RestController("/cookie")
public class CookieRestController {
     private Log log = LogFactory.getLog(DateRestController.class);
    @RequestMapping("/uuid")
    public UUID uuid(@CookieValue("UUID") UUID uuid) {
        log.info(uuid);
        return uuid;
    }
    @RequestMapping("/int")
    public int ints(@CookieValue("name") int i) {
        log.info(i);
        return i;
    }
    @RequestMapping("/date")
    public Date date(@CookieValue("name") Date date) {
        log.info(date);
        return date;
    }
    @RequestMapping("/get1")
    public Integer get1(@CookieValues List<Object> date) {
        log.info(date);
        return date.size();
    }
}