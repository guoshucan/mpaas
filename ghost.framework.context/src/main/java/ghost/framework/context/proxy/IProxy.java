package ghost.framework.context.proxy;

import ghost.framework.context.application.IGetApplication;
import net.bytebuddy.ByteBuddy;

import java.lang.annotation.Annotation;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 17:17 2020/1/12
 */
public interface IProxy extends IGetApplication {
    /**
     * 获取代理注释对象
     * @param annotationObject
     * @param <R>
     * @return
     */
    default <R> R getProxyAnnotationObject(Annotation annotationObject) {
        return (R) ProxyUtil.getProxyAnnotationObject(this.getApp().getBean(ByteBuddy.class), annotationObject, ProxyUtil.getProxyObjectAnnotationClass(annotationObject));
    }

//    /**
//     * 获取 {@link AutowiredClass} 注释的value参数值
//     * @param autowiredClass
//     * @return
//     */
//    default Class<?>[] getProxyAutowiredClassValue(AutowiredClass autowiredClass) {
//        return  ProxyUtil.getProxyAutowiredClassValue(autowiredClass);
//    }
}