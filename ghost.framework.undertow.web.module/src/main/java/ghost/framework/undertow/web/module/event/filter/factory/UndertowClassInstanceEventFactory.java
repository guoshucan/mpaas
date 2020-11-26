//package ghost.framework.undertow.web.module.event.filter.factory;
//
//import ghost.framework.beans.annotation.application.Application;
//import ghost.framework.beans.annotation.container.BeanCollectionContainer;
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.base.ICoreInterface;
//import ghost.framework.context.bean.factory.IClassBeanFactoryContainer;
//import ghost.framework.context.bean.factory.IClassBeanTargetHandle;
//import ghost.framework.core.event.AbstractApplicationOwnerEventFactory;
//
//import javax.servlet.Filter;
//import javax.servlet.Servlet;
//import java.util.Arrays;
//import java.util.List;
//
///**
// * package: ghost.framework.undertow.web.module.event.filter.factory
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 22:52 2020/1/29
// */
//@BeanCollectionContainer(IClassBeanFactoryContainer.class)
//public class UndertowClassInstanceEventFactory
//        <
//                O extends ICoreInterface,
//                T extends Class<?>,
//                C extends Class<?>,
//                E extends IClassBeanTargetHandle<O, T, V, String, Object>,
//                V extends Object
//                >
//        extends AbstractApplicationOwnerEventFactory<O, T, E>
//        implements IUndertowClassInstanceEventFactory<O, T, C, E, V> {
//    /**
//     * 初始化应用注释注入基础类
//     *
//     * @param app 设置应用接口
//     */
//    protected UndertowClassInstanceEventFactory(@Application @Autowired IApplication app) {
//        super(app);
//    }
//
//    /**
//     * 加载事件
//     *
//     * @param event 事件对象
//     */
//    @Override
//    public void loader(E event) {
//        if (!this.classList.contains(event.getTarget())) {
//            return;
//        }
//        this.getLog().info("loader:" + event.toString());
//        //定义拥有者
//        this.positionOwner(event);
//        //添加执行创建对象
//        if (event.getParameters() == null) {
//            event.setValue((V) new ClassInstanceFactory(event.getExecuteOwner().newInstance(event.getTarget())));
//        } else {
//            event.setValue((V) new ClassInstanceFactory(event.getExecuteOwner().newInstance(event.getTarget(), event.getParameters())));
//        }
//        //已经处理
//        event.setHandle(true);
//    }
//
//    /**
//     * 事件类型
//     */
//    private List<Class<?>> classList = Arrays.asList(new Class[]{Filter.class, Servlet.class});
//
//    /**
//     * 获取类型列表
//     *
//     * @return
//     */
//    @Override
//    public C getTargetClass() {
//        return null;
//    }
//}