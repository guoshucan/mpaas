package ghost.framework.context.application;

import ghost.framework.context.base.IGetHome;
import ghost.framework.util.NotImplementedException;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:应用内容接口
 * @Date: 1:15 2019-05-28
 */
public interface IApplicationContent extends IGetHome {
    /**
     * 获取是否为windows
     *
     * @return
     */
    boolean isWindows() throws NotImplementedException;

    /**
     * 获取是否为linux
     *
     * @return
     */
    boolean isLinux() throws NotImplementedException;
}