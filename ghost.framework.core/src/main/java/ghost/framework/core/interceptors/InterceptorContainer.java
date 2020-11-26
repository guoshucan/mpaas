//package ghost.framework.core.interceptors;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:拦截器容器基础类
// * @Date: 0:40 2019/12/1
// */
//public abstract class InterceptorContainer<I extends IInterceptor> implements IInterceptorContainer<I> {
//    /**
//     * 初始化拦截器容器基础类
//     */
//    protected InterceptorContainer() {
//        this.list = new ArrayList<>(5);
//    }
//
//    /**
//     * 拦截器列表
//     */
//    private List<I> list;
//
//    /**
//     * 获取拦截器列表
//     *
//     * @return
//     */
//    @Override
//    public List<I> getInterceptorList() {
//        return list;
//    }
//}