//package ghost.framework.core.event.annotation.factory;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.base.ICoreInterface;
//import ghost.framework.context.event.IApplicationOwnerEventFactory;
//import ghost.framework.context.event.annotation.factory.IAnnotationEventFactory;
//import ghost.framework.core.proxy.cglib.ICglibClassAnnotationEventFactory;
//import ghost.framework.util.ReflectUtil;
//import net.sf.cglib.proxy.Callback;
//import net.sf.cglib.proxy.Enhancer;
//import net.sf.cglib.proxy.MethodInterceptor;
//import net.sf.cglib.proxy.MethodProxy;
//
//import java.lang.reflect.Method;
//import java.util.Map;
///**
// * package: ghost.framework.core.proxy.cglib
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 2020/2/11:21:05
// */
//public class CglibClassAnnotationEventFactory implements Callback, ICglibClassAnnotationEventFactory, MethodInterceptor {
//    @Autowired
//    private IApplication app;
//
//    /**
//     * 创建代理对象
//     *
//     * @param coreInterface
//     * @param c
//     */
//    @Override
//    public void createObject(ICoreInterface coreInterface, Class<?> c) {
////        c.getDeclaredAnnotations();//伪获取注释
//        Enhancer enhancer = new Enhancer();
//        enhancer.setSuperclass(c);
//        enhancer.setCallback(this);
//        //设置继承代理标签接口
////        enhancer.setInterfaces(new Class[]{ICglibProxyTag.class});
//        Object object = null;
//        try {
//            //获取构建参数列表
//            Map<Class<?>, Object> map = coreInterface.newInstanceMapParameters(c);
//            if (map == null || map.isEmpty()) {
////                object = enhancer.create();
//            } else {
//                final Class[] argumentTypes = new Class[map.size()];
//                final Object[] arguments = new Object[map.size()];
//                for (Map.Entry<Class<?>, Object> entry : map.entrySet()) {
//                    argumentTypes[argumentTypes.length - 1] = entry.getKey();
//                    arguments[arguments.length - 1] = entry.getValue();
//                }
//
//                object = enhancer.create(argumentTypes, arguments);
//                Class ccc = object.getClass();
//                ccc.getDeclaredAnnotations();//伪获取注释
//                System.out.println(ccc.toString());
////                ProxyFactory proxyFactory = new ProxyFactory();
////                proxyFactory.setSuperclass(c);
////                enhanced = proxyFactory.c(typesArray, valuesArray);
//            }
//            //构建对象事件
//            coreInterface.newInstanceObjectEvent(object);
//            //注入对象
//            coreInterface.injection(object);
//            //绑定
//            coreInterface.beanEvent(object, null);
//            //绑定对象
//            System.out.println(object.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * @param o
//     * @param method
//     * @param objects
//     * @param methodProxy
//     * @return
//     * @throws Throwable
//     */
//    @Override
//    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
//        Object r = null;
//        if (method.getName().equals("loader")) {
//            if (ReflectUtil.invokeMethod(o, "isLoader", objects)) {
//                return null;
//            }
//            System.out.println(o.getClass().toString());
//            //获取代理调用对象函数的注释事件工厂接口
//            IAnnotationEventFactory factory = (IAnnotationEventFactory) o;
//            //获取注释事件工厂的注释类型标签
////            AnnotationTag.AnnotationTags annotationTag = IAnnotationRootExecutionChain.getStereoTypeAnnotationTag(this.app.getBean(IAnnotationRootExecutionChain.class).rootClass(), factory.getAnnotation());
////            //判断事件工厂注释类型是否为存储类型
////            if (annotationTag != null && annotationTag == AnnotationTag.AnnotationTags.StereoType) {
////                //加载注释执行链
////                ReflectUtil.invokeMethod(o, "loaderAnnotationChain", objects);
////            }
//            //定位核心接口
//            ReflectUtil.invokeMethod((IApplicationOwnerEventFactory)o, "positionOwner", objects);
//            //执行调用函数
//            r = methodProxy.invokeSuper(o, objects);
//            //判断事件工厂注释类型是否为存储类型
////            if (annotationTag != null && annotationTag == AnnotationTag.AnnotationTags.StereoType) {
////                //属于存储类型执行完成函数调用
////                ReflectUtil.invokeMethod(o, "completeExecute", objects);
////            } else {
////                //不属于存储类型执行函数调用
////                ReflectUtil.invokeMethod(o, "setExecute", objects);
////            }
//            return r;
//        }
//        return methodProxy.invokeSuper(o, objects);
//    }
//}