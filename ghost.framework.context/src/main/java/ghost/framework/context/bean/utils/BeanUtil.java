package ghost.framework.context.bean.utils;

import ghost.framework.beans.annotation.conditional.ConditionalOnMissingBean;
import ghost.framework.beans.annotation.order.Order;
import ghost.framework.beans.annotation.stereotype.Service;
import ghost.framework.context.bean.BeanMethod;
import ghost.framework.context.bean.IBeanDefinition;
import ghost.framework.beans.BeanException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:绑定工具类
 * @Date: 10:34 2019-06-09
 */
public class BeanUtil {
    /**
     * 升序排序，从小到大排序
     * 绑定函数按照 {@link Order} 注释位置排序，如果没有注释默认值为0
     *
     * @param list 绑定函数列表
     */
    public static void beanMethodListSort(List<BeanMethod> list) {
        list.sort(new Comparator<BeanMethod>() {
            @Override
            public int compare(BeanMethod a, BeanMethod b) {
                return a.getOrder() - b.getOrder();
            }
        });
    }

    /**
     * @param c
     * @return
     */
    public static List<BeanMethod> getBeanMethodOrderList(Class<?> c) {
        List<BeanMethod> list = new ArrayList<>();
        //获取对象函数列表
        for (Method method : c.getDeclaredMethods()) {
            //判断只有注释的函数才添加
            if (isBeanMethod(method)) {
                list.add(new BeanMethod(method));
            }
        }
        beanMethodListSort(list);
        return list;
    }

    /**
     * 判断是否为无效bean函数
     *
     * @param method
     * @return
     */
    public static boolean isBeanMethod(Method method) {
        return Modifier.isPublic(method.getModifiers()) && method.getDeclaredAnnotations().length > 0;
    }

    /**
     * 判断绑定名称是否存在
     *
     * @param map  绑定列表
     * @param name 绑定名称
     * @param <B>
     * @return
     */
    public static <B extends IBeanDefinition> boolean beanContains(Map<String, B> map, String name) {
        //接口类型处理
        synchronized (map) {
            //遍历绑定定义对象
            for (String n : map.keySet()) {
                //判断接口是否存在绑定定义对象中
                if (n.equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断绑定对象是否存在
     *
     * @param map 绑定列表
     * @param o   绑定对象
     * @param <B>
     * @return
     */
    public static <B extends IBeanDefinition> boolean beanContains(Map<String, B> map, Object o) {
        return beanContains(map, o.getClass());
    }

    /**
     * 判断绑定类型是否存在
     *
     * @param map 绑定列表
     * @param c   绑定类型
     * @param <B>
     * @return
     */
    public static <B extends IBeanDefinition> boolean beanContains(Map<String, B> map, Class<?> c) {
        //判断类型是否未接口
        if (c.isInterface()) {
            //接口类型处理
            synchronized (map) {
                //遍历绑定定义对象
                for (IBeanDefinition d : map.values()) {
                    //判断接口是否存在绑定定义对象中
                    if (c.isAssignableFrom(d.getObject().getClass())) {
                        return true;
                    }
                }
            }
        } else {
            String cn = c.getName();
            //不是接口类型处理
            synchronized (map) {
                //遍历绑定定义对象
                for (Map.Entry<String, B> entry : map.entrySet()) {
                    //判断接口是否存在绑定定义对象中
                    if (c.isAssignableFrom(entry.getValue().getObject().getClass()) ||
                            entry.getValue().getObject().getClass().getName().equals(cn) ||
                            entry.getKey().equals(cn)
                    ) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

//    /**
//     * 对象绑定后调用函数动作
//     * 对该对象的类型的函数存在{@link ghost.framework.beans.execute.annotation.BeanAction}时处理
//     *
//     * @param o
//     * @return
//     */
//    public static Method getBeanAfterInvokeMethodAction(Object o) {
//        return getInjectionInvokeMethodAction(o.getClass(), Action.After);
//    }
//
//    /**
//     * 对象绑定后调用函数动作
//     * 对该对象的类型的函数存在{@link ghost.framework.beans.execute.annotation.BeanAction}时处理
//     *
//     * @param c
//     * @return
//     */
//    public static Method getBeanAfterInvokeMethodAction(Class<?> c) {
//        return getInjectionInvokeMethodAction(c, Action.After);
//    }
//
//    /**
//     * 获取对象调用 {@link BeanAction}初始化注释函数
//     * 获取在绑定注入后调用函数
//     *
//     * @param o
//     * @return
//     */
//    public static Method getInjectionAfterInvokeMethodAction(Object o) {
//        return getInjectionInvokeMethodAction(o.getClass(), Action.After);
//    }

//    /**
//     * 获取对象调用 {@link BeanAction}初始化注释函数
//     *
//     * @param c
//     * @return
//     */
//    public static Method getInjectionAfterInvokeMethodAction(Class<?> c) {
//        return getInjectionInvokeMethodAction(c, Action.After);
//    }
//
//    /**
//     * @param o
//     * @return
//     */
//    public static Method getInjectionInvokeMethodAction(Object o, Action action) {
//        return getInjectionInvokeMethodAction(o.getClass(), action);
//    }
//
//    /**
//     * @param c
//     * @param action
//     * @return
//     */
//    public static Method getInjectionInvokeMethodAction(Class<?> c, Action action) {
//        for (Method method : c.getDeclaredMethods()) {
//            if (method.isAnnotationPresent(BeanAction.class) && method.getAnnotation(BeanAction.class).value().equals(action)) {
//                return method;
//            }
//        }
//        return null;
//    }
//
//    /**
//     * 获取绑定对象调用 {@link BeanAction}初始化注释函数
//     * 获取在绑定注入前调用函数列表
//     *
//     * @param o
//     * @return
//     */
//    public static List<Method> getInjectionBeforeInvokeMethodActionList(Object o) {
//        return getInjectionBeforeInvokeMethodActionList(o.getClass());
//    }

//    /**
//     * 获取对象调用 {@link BeanAction}初始化注释函数
//     * 获取在绑定注入前调用函数列表
//     *
//     * @param c
//     * @return
//     */
//    public static List<Method> getInjectionBeforeInvokeMethodActionList(Class<?> c) {
//        return getInjectionInvokeMethodActionList(c, Action.Before);
//    }
//
//    /**
//     * 获取对象调用 {@link BeanAction}初始化注释函数
//     * 获取在绑定注入后调用函数列表
//     *
//     * @param o
//     * @return
//     */
//    public static List<Method> getInjectionAfterInvokeMethodActionList(Object o) {
//        return getInjectionInvokeMethodActionList(o.getClass(), Action.After);
//    }

//    /**
//     * 获取对象调用 {@link BeanAction}初始化注释函数
//     * 获取在绑定注入后调用函数列表
//     *
//     * @param c      注入类型
//     * @param action 绑定动作枚举
//     * @return
//     */
//    public static List<Method> getInjectionInvokeMethodActionList(Class<?> c, Action action) {
//        List<Method> list = new ArrayList<>();
//        for (Method method : c.getDeclaredMethods()) {
//            if (method.isAnnotationPresent(BeanAction.class) && method.getAnnotation(BeanAction.class).value().equals(action)) {
//                list.add(method);
//            }
//        }
//        OrderAnnotationUtil.methodListOrderSort(list);
//        return list;
//    }

//    /**
//     * 获取对象调用 {@link BeanAction}初始化注释函数
//     * 获取在绑定后调用函数列表
//     *
//     * @param o
//     * @return
//     */
//    public static List<Method> getBeanAfterActionList(Object o) {
//        return getBeanAfterActionList(o.getClass());
//    }

//    /**
//     * 获取对象调用 {@link BeanAction}初始化注释函数
//     * 获取在绑定后调用函数列表
//     *
//     * @param c
//     * @return
//     */
//    public static List<Method> getBeanAfterActionList(Class<?> c) {
//        return getInjectionInvokeMethodActionList(c, Action.After);
//    }

    /**
     * 使用接口获取绑定定义
     *
     * @param map
     * @param c
     * @return
     */
    public static IBeanDefinition getInterfaceBeanDefinition(Map<String, IBeanDefinition> map, Class<?> c) {
        synchronized (map) {
            for (Map.Entry<String, IBeanDefinition> entry : map.entrySet()) {
                if (c.isAssignableFrom(entry.getValue().getObject().getClass())) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

//    /**
//     * 获取注释Id。
//     * 如果没有获取到接口Id注释侧返回null。
//     *
//     * @param o
//     * @return
//     */
////    @SuppressWarnings("unchecked")
//    public static String getAnnotationKey(Object o) {
//        try {
//            if (o.getClass().isAnnotationPresent(Key.class)) {
//                return o.getClass().getAnnotation(Key.class).value();
//            }
//        } catch (NullPointerException e) {
//        }
//        return null;
//    }

//    /**
//     * 获取类型Id。
//     * 如果类型没有注释Id侧返回类名称。
//     *
//     * @param c
//     * @return
//     */
//    public static String getClassKey(Class<?> c) {
//        try {
//            if (c.isAnnotationPresent(Key.class)) {
//                return c.getAnnotation(Key.class).value();
//            }
//        } catch (NullPointerException e) {
//        }
//        return c.getName();
//    }

//    /**
//     * 获取包key。
//     *
//     * @param packageName
//     * @return
//     */
//    public static String getPackageKey(String packageName) {
//        return Package.getPackage(packageName).getAnnotation(Key.class).value();
//    }

//    /**
//     * 获取注释对象Id。
//     * 如果没有注释接口Id侧返回类名称作为对象Id的返回。
//     *
//     * @param o
//     * @return
//     */
//    public static String getKey(Object o) {
//        try {
//            if (o.getClass().isAnnotationPresent(Key.class)) {
//                return o.getClass().getAnnotation(Key.class).value();
//            }
//        } catch (NullPointerException e) {
//        }
//        return o.getClass().getName();
//    }

    /**
     * 是否为绑定服务
     *
     * @param c
     * @return
     */
    public static boolean isBeanService(Class<?> c) {
        return !Modifier.isStatic(c.getModifiers()) && c.isAnnotationPresent(Service.class);
    }

//    /**
//     * 是否为绑定回调函数
//     *
//     * @param m
//     * @return
//     */
//    public static boolean isBeanCallMethod(Method m) {
//        return !Modifier.isStatic(m.getModifiers()) && !m.getReturnType().equals(Void.TYPE) && m.isAnnotationPresent(Call.class);
//    }


//    /**
//     * 模块绑定注释
//     *
//     * @param m
//     * @return
//     */
//    public static boolean isModuleBeanMethod(Method m) {
//        return isAnnotationMethod(m, ModuleBean.class);
//    }

//    public static boolean isModuleBeanMethodAccessible(Method m) {
//        if (isModuleBeanMethod(m)) {
//            m.setAccessible(true);
//            return true;
//        }
//        return false;
//    }
//
//    public static boolean isModuleConditionalOnMissingBeanMethodAccessible(Method m) {
//        if (isModuleConditionalOnMissingBeanMethod(m)) {
//            m.setAccessible(true);
//            return true;
//        }
//        return false;
//    }

//    private static boolean isModuleConditionalOnMissingBeanMethod(Method m) {
//        return isAnnotationMethod(m, ModuleConditionalOnMissingBean.class);
//    }

    /**
     * @param m
     * @return
     */
    public static boolean isConditionalOnMissingBeanMethodAccessible(Method m) {
        if (isConditionalOnMissingBeanMethod(m)) {
            m.setAccessible(true);
            return true;
        }
        return false;
    }

    /**
     * @param m
     * @return
     */
    public static boolean isConditionalOnMissingBeanMethod(Method m) {
        return isAnnotationMethod(m, ConditionalOnMissingBean.class);
    }

    /**
     * 判断函数是否指定注释
     *
     * @param m
     * @param a
     * @return
     */
    public static boolean isAnnotationMethod(Method m, Class<? extends Annotation> a) {
        return !Modifier.isStatic(m.getModifiers()) && m.isAnnotationPresent(a);
    }
//    public static <T, B extends IBeanDefinition> T getTypeBean(Map<String, B> map, Type depend) {
//        synchronized (map) {
//            for (Map.Entry<String, B> entry : map.entrySet()) {
//                Object o = entry.getOrderValue().object;
//                //比较子类
//                if (o.getClass().isInstance(depend)) {
//                    return (T) entry.getOrderValue().object;
//                }
//            }
//        }
//        return null;
//    }

    /**
     * 获取绑定对象
     *
     * @param beanSource 绑定对象源
     * @param map        绑定定义列表
     * @param name       绑定定义类名称
     * @return
     * @throws IllegalArgumentException
     */
    public static <B extends IBeanDefinition> Object getBeanObject(Object beanSource, Map<String, B> map, String name) throws IllegalArgumentException {
        synchronized (map) {
            for (Map.Entry<String, B> entry : map.entrySet()) {
                if (entry.getKey().equals(name)) {
                    return entry.getValue().getObject();
                }
            }
        }
        //未找到绑定对象，参数无效错误！
        if (beanSource == null) {
            throw new IllegalArgumentException("getBeanObject->value:" + name);
        }
        throw new IllegalArgumentException(beanSource.getClass().getName() + "->getBeanObject->value:" + name);
    }

    /**
     * 使用绑定名称获取邦迪定义对象
     *
     * @param beanMap 绑定列表
     * @param name    绑定名称
     * @return
     */
    public static IBeanDefinition getNameBeanDefinition(Map<String, IBeanDefinition> beanMap, String name) {
        synchronized (beanMap) {
            return beanMap.get(name);
        }
    }

    /**
     * 获取绑定定义
     *
     * @param beanMap 绑定地图
     * @param name    绑定键定义
     * @param loader  指定类加载器
     * @return
     */
    public static IBeanDefinition getBeanDefinition(Map<String, IBeanDefinition> beanMap, String name, ClassLoader loader) {
        synchronized (beanMap) {
            if (beanMap.containsKey(name)) {
                return beanMap.get(name);
            }
            //如果注入的是接口时判断是否有该接口继承的绑定对象
            try {
                Class<?> c = Class.forName(name, false, loader);
                for (Map.Entry<String, IBeanDefinition> entry : beanMap.entrySet()) {
                    if (c.isAssignableFrom(entry.getValue().getObject().getClass())) {
                        return entry.getValue();
                    }
                }
            } catch (ClassNotFoundException e) {
                throw new BeanException(e.getMessage(), e);
            }
            return null;
        }
    }

//    /**
//     * 获取绑定定义
//     *
//     * @param beanMap 绑定地图
//     * @param value     绑定键定义
//     * @param loader  指定类加载器
//     * @return
//     */
//    public static IBeanDefinition getBeanDefinition(ConcurrentSkipListMap<String, IBeanDefinition> beanMap, BeanDefinitionKey value, ClassLoader loader) {
//        return getBeanDefinition(beanMap, value.getName(), loader);
//    }

    /**
     * 指定注释获取绑定对象
     *
     * @param beanMap 绑定地图
     * @param a       注释类型
     * @return
     */
    public static <T> T getAnnotationBean(Map<String, IBeanDefinition> beanMap, Class<? extends Annotation> a) {
        synchronized (beanMap) {
            for (Map.Entry<String, IBeanDefinition> entry : beanMap.entrySet()) {
                if (entry.getValue().getObject().getClass().isAnnotationPresent(a)) {
                    return (T) entry.getValue().getObject();
                }
            }
            return null;
        }
    }

    /**
     * 指定注释获取绑定对象列表
     *
     * @param beanMap 绑定地图
     * @param a       注释类型
     * @param <T>
     * @return
     */
    public static <T> List<T> getAnnotationBeanList(Map<String, IBeanDefinition> beanMap, Class<? extends Annotation> a) {
        List<T> list = new ArrayList<>();
        synchronized (beanMap) {
            for (Map.Entry<String, IBeanDefinition> entry : beanMap.entrySet()) {
                if (entry.getValue().getObject().getClass().isAnnotationPresent(a)) {
                    list.add((T) entry.getValue().getObject());
                }
            }
        }
        return list;
    }
//
//    /**
//     * 删除绑定定义dui
//     *
//     * @param beanMap
//     * @param name
//     * @return
//     */
//    public static IBeanDefinition removeBean(Map<String, IBeanDefinition> beanMap, String name) {
//        IBeanDefinition definition = null;
//        synchronized (beanMap) {
//            for (Map.Entry<String, IBeanDefinition> entry : beanMap.entrySet()) {
//                //判断是否有要删除的绑定定义对象
//                if (entry.getKey().equals(name)) {
//                    //找到需要删除的绑定定义对象
//                    definition = entry.getOrderValue();
//                    break;
//                }
//            }
//        }
//        //判断是否有需要删除的定义对象
//        if (definition != null) {
//            synchronized (beanMap) {
//                beanMap.remove(name);
//            }
//            return definition;
//        }
//        return null;
//    }

    /**
     * 删除绑定定义对象
     *
     * @param beanMap    绑定地图
     * @param definition 要删除的定义
     * @return 返回删除后的绑定定义对象
     */
    public static IBeanDefinition removeBean(Map<String, IBeanDefinition> beanMap, IBeanDefinition definition) {
        boolean isDefinition = false;
        synchronized (beanMap) {
            for (Map.Entry<String, IBeanDefinition> entry : beanMap.entrySet()) {
                //判断是否有要删除的绑定定义对象
                if (entry.getKey().equals(definition.getName())) {
                    //找到需要删除的绑定定义对象
                    isDefinition = true;
                    break;
                }
            }
        }
        //判断是否有需要删除的定义对象
        if (isDefinition) {
            beanMap.remove(definition.getName());
            return definition;
        }
        return null;
    }

//    /**
//     * 判断注释类型是否不等于定绑定动作注释
//     *
//     * @param container 注释类型
//     * @param action    绑定注释动作
//     * @return
//     */
//    public static boolean notExistBeanListContainerAction(BeanSimpleListContainer container, Action action) {
//        return !existAnnotationBeanAction(container, action);
//    }
//
//    /**
//     * 判断注释类型是等于指定绑定动作注释
//     *
//     * @param container 注释类型
//     * @param action    绑定注释动作
//     * @return
//     */
//    public static boolean existAnnotationBeanAction(BeanSimpleListContainer container, Action action) {
//        //判断绑定动作位置验证
////        if (container.action().equals(action)) {
////            //绑定动作无效
////            return true;
////        }
//        //注释类型没有注释绑定动作或注释绑定动作动作无效
//        return false;
//    }
}