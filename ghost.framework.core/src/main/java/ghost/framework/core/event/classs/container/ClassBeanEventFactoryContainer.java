//package ghost.framework.core.event.classs.container;
//import ghost.framework.beans.annotation.application.Application;
//import ghost.framework.beans.annotation.constraints.Nullable;
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.context.base.ICoreInterface;
//import ghost.framework.core.event.classs.IClassEventTargetHandle;
//import ghost.framework.context.event.factory.IClassBeanEventFactoryContainer;
//import ghost.framework.core.event.classs.factory.IClassEventFactory;
//
//import java.util.ArrayList;
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:类型注释事件监听工厂容器类
// * @Date: 7:45 2019/12/27
// * @param <O> 发起方
// * @param <T> 注释类型
// * @param <C> 目标类型
// * @param <E> 注释绑定事件目标处理
// * @param <L> IClassEventFactory接口类型或继承IClassEventFactory接口对象
// * @param <V>
// */
//public class ClassBeanEventFactoryContainer
//        <
//                O extends ICoreInterface,
//                T extends Class<?>,
//                C extends Class<?>,
//                E extends IClassEventTargetHandle<O, T, V, String, Object>,
//                L extends IClassEventFactory<O, T, C, E, V>,
//                V extends Object
//                >
//        extends AbstractEventFactoryContainer<L>
//        implements IClassBeanEventFactoryContainer<O, T, C, E, L, V> {
//    /**
//     * 初始化类事件监听容器
//     *
//     * @param parent 父级类事件监听容器
//     */
//    public ClassBeanEventFactoryContainer(@Application @Autowired @Nullable IClassBeanEventFactoryContainer<O, T, C, E, L, V> parent) {
//        this.parent = parent;
//    }
//
//    /**
//     * 父级类事件监听容器
//     */
//    private IClassBeanEventFactoryContainer<O, T, C, E, L, V> parent;
//
//    /**
//     * 获取级
//     *
//     * @return
//     */
//    @Override
//    public IClassBeanEventFactoryContainer<O, T, C, E, L, V> getParent() {
//        return parent;
//    }
//
//    /**
//     * 添加注释
//     *
//     * @param event
//     */
//    @Override
//    public void loader(E event) {
//        //获取执行列表
//        for (L l : new ArrayList<L>(this)) {
//            //加载处理
//            l.loader(event);
//            //判断是否已经处理
//            if (event.isHandle()) {
//                return;
//            }
//        }
//        //向父级加载
//        if (this.parent != null) {
//            this.parent.loader(event);
//        }
//    }
//}