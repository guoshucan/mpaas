//package ghost.framework.core.proxy.cglib;
//import java.util.ArrayList;
//import java.util.List;
///**
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:代理工厂容器
// * @Date: 12:49 2019/11/23
// */
//public class CglibProxyFactoryContainer implements ICglibProxyFactoryContainer {
//    /**
//     * 同步
//     */
//    private Object root = new Object();
//    /**
//     * 初始化代理工厂容器
//     */
//    public CglibProxyFactoryContainer(){
//        this.list = new ArrayList<>();
//    }
//
//    /**
//     * 代理工厂列表
//     */
//    private List<CglibProxyFactory> list;
//
//    /**
//     * 获取代理工厂列表
//     * @return
//     */
//    @Override
//    public List<CglibProxyFactory> getProxyFactoryList() {
//        return list;
//    }
//
//    /**
//     * 注册代理工厂
//     * @param proxyFactory 代理工厂
//     * @return
//     */
//    @Override
//    public boolean put(CglibProxyFactory proxyFactory) {
//        synchronized (root) {
//            return this.list.put(proxyFactory);
//        }
//    }
//
//    /**
//     * 卸载代理工厂
//     * @param proxyFactory 卸载代理工厂
//     * @return
//     */
//    @Override
//    public boolean remove(CglibProxyFactory proxyFactory) {
//        synchronized (root) {
//            return this.list.remove(proxyFactory);
//        }
//    }
//}