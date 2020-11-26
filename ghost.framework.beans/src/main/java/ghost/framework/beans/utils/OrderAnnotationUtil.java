package ghost.framework.beans.utils;

import ghost.framework.beans.annotation.order.Order;
import ghost.framework.beans.annotation.configuration.properties.ConfigurationProperties;
import ghost.framework.beans.annotation.configuration.ConfigurationClassProperties;
import ghost.framework.beans.configuration.environment.annotation.EnvironmentProperties;
import ghost.framework.beans.configuration.environment.bind.annotation.BindEnvironmentProperties;
import ghost.framework.util.ReflectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:注释排序工具
 * @Date: 23:07 2019-01-24
 */
public final class OrderAnnotationUtil {
    /**
     * @param properties
     * @return
     */
    public static List<BindEnvironmentProperties> getAnnotationBindEnvironmentPropertiesListSort(BindEnvironmentProperties[] properties) {
        List<BindEnvironmentProperties> list = new ArrayList<>();
        for (BindEnvironmentProperties p : properties) {
            list.add(p);
        }
        list.sort(new Comparator<BindEnvironmentProperties>() {
            @Override
            public int compare(BindEnvironmentProperties a, BindEnvironmentProperties b) {
                return a.order() - b.order();
            }
        });
        return list;
    }

    /**
     * @param properties
     * @return
     */
    public static List<EnvironmentProperties> getAnnotationEnvironmentPropertiesListSort(EnvironmentProperties[] properties) {
        List<EnvironmentProperties> list = new ArrayList<>();
        for (EnvironmentProperties p : properties) {
            list.add(p);
        }
        list.sort(new Comparator<EnvironmentProperties>() {
            @Override
            public int compare(EnvironmentProperties a, EnvironmentProperties b) {
                return a.order() - b.order();
            }
        });
        return list;
    }

    /**
     * @param properties
     * @return
     */
    public static List<ConfigurationClassProperties> getAnnotationClassPropertiesListSort(ConfigurationClassProperties[] properties) {
        List<ConfigurationClassProperties> list = new ArrayList<>();
        for (ConfigurationClassProperties p : properties) {
            list.add(p);
        }
        list.sort(new Comparator<ConfigurationClassProperties>() {
            @Override
            public int compare(ConfigurationClassProperties a, ConfigurationClassProperties b) {
                return a.order() - b.order();
            }
        });
        return list;
    }

    /**
     * 获取注释配置文件加载排序
     *
     * @param properties
     * @return
     */
    public static List<ConfigurationProperties> getAnnotationConfigurationPropertiesListSort(ConfigurationProperties[] properties) {
        List<ConfigurationProperties> list = new ArrayList<>();
        for (ConfigurationProperties p : properties) {
            list.add(p);
        }
        list.sort(new Comparator<ConfigurationProperties>() {
            @Override
            public int compare(ConfigurationProperties a, ConfigurationProperties b) {
                return a.order() - b.order();
            }
        });
        return list;
    }

//    /**
//     * 升序排序，从小到大排序
//     * 绑定函数按照 {@link Order} 注释位置排序，如果没有注释默认值为0
//     *
//     * @param list 绑定函数列表
//     */
//    public static void beanMainMethodListSort(List<BeanMainMethod> list) {
//        list.sort(new Comparator<BeanMainMethod>() {
//            @Override
//            public int compare(BeanMainMethod a, BeanMainMethod b) {
//                return a.getOrder() - b.getOrder();
//            }
//        });
//    }


    public static void methodListOrderSort(List<Method> list) {
        list.sort(new Comparator<Method>() {
            @Override
            public int compare(Method a, Method b) {
                int aa = 0;
                int bb = 0;
                if (a.isAnnotationPresent(Order.class)) {
                    aa = a.getAnnotation(Order.class).value();
                }
                if (b.isAnnotationPresent(Order.class)) {
                    bb = b.getAnnotation(Order.class).value();
                }
                return aa - bb;
            }
        });
    }

    /**
     * 获取注释函数列表
     * 并且按照 Order 注释的位置进行排序返回
     * 默认最小值排在最前位置
     *
     * @param o 要获取注释函数列表的对象
     * @param a 指定注释
     * @return
     */
    public static List<Method> getAnnotationMethodListSort(Object o, Class<? extends Annotation> a) {
        return getAnnotationMethodListSort(o.getClass(), a);
    }

    /**
     * 获取注释函数列表
     * 并且按照 Order 注释的位置进行排序返回
     * 默认最小值排在最前位置
     *
     * @param c 要获取注释函数列表的类型
     * @param a 指定注释
     * @return
     */
    public static List<Method> getAnnotationMethodListSort(Class<?> c, Class<? extends Annotation> a) {
        List<Method> list = ReflectUtil.getAnnotationMethods(c, a);
        methodListSort(list);
        return list;
    }

    /**
     * 排列类从小开始到大开始位置列表。
     * 获取注释数组类型的{@link Order}注释排序按照降序排列
     *
     * @param values
     * @return
     */
    public static List<Class<?>> classListSort(Class<?>[] values) {
        List<Class<?>> list = new ArrayList();
        for (Class<?> o : values) {
            list.add(o);
        }
        classListSort(list);
        return list;
    }

    /**
     * 排列类从小开始到大开始位置列表。
     * 如果有Order注释使用Order注释值排序，如果没有Order注释按照默认0排序
     * 获取注释数组类型的{@link Order}注释排序按照降序排列
     *
     * @param list
     */
    public static void classListSort(List<Class<?>> list) {
        list.sort(new Comparator<Class<?>>() {
            @Override
            public int compare(Class<?> a, Class<?> b) {
                return getClassValue(a) - getClassValue(b);
            }
        });
    }

    /**
     * 排列对象从小开始到大开始位置列表。
     *
     * @param values
     * @return
     */
    public static List<Method> methodListSort(Method[] values) {
        List<Method> list = new ArrayList();
        for (Method o : values) {
            list.add(o);
        }
        methodListSort(list);
        return list;
    }

    /**
     * 排列对象从小开始到大开始位置列表。
     *
     * @param list
     * @return
     */
    public static List<Method> methodListSort(List<Method> list) {
        list.sort(new Comparator<Method>() {
            @Override
            public int compare(Method a, Method b) {
                return getMethodValue(a) - getMethodValue(b);
            }
        });
        return list;
    }

    /**
     * 获取不可变的事件接口列表
     *
     * @param list
     * @return
     */
    public static final <T> List<T> copyListSort(List<T> list) {
        synchronized (list) {
            //获取有排序注释的优先执行，排序注释有由小到大
            ArrayList arrayList = new ArrayList<>(list);
            methodListSort(arrayList);
            return arrayList;
        }
    }

    /**
     * 获取函数排序注释值
     *
     * @param method 函数
     * @return
     */
    public static int getMethodValue(Method method) {
        //判断是否有函数排序注释
        if (method.isAnnotationPresent(Order.class)) {
            //获取函数排序注释值
            return method.getAnnotation(Order.class).value();
        }
        //没有函数排序注释的使用默认值0的排序值
        return 0;
    }

    /**
     * 获取类注释位置
     *
     * @param c 类
     * @return
     */
    public static int getClassValue(Class<?> c) {
        if (c.isAnnotationPresent(Order.class)) {
            return c.getAnnotation(Order.class).value();
        }
        return 0;
    }

    /**
     * 注释值的对象排序
     *
     * @param list
     */
    public static <T> void listSort(List<T> list) {
        list.sort(new Comparator<T>() {
            @Override
            public int compare(T a, T b) {
                return getOrderValue(a) - getOrderValue(b);
            }
        });
    }

    /**
     * 获取对象注释排序值
     *
     * @param o 对象
     * @return
     */
    public static int getOrderValue(Object o) {
        if (o instanceof Class) {
            Class<?> c = (Class<?>) o;
            //判断是否有函数排序注释
            if (c.isAnnotationPresent(Order.class)) {
                //获取函数排序注释值
                return c.getDeclaredAnnotation(Order.class).value();
            }
            return 0;
        }
        //判断是否有函数排序注释
        if (o.getClass().isAnnotationPresent(Order.class)) {
            //获取函数排序注释值
            return o.getClass().getDeclaredAnnotation(Order.class).value();
        }
        //没有函数排序注释的使用默认值0的排序值
        return 0;
    }

    /**
     * 注释对象排序
     *
     * @param values
     * @return
     */
    public static <T> List<T> listSort(T[] values) {
        List<T> list = new ArrayList();
        for (T o : values) {
            list.add(o);
        }
        listSort(list);
        return list;
    }

    /**
     * @param set
     * @param <T>
     */
    public static <T> void setSort(Set<T> set) {
        List<T> list = new ArrayList<>(set);
        listSort(list);
        set.clear();
        set.addAll(list);
    }

    /**
     * map排序
     *
     * @param map
     * @param <K>
     * @param <V>
     */
    public static <K, V> void mapValueSort(Map<K, V> map) {
        //拿到map的键值对集合
        Set<Map.Entry<K, V>> set = map.entrySet();
        //将这个set集合转为list集合为了使用工具类的排序方法
        List<Map.Entry<K, V>> list = new ArrayList<>(set);
        //排序
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
                    @Override
                    public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                        return getOrderValue(o1.getValue()) - getOrderValue(o2.getValue());  //降序排序，如果要升序就用前面减后面
                    }
                }
        );
        //重新装入LinkedHashMap中
        LinkedHashMap<K, V> linkedHashMap = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            linkedHashMap.put(entry.getKey(), entry.getValue());
        }
        map.clear();
        linkedHashMap.forEach(map::put);
    }
}