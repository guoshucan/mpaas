package ghost.framework.data.core;

import ghost.framework.module.module.ModuleTypes;
import ghost.framework.module.annotations.IPackageInfo;
import ghost.framework.module.language.annotations.Global;
import ghost.framework.module.language.annotations.Local;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 8:57 2019-02-08
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
        return "e9d38eb2-75fa-4df2-ae22-d47f3a91936e";
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
