//package ghost.framework.core.interceptors;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:事件拦截器基础类
// * @Date: 0:59 2019/12/1
// */
//public abstract class EventInterceptor<I> implements IEventInterceptor<I> {
//    /**
//     * 初始化事件拦截器基础类
//     */
//    protected EventInterceptor() {
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
//    public List<I> getList() {
//        return list;
//    }
//}