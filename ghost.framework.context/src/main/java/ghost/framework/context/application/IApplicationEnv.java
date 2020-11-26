package ghost.framework.context.application;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.constraints.Nullable;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 1:48 2019-05-28
 */
public interface IApplicationEnv {
    /**
     * 获取应用内容env
     *
     * @return
     */
    @NotNull
    IApplicationEnvironment getEnv();
    /**
     * 获取模块可空env
     * @return
     */
    @Nullable
    IApplicationEnvironment getNullableEnv();
}