package ghost.framework.web.angular1x.script.plugin;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.util.IdGenerator;
import ghost.framework.web.angular1x.context.controller.ControllerBase;
import ghost.framework.web.context.bind.annotation.RestController;

/**
 * package: ghost.framework.web.angular1x.ssh.script.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/8/26:9:54
 */
@RestController("/api")
public class ScriptTaskRestController extends ControllerBase {
    /**
     * 注入id生成接口
     */
    @Application
    @Autowired
    private IdGenerator generator;
}
