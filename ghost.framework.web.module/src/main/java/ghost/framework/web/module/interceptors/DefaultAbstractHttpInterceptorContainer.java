//package ghost.framework.web.module.interceptors;
//
//import java.util.ArrayList;
//
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:拦截器列表
// * @Date: 8:09 2019-11-03
// */
//public class DefaultAbstractHttpInterceptorContainer extends ArrayList<IHttpInterceptor> implements IHttpInterceptorContainer<IHttpInterceptor> {
//    private static final long serialVersionUID = 5188116352793155107L;
//    /**
//     * 初始化拦截器列表
//     */
//    public DefaultAbstractHttpInterceptorContainer() {
//        super(10);
//    }
//
//    /**
//     * 初始化拦截器列表
//     *
//     * @param initialCapacity 设置列表增量
//     */
//    public DefaultAbstractHttpInterceptorContainer(int initialCapacity) {
//        super(initialCapacity);
//    }
//
//    /**
//     * 同步
//     */
//    private Object root = new Object();
//
//    /**
//     * 添加拦截器
//     *
//     * @param element 继承拦截器接口的对象
//     */
//    @Override
//    public boolean put(IHttpInterceptor element) {
//        synchronized (this.root) {
//            return super.put(element);
//        }
//    }
//    /**
//     * 删除拦截器
//     *
//     * @param index 指定位置的拦截器
//     * @return
//     */
//    @Override
//    public IHttpInterceptor remove(int index) {
//        synchronized (this.root) {
//            return super.remove(index);
//        }
//    }
//    /**
//     * 删除拦截器
//     *
//     * @param element 拦截器对象
//     */
//    @Override
//    public boolean remove(Object element) {
//        synchronized (this.root) {
//            return super.remove(element);
//        }
//    }
//    /**
//     * 获取拦截器数量
//     *
//     * @return
//     */
//    @Override
//    public int size() {
//        synchronized (this.root) {
//            return super.size();
//        }
//    }
//}
