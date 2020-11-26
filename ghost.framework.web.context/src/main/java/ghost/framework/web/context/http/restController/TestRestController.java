package ghost.framework.web.context.http.restController;

import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.bind.annotation.RestController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * package: ghost.framework.web.context.http.restController
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 13:37 2020/1/31
 */
@RestController("/test")
public class TestRestController {
     private Log log = LogFactory.getLog(TestRestController.class);
    public TestRestController(){

    }
    @RequestMapping("add")
    public void add(String name) {
        this.log.info("add:" + name);
    }

    @RequestMapping("delete")
    public void delete(String name) {
        this.log.info("delete:" + name);
    }

    @RequestMapping("get")
    public String get(String name) {
        this.log.info("get:" + name);
        return name;
    }
}