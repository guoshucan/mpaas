package ghost.framework.module.language;//package ghost.framework.module.language;
//
//import ghost.framework.module.content.Module;
//import ghost.framework.module.annotation.IPackageInfo;
//import ghost.framework.app.context.IApplicationContent;
//
//import java.io.IOException;
//import java.security.ProtectionDomain;
//import java.util.List;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:模块语言包
// * @Date: 23:22 2019/4/24
// */
//public class ModuleLanguage extends Module implements IModuleLanguage {
//    /**
//     *
//     * @param owner 模块容器
//     * @param dev 是否为开发模式
//     * @param domain        包域
//     * @param info
//     * @throws IOException
//     */
//    public ModuleLanguage(
//            IApplicationContent owner,
//            boolean dev,
//            ProtectionDomain domain,
//            IPackageInfo info) throws IOException {
//        super(owner, dev, domain, info);
//    }
//    /**
//     * 模块语言名称
//     * list表示一个语言包可以有多个语言
//     */
//    private List<String> languageNames;
//
//    /**
//     * 获取语言名称
//     *
//     * @return
//     */
//    @Override
//    public List<String> getLanguageNames() {
//        return languageNames;
//    }
//
//    /**
//     * 设置语言名称
//     *
//     * @param languageNames
//     */
//    @Override
//    public void setLanguageNames(List<String> languageNames) {
//        this.languageNames = languageNames;
//    }
//
//}