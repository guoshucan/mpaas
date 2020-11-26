package ghost.framework.context.base;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.environment.IEnvironment;

/**
 * package: ghost.framework.core.base
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:基础接口
 * @Date: 2019/12/10:7:14
 */
public interface IBase {
    /**
     * 获取env接口
     * @return
     */
    @NotNull
    IEnvironment getEnv();
    /**
     * 获取模块可空env
     * @return
     */
    @Nullable
    IEnvironment getNullableEnv();
    /**
     * 获取模块ome
     *
     * @return
     */
    @NotNull
    ApplicationHome getHome();
    /**
     * 获取是否为windows
     *
     * @return
     */
    boolean isWindows();

    /**
     * 获取是否为linux
     *
     * @return
     */
    boolean isLinux();
}
