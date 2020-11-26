//package ghost.framework.core.event;
//
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.base.ICoreInterface;
//import ghost.framework.context.event.IOwnerEventTargetHandle;
//
///**
// * package: ghost.framework.core.event
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 2020/2/9:16:13
// */
//public abstract class AbstractApplicationOwnerEventFactoryContainer<O extends ICoreInterface, T, E extends IOwnerEventTargetHandle<O, T>>
//        extends ApplicationOwnerEventFactory<O, T, E>
//    implements IApplicationOwnerEventFactoryContainer<O, T, E>
//{
//    /**
//     * 初始化应用注释注入基础类
//     *
//     * @param app 设置应用接口
//     */
//    protected AbstractApplicationOwnerEventFactoryContainer(IApplication app) {
//        super(app);
//    }
//}