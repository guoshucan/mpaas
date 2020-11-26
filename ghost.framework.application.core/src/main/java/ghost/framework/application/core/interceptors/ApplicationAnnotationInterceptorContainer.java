//package ghost.framework.app.core.interceptors;
//
//import ghost.framework.core.application.interceptors.annotation.IApplicationAnnotationInterceptorContainer;
//import ghost.framework.core.interceptors.IClassEventInterceptor;
//import ghost.framework.core.interceptors.annotation.IAnnotationBeanInterceptor;
//
//import java.lang.annotation.Annotation;
//
///**
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:注释拦截器容器
// * @Date: 12:35 2019/11/22
// */
//public class ApplicationAnnotationInterceptorContainer extends ApplicationInterceptorContainer<IAnnotationBeanInterceptor>
//        implements IClassEventInterceptor, IApplicationAnnotationInterceptorContainer<IAnnotationBeanInterceptor> {
//    /**
//     * 初始化注释拦截器容器
//     */
//    public ApplicationAnnotationInterceptorContainer() {
//        super();
//    }
//    /**
//     * 类型引发的通知类型
//     *
//     * @param trigger 触发对象
//     * @param c       指定类型的类型
//     * @return 返回是否处理了事件
//     */
//    @Override
//    public boolean classAnnotationEvent(Object trigger, Class<?> c) {
//        synchronized (this.getInterceptorList()) {
//            for (IAnnotationBeanInterceptor i : this.getInterceptorList()) {
//                //判断是否未注册拦截器
//                for (Annotation a : c.getAnnotations()) {
//                    if (i.contains(a)) {
//                        if (i.classAnnotationEvent(trigger, c)) {
//                            //已经处理了此注释事件，不要延续
//                            return true;
//                        }
//                        break;
//                    }
//                }
//            }
//        }
//        return false;
//    }
//}