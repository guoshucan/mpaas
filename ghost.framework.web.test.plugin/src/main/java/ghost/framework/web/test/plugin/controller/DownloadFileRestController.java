package ghost.framework.web.test.plugin.controller;

import ghost.framework.web.context.bind.annotation.PathVariable;
import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.bind.annotation.RequestParam;
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
@RestController("/download/file")
public class DownloadFileRestController {
     private Log log = LogFactory.getLog(DownloadFileRestController.class);

    @RequestMapping("/DownloadFile1/{name}")
    public void DownloadFile1(@PathVariable("name") String name) {
        log.info(name);
    }

    @RequestMapping("/DownloadFile0")
    public void DownloadFile0(@RequestParam("name") String name) {
        log.info(name);
    }

    @RequestMapping("/DownloadFile2")
    public void DownloadFile2(String name, int shuzi) {
        log.info(name + ">" + shuzi);
    }
}