package ghost.framework.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Map;

/**
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:注释反射工具
 * @Date: 22:50 2019/11/18
 */
public final class AnnotationReflectUtil {
    /**
     * 注释错误常量
     */
    public static final String ERROR = "error";
    /**
     * 反射注释参数名称
     */
    public static final String MEMBER_VALUES = "memberValues";

    /**
     * 设置注释值
     * @param a 注释
     * @param name   修改注释的参数名称
     * @param value  修改注释的参数值
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static void setValue(Annotation a, String name, Object value) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        InvocationHandler ih = Proxy.getInvocationHandler(a);
        Field f = ih.getClass().getDeclaredField(MEMBER_VALUES);
        f.setAccessible(true);
        Map m = (Map) f.get(ih);
        m.put(name, value);
    }
    /**
     * 修改注释值
     *
     * @param parameter 注释所在参数
     * @param annotation      注释类型
     * @param name   修改注释的参数名称
     * @param value  修改注释的参数值
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static void setParameterValue(Parameter parameter, Class<? extends Annotation> annotation, String name, Object value) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Annotation a = parameter.getAnnotation(annotation);
        if (a == null) {
            return;
        }
        setValue(a, name, value);
    }
    /**
     * 修改注释值
     *
     * @param method 注释所在函数
     * @param annotation      注释类型
     * @param name   修改注释的参数名称
     * @param value  修改注释的参数值
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static void setMethodValue(Method method, Class<? extends Annotation> annotation, String name, Object value) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        method.setAccessible(true);
        Annotation a = method.getAnnotation(annotation);
        if (a == null) {
            return;
        }
        setValue(a, name, value);
    }

    /**
     * 修改注释值
     *
     * @param field 注释所在例
     * @param annotation     注释类型
     * @param name  修改注释的参数名称
     * @param value 修改注释的参数值
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static void setFieldValue(Field field, Class<? extends Annotation> annotation, String name, Object value) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        field.setAccessible(true);
        Annotation a = field.getAnnotation(annotation);
        if (a == null) {
            return;
        }
        setValue(a, name, value);
    }
}