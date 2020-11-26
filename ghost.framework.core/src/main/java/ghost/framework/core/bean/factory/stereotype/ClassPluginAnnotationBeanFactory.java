//package ghost.framework.core.bean.factory.stereotype;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.stereotype.Component;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.base.ICoreInterface;
//import ghost.framework.context.event.annotation.IClassAnnotationEventTargetHandle;
//import ghost.framework.context.bean.factory.stereotype.IClassPluginAnnotationEventFactory;
//import ghost.framework.context.exception.InjectionAnnotationBeanException;
//import ghost.framework.beans.plugin.bean.annotation.Plugin;
//import ghost.framework.util.ReflectUtil;
//import ghost.framework.util.StringUtils;
//import org.apache.log4j.Logger;
//
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Method;
//
///**
// * package: ghost.framework.core.bean.factory.stereotype
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 2020/2/14:8:23
// */
//@Component
//public class ClassPluginAnnotationEventFactory
//        <
//                O extends ICoreInterface,
//                T extends Class<?>,
//                E extends IClassAnnotationEventTargetHandle<O, T, V, String, Object>,
//                V extends Object
//                >
//        implements IClassPluginAnnotationEventFactory<O, T, E, V> {
//    /**
//     * 初始化类型 {@link Plugin} 注释事件工厂类
//     *
//     * @param app 应用接口
//     */
//    public ClassPluginAnnotationEventFactory(@Autowired IApplication app) {
//        this.app = app;
//    }
//
//    private IApplication app;
//
//    @Override
//    public String toString() {
//        return "ClassPluginAnnotationEventFactory{" +
//                "annotation=" + annotation.toString() +
//                '}';
//    }
//
//    @Override
//    public IApplication getApp() {
//        return app;
//    }
//     private Log log = LogFactory.getLog(ClassServiceAnnotationEventFactory.class);
//    @Override
//    public Log getLog() {
//        return log;
//    }
//    /**
//     * 注释类型
//     */
//    private final Class<? extends Annotation> annotation = Plugin.class;
//
//    /**
//     * 重新注释类型
//     *
//     * @return
//     */
//    @Override
//    public Class<? extends Annotation> getAnnotationClass() {
//        return annotation;
//    }
//
//    /**
//     * 绑定事件
//     *
//     * @param event 事件对象
//     */
//    @Override
//    public void loader(E event) {
//        this.getLog().debug("loader>class:" + event.getTarget().getName());
//        //获取注入注释对象
//        Plugin plugin = this.getAnnotation(event);
//        //绑定依赖类型
//        for (Class<?> c : plugin.depend()) {
//            event.getExecuteOwner().addBean(c);
//        }
//        //构建类型实例
//        this.newInstance(event);
//        //判断服务注释是否指定名称
//        if (plugin.value().equals("")) {
//            //未指定绑定服务名称
//            //使用注释函数名称绑定
//            try {
//                //判断函数名称绑定注释
//                Method method = ReflectUtil.findMethod(event.getTarget(), Plugin.Name.class);
//                //判断是否注释函数名称
//                if (method != null) {
//                    //获取函数注释名称
//                    Plugin.Name name = method.getAnnotation(Plugin.Name.class);
//                    //调用函数获取绑定名称
//                    event.setName(name.prefix() + method.invoke(event.getValue(), event.getExecuteOwner().newInstanceParameters(event.getValue(), method)).toString());
//                }
//            } catch (Exception e) {
//                throw new InjectionAnnotationBeanException(e);
//            }
//        } else {
//            //指定绑定的服务名称
//            event.setName(plugin.value());
//        }
//        //绑定入容器中
//        if (StringUtils.isEmpty(event.getName())) {
//            event.getExecuteOwner().addBean(event.getValue());
//        } else {
//            event.getExecuteOwner().addBean(event.getName(), event.getValue());
//        }
//    }
//
//    /**
//     * 删除绑定事件
//     *
//     * @param event 事件对象
//     */
//    @Override
//    public void unloader(E event) {
//
//    }
//}