package ghost.framework.context.proxy;
import ghost.framework.util.ReflectUtil;
import ghost.framework.util.proxy.ProxyException;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * package: ghost.framework.util
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:代理工具类
 * @Date: 2020/1/9:21:28
 */
public final class ProxyUtil {
    /**
     * 获取注释列表
     * @param annotation 注释对象
     * @param targetAnnotation 目标注释类型
     * @param <T> 返回注释类型
     * @return
     */
    public static <T extends Annotation> List<T> getProxyAnnotationObjectList(Annotation annotation, Class<? extends Annotation> targetAnnotation) {
        List<T> rs = new ArrayList<>();
        Map<String, Object> map = getProxyObjectAnnotationMemberValues(annotation);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue().getClass().isArray()) {
                for (Object o : (Object[]) entry.getValue()) {
                    Class<?> c = getProxyObjectClass(o);
                    if (c.equals(targetAnnotation)) {
                        rs.add((T) o);
                    }
                }
            } else {
                if (entry.getValue().getClass().equals(targetAnnotation)) {
                    rs.add((T) entry.getValue());
                }
            }
        }
        return rs;
    }
    /**
     * 获取代理对象注释参数map
     * @param object 对象
     * @return
     * @throws
     */
    public static Map<String, Object> getProxyObjectAnnotationMemberValues(Object object) {
        //判断是否有代理
        if (!Proxy.isProxyClass(object.getClass())) {
            throw new ProxyException(object.getClass().getName());
        }
        //获取代理接口
        InvocationHandler handler = Proxy.getInvocationHandler(object);
        //获取代理声明列表
        Map<String, Object> map = ReflectUtil.findField(handler, "memberValues");
        return map;
    }

    /**
     * 获取注释的map模型值地图
     * @param annotation
     * @return
     */
    public static Map<String, Object> getProxyAnnotationMap(Annotation annotation) {
        //判断是否有代理
        if (Proxy.isProxyClass(annotation.getClass())) {
            //获取代理接口
            InvocationHandler handler = Proxy.getInvocationHandler(annotation);
            //获取代理声明列表
            Map<String, Object> map = ReflectUtil.findField(handler, "memberValues");
            return map;
        }
        throw new IllegalArgumentException(annotation.toString());
    }
    /**
     * 获取代理注释对象
     * 如果不是代理返回源对象
     *
     * @param buddy  动态字节生成类
     * @param object 注释对象
     * @param c      注释类型
     * @param <T>
     * @return
     * @throws
     */
    public static <T extends Annotation> T getProxyAnnotationObject(ByteBuddy buddy, Object object, Class<? extends Annotation> c) {
        //判断是否有代理
        if (Proxy.isProxyClass(object.getClass())) {
            //获取代理接口
            InvocationHandler handler = Proxy.getInvocationHandler(object);
            //获取代理声明列表
            Map<String, Object> map = ReflectUtil.findField(handler, "memberValues");
            //动态创建类型对象
//            ByteBuddy buddy = new ByteBuddy(ClassFileVersion.JAVA_V8);
            //继承注释类型创建动态类建造者
            DynamicType.Builder builder = buddy.subclass(c);
            //填充注释函数
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                builder = builder.method(ElementMatchers.named(entry.getKey())).intercept(FixedValue.value(entry.getValue()));
            }
            //创建类型
            Class<?> dynamicType = builder
                    .make()
                    .load(c.getClassLoader())
                    .getLoaded();
            //创建类型实例
            try {
                return (T) dynamicType.newInstance();
            } catch (InstantiationException e) {
                throw new ProxyException(object.getClass().getName(), e);
            } catch (IllegalAccessException e) {
                throw new ProxyException(object.getClass().getName(), e);
            }
        }
        return (T) object;
    }

    /**
     * 判断对象是否为代理对象
     *
     * @param object
     * @return
     */
    public static boolean isProxyObjectClass(Object object) {
        return Proxy.isProxyClass(object.getClass());
    }

    /**
     * 获取代理对象注释类型
     *
     * @param object 代理注释对象
     * @param <T>
     * @return
     */
    public static <T extends Class<? extends Annotation>> T getProxyObjectAnnotationClass(Object object) {
        return (T) getProxyObjectClass(object);
    }

    /**
     * 获取代理对象类型
     * 如果对象属于代理对象是返回代理对象的原始对象类型
     * 如果没有代理直接返回对象类型
     *
     * @param object
     * @return
     */
    public static Class<?> getProxyObjectClass(Object object) {
        if (Proxy.isProxyClass(object.getClass())) {
            Object handler = Proxy.getInvocationHandler(object);
            //判断注释代理类型处理
            if (handler.getClass().getName().equals("sun.reflect.annotation.AnnotationInvocationHandler")) {
                //获取代理类型声明的原始类型
                return ReflectUtil.findField(handler, "type");
            }
        }
        return object.getClass();
    }

    /**
     * 判断是否为Cglib代理对象
     * @param target
     * @return
     */
    public static boolean isCglibProxyObject(Object target) {
        return ReflectUtil.existsField(target.getClass(), "CGLIB$CALLBACK_0");
    }

    /**
     * 获取代理对象
     * @param target
     * @param <T>
     * @return
     */
    public static <T> T getProxyTarget(Object target) {
        //获取CGLIB代理声明
        Field field = ReflectUtil.findField(target.getClass(), "CGLIB$CALLBACK_0");
        try {
            if (field != null) {
                return ((IProxyTarget<T>) field.get(target)).getTarget();
            }
        } catch (Exception e) {
            throw new ProxyException(e.getMessage(), e);
        }
        //获取其它代理对象
        return (T) target;
    }

//    /**
//     * 获取 {@link AutowiredClass} 注释的value参数值
//     *
//     * @param autowiredClass
//     * @return
//     */
//    public static Class<?>[] getProxyAutowiredClassValue(AutowiredClass autowiredClass) {
//        if (Proxy.isProxyClass(autowiredClass.getClass())) {
//            Object handler = Proxy.getInvocationHandler(autowiredClass);
//            //获取代理声明列表
//            Map<String, Object> map = ReflectUtil.findField(handler, "memberValues");
//            Object p = map.get("value");
//            try {
//                if(p instanceof Class<?>[]){
//                    return (Class<?>[]) p;
//                }
//                Field field = ReflectUtil.findField(p.getClass(), "foundType");
//                field.setAccessible(true);
//                String foundType = (String) field.get(p);
//                //class java.lang.Class[interface ghost.framework.web.context.servlet.context.IServletContextInitializerContainer]
//                foundType = foundType.substring("class java.lang.Class[".length());
//                foundType = foundType.substring(0, foundType.length() - 1);
//                foundType = StringUtils.replace(foundType, "interface ", "");
//                foundType = StringUtils.replace(foundType, "class ", "");
//                String[] strings = StringUtils.split(foundType, ",");
//                Class<?>[] classes = new Class[strings.length];
//                for (String c : strings) {
//                    classes[classes.length - 1] = Class.forName(c, false, Thread.currentThread().getContextClassLoader());
//                }
//                return classes;
//            } catch (Exception e) {
//                throw new ProxyException(e);
//            }
//        }
//        return autowiredClass.value();
//    }
}