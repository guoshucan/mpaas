//package ghost.framework.core.event.properties.factory.container;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.application.Application;
//import ghost.framework.beans.annotation.constraints.Nullable;
//import ghost.framework.context.base.ICoreInterface;
//import ghost.framework.core.event.factory.AbstractEventListenerFactoryContainer;
//import ghost.framework.core.event.properties.IPropertiesEventTargetHandle;
//import ghost.framework.core.event.properties.factory.IAbstractPropertiesEventFactory;
//import ghost.framework.context.io.Resource;
//
//import java.util.List;
//import java.util.Properties;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description: {@link Properties} 对象事件工厂容器类
// * @Date: 17:36 2020/1/14
// * @param <O> 发起方对象
// * @param <T> 目标处理对象
// * @param <P> {@link Properties}
// * @param <L> 为继承IAbstractPropertiesEventFactory接口的类型或对象
// * @param <E> 配置事件目标处理对象
// */
//public class PropertiesEventFactoryContainer
//        <
//                O extends ICoreInterface,
//                T extends Resource,
//                P extends Properties,
//                E extends IPropertiesEventTargetHandle<O, T, P>,
//                L extends Object>
//        extends AbstractEventListenerFactoryContainer<L>
//        implements IPropertiesEventFactoryContainer<O, T, P, E, L> {
//    /**
//     * 初始化类事件监听容器
//     *
//     * @param parent 父级类事件监听容器
//     */
//    public PropertiesEventFactoryContainer(@Application @Autowired @Nullable IPropertiesEventFactoryContainer<O, T, P, E, L> parent) {
//        this.parent = parent;
//    }
//
//    /**
//     * 父级类事件监听容器
//     */
//    private IPropertiesEventFactoryContainer<O, T, P, E, L> parent;
//
//    /**
//     * @param event 事件对象
//     */
//    @Override
//    public void loader(E event) {
//        this.getLog().info("loader:" + event.toString());
//        //遍历执行
//        for (List<L> sl : this.getEventExecuteOrderList()) {
//            //遍历注释绑定注入器接口类型列表
//            for (L l : sl) {
//                //判断是否执行接口
//                if (l instanceof Class) {
//                    //获取接口类型对象
//                    IAbstractPropertiesEventFactory factory = (IAbstractPropertiesEventFactory) event.getOwner().getBean((Class<?>) l);
//                    //比对资源文件扩展名
//                    if(factory.getExtensionName().equals(event.getTarget().getExtensionName())) {
//                        //执行事件
//                        factory.loader(event);
//                        //判断事件是否已经处理
//                        if (event.isHandle()) {
//                            return;
//                        }
//                    }
//                }
//                //对象类型处理
//                if (IAbstractPropertiesEventFactory.class.isAssignableFrom(l.getClass())) {
//                    //获取接口类型对象
//                    IAbstractPropertiesEventFactory factory = (IAbstractPropertiesEventFactory) l;
//                    //比对资源文件扩展名
//                    if(factory.getExtensionName().equals(event.getTarget().getExtensionName())) {
//                        factory.loader(event);
//                        //判断事件是否已经处理
//                        if (event.isHandle()) {
//                            return;
//                        }
//                    }
//                }
//            }
//        }
//        if (this.parent != null) {
//            this.parent.loader(event);
//            if (event.isHandle()) {
//                return;
//            }
//        }
//    }
//
//    /**
//     * @param event 事件对象
//     */
//    @Override
//    public void unloader(E event) {
//        this.getLog().info("unloader:" + event.toString());
//        //遍历执行
//        for (List<L> sl : this.getEventExecuteOrderList()) {
//            //遍历注释绑定注入器接口类型列表
//            for (L l : sl) {
//                //判断是否执行接口
//                if (l instanceof Class) {
//                    //获取接口类型对象
//                    IAbstractPropertiesEventFactory factory = (IAbstractPropertiesEventFactory) event.getOwner().getBean((Class<?>) l);
//                    //比对资源文件扩展名
//                    if(factory.getExtensionName().equals(event.getTarget().getExtensionName())) {
//                        //执行事件
//                        factory.unloader(event);
//                        //判断事件是否已经处理
//                        if (event.isHandle()) {
//                            return;
//                        }
//                    }
//                }
//                //对象类型处理
//                if (IAbstractPropertiesEventFactory.class.isAssignableFrom(l.getClass())) {
//                    //获取接口类型对象
//                    IAbstractPropertiesEventFactory factory = (IAbstractPropertiesEventFactory) l;
//                    //比对资源文件扩展名
//                    if(factory.getExtensionName().equals(event.getTarget().getExtensionName())) {
//                        //执行事件
//                        factory.unloader(event);
//                        //判断事件是否已经处理
//                        if (event.isHandle()) {
//                            return;
//                        }
//                    }
//                }
//            }
//        }
//        if (this.parent != null) {
//            this.parent.loader(event);
//            if (event.isHandle()) {
//                return;
//            }
//        }
//    }
//}