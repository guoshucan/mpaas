package ghost.framework.data;

import ghost.framework.module.module.ModuleTypes;
import ghost.framework.module.annotations.IPackageInfo;
import ghost.framework.module.language.annotations.Global;
import ghost.framework.module.language.annotations.Local;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 8:59 2019-02-08
 */
@Global
@Local
public final class PackageInfo implements IPackageInfo {
    /**
     * 获取模块id
     * @return
     */
    @Override
    public String id() {
        return "8a65fe54-b1f7-4a5c-a493-1aefd8cd6f05";
    }
    /**
     * 重写模块类型
     * @return
     */
    @Override
    public ModuleTypes type() {
        return ModuleTypes.module;
    }

    /**
     * 重写为内置模块
     * @return
     */
    @Override
    public boolean built() {
        return true;
    }
}
