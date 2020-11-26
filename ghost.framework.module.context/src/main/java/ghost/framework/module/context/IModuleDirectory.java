package ghost.framework.module.context;

import java.io.File;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块目录内容接口
 * @Date: 18:24 2019/6/4
 */
public interface IModuleDirectory {

    /**
     * 获取包临时目录
     *
     * @return
     */
    File getTempDirectory();
}
