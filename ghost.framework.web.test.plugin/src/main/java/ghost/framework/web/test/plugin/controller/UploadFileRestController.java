package ghost.framework.web.test.plugin.controller;

import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.bind.annotation.RequestParam;
import ghost.framework.web.context.bind.annotation.RestController;
import ghost.framework.web.context.http.multipart.MultipartFile;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * package: ghost.framework.web.test.plugin.controller
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/3:22:51
 */
@RestController("/download/file")
public class UploadFileRestController {
     private Log log = LogFactory.getLog(DownloadFileRestController.class);

    @RequestMapping("/UploadFile")
    public String UploadFile(@RequestParam("upload") MultipartFile file) {
        log.info(file.getName());

        return "";
    }
}