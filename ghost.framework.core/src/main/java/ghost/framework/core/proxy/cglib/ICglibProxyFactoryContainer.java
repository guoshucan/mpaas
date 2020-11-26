//package ghost.framework.core.proxy.cglib;
//import java.util.List;
///**
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:代理工厂容器接口
// * @Date: 12:49 2019/11/23
// */
//public interface ICglibProxyFactoryContainer {
//    /**
//     * 获取代理工厂列表
//     * @return
//     */
//    List<CglibProxyFactory> getProxyFactoryList();
//    /**
//     * 注册代理工厂
//     * @param proxyFactory 代理工厂
//     * @return 返回注释注册是否有效，无效一般已经存在
//     */
//    boolean put(CglibProxyFactory proxyFactory);
//    /**
//     * 获取代理工厂列表
//     *
//     * @param list 注册代理工厂列表
//     */
//    default void addList(List<CglibProxyFactory> list) {
//        for (CglibProxyFactory proxyFactory : list) {
//            this.put(proxyFactory);
//        }
//    }
//    /**
//     * 获取代理工厂列表
//     *
//     * @param list 注册代理工厂列表
//     */
//    default void addArray(CglibProxyFactory[] list) {
//        for (CglibProxyFactory proxyFactory : list) {
//            this.put(proxyFactory);
//        }
//    }
//    /**
//     * 卸载代理工厂
//     * @param proxyFactory 卸载代理工厂
//     * @return 返回卸载注释是否完成，无效一般注释不存在
//     */
//    boolean remove(CglibProxyFactory proxyFactory);
//    /**
//     * 卸载代理工厂
//     *
//     * @param list 卸载代理工厂列表
//     */
//    default void removeList(List<CglibProxyFactory> list) {
//        for (CglibProxyFactory proxyFactory : list) {
//            this.remove(proxyFactory);
//        }
//    }
//    /**
//     * 卸载代理工厂
//     *
//     * @param list 卸载代理工厂列表
//     */
//    default void removeArray(CglibProxyFactory[] list) {
//        for (CglibProxyFactory proxyFactory : list) {
//            this.remove(proxyFactory);
//        }
//    }
//}
