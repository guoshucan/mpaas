//package ghost.framework.module.context.event;
//
//import ghost.framework.core.event.IEventListener;
//
//import java.security.CodeSource;
//import java.security.ProtectionDomain;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.jar.JarEntry;
//
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:模块加载事件监听接口
// * @Date: 15:23 2019-05-19
// */
//public interface IModuleLoaderEventListener extends IEventListener {
//    /**
//     * @param appPackages
//     * @param appClasss
//     * @param entry
//     */
//    default void loadJarLoader(HashMap<String, Package> appPackages, ConcurrentHashMap<String, Object> appClasss, Map.Entry<CodeSource, ProtectionDomain> entry) {
//
//    }
//
//    /**
//     * @param entry
//     */
//    default void loadJarLoader(JarEntry entry) {
//
//    }
//
//    /**
//     * @param entry
//     */
//    default void loadClasssLoader(Map.Entry<CodeSource, ProtectionDomain> entry) {
//    }
//
//    /**
//     * @param appPackages
//     * @param appClasss
//     * @param entry
//     */
//    default void uninstallJarLoader(HashMap<String, Package> appPackages, ConcurrentHashMap<String, Object> appClasss, Map.Entry<CodeSource, ProtectionDomain> entry) {
//
//    }
//
//    /**
//     * @param entry
//     */
//    default void uninstallJarLoader(JarEntry entry) {
//
//    }
//
//    /**
//     * @param entry
//     */
//    default void uninstallClasssLoader(Map.Entry<CodeSource, ProtectionDomain> entry) {
//    }
//}