package ghost.framework.reflect;

import ghost.framework.reflect.annotations.Key;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:反射工具类
 * @Date: 20:20 2019/5/13
 */
public final class ReflectUtil {
    public static final String packageInfo = "package-info";
    public static final String packageInfoClass = "package-info.class";
    public static final String DotJAR = ".jar";
    public static final String Meta_Inf_Name = "META-INF";
    public static final String Manifest_Name = "MANIFEST.MF";
    public static final String DotClass = ".class";
    /**
     * 获取包的版本信息
     * @param jar 包文件
     * @return
     * @throws IOException
     */
    public static Manifest getManifest(File jar) throws IOException {
        Manifest m = null;
        try (JarFile j = new JarFile(jar)) {
            m = j.getManifest();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new IOException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        return m;
    }
    /**
     * 获取指定注释的函数列表
     * @param c
     * @param a
     * @return
     */
    public static List<Method> getAnnotationMethods(Class<?> c, Class<? extends Annotation> a) {
        List<Method> list = new ArrayList<>();
        //遍历函数
        for (Method m : c.getDeclaredMethods()) {
            //判断是否注释与不为静态的函数
            if (!Modifier.isStatic(m.getModifiers()) && m.isAnnotationPresent(a)) {
                list.add(m);
            }
        }
        return list;
    }
    /**
     * 获取函数
     * 没有返回参数的函数
     * @param c
     * @return
     */
    public static Method getAnnotationVoidMethod(Class<?> c, Class<? extends Annotation> a) {
        //遍历函数
        for (Method m : c.getDeclaredMethods()) {
            //判断启动注释
            if (!Modifier.isStatic(m.getModifiers()) && m.isAnnotationPresent(a) && m.getReturnType().equals(Void.TYPE)) {
                return m;
            }
        }
        return null;
    }

    /**
     * 将类名称转换为类路径
     * 比喻java.lang.annotation.Annotation转java/lang/annotation/Annotation.class
     *
     * @param name
     * @return
     */
    public static String toClassResourcePath(String name) {
        return StringUtils.replace(name, ".", "/") + DotClass;
    }

    /**
     * 获取指定注释函数
     *
     * @param c
     * @return
     */
    public static Method getAnnotationMethod(Class<?> c, Class<? extends Annotation> a) {
        //遍历函数
        for (Method m : c.getDeclaredMethods()) {
            //判断启动注释
            if (c.isAnnotationPresent(a)) {
                return m;
            }
        }
        return null;
    }

    /**
     * 获取类型注释列表
     *
     * @param c
     * @param a
     * @param <T>
     * @return
     */
    public static <T extends Annotation> List<T> getAnnotationsByType(Class<?> c, Class<T> a) {
        List<T> list = new ArrayList<>();
        T[] ts = c.getDeclaredAnnotationsByType(a);
        if (ts != null) {
            for (T t : ts) {
                list.add(t);
            }
        }
        T[] ts1 = c.getAnnotationsByType(a);
        if (ts1 != null) {
            for (T t : ts1) {
                list.add(t);
            }
        }
        return list;
    }

    /**
     * 设置对象属性值
     *
     * @param o 对象
     * @param n 属性名称
     * @param v 属性值
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static void setField(Object o, String n, Object v) throws IllegalArgumentException, IllegalAccessException {
        Field field = findField(o.getClass(), n);
        field.setAccessible(true);
        field.set(o, v);
    }

    /**
     * 设置对象属性值
     *
     * @param o 对象
     * @param f 属性
     * @param v 属性值
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static void setField(Object o, Field f, Object v) throws IllegalArgumentException, IllegalAccessException {
        f.setAccessible(true);
        f.set(o, v);
    }

    /**
     * 循环向上转型, 获取对象的 DeclaredField
     *
     * @param c         : 子类对象
     * @param fieldName : 父类中的属性名
     * @return 父类中的属性对象
     */
    public static Field findField(Class<?> c, String fieldName) {
        Field field = null;
        for (; c != Object.class; c = c.getSuperclass()) {
            try {
                field = c.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (Exception e) {
                //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
                //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了
            }
        }
        return null;
    }

    /**
     * 获取对象属性的值
     *
     * @param o    对象
     * @param name 属性名称
     * @param <T>  返回类型
     * @return
     */
    public static <T> T findField(Object o, String name) {
        try {
            return (T) findField(o.getClass(), name).get(o);
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
        return null;
    }

    /**
     * 获取包Id。
     *
     * @param packageName
     * @return
     */
    public static String getPackageId(String packageName) {
        return Package.getPackage(packageName).getAnnotation(Key.class).value();
    }

    /**
     * 该类是否集成某个接口。
     *
     * @param c 指定类。
     * @param i 指定该类是否集成该接口。
     * @return
     */
    public static boolean isInterface(Class<?> c, Class<?> i) {
        Class[] interfaces = c.getInterfaces();
        for (Class<?> ci : interfaces) {
            if (ci == i) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取注释对象Id。
     * 如果没有注释接口Id侧返回类名称作为对象Id的返回。
     *
     * @param o
     * @return
     */
    public static String getId(Object o) {
        try {
            if (o.getClass().isAnnotationPresent(Key.class)) {
                return o.getClass().getAnnotation(Key.class).value();
            }
        } catch (NullPointerException e) {
        }
        return o.getClass().getName();
    }

    /**
     * 获取注释Id。
     * 如果没有获取到接口Id注释侧返回null。
     *
     * @param o
     * @return
     */
//    @SuppressWarnings("unchecked")
    public static String getAnnotationId(Object o) {
        try {
            if (o.getClass().isAnnotationPresent(Key.class)) {
                return o.getClass().getAnnotation(Key.class).value();
            }
        } catch (NullPointerException e) {
        }
        return null;
    }

    /**
     * 获取类型Id。
     * 如果类型没有注释Id侧返回类名称。
     *
     * @param c
     * @return
     */
    public static String getClassId(Class<?> c) {
        try {
            if (c.isAnnotationPresent(Key.class)) {
                return c.getAnnotation(Key.class).value();
            }
        } catch (NullPointerException e) {
        }
        return c.getName();
    }

    /**
     * 执行函数
     *
     * @param method
     * @param parameters
     * @return
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Object commandMethod(Method method, Object[] parameters) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (method.getReturnType().equals(Void.TYPE)) {
            method.invoke(null, parameters);
            return null;
        } else {
            return method.invoke(null, parameters);
        }
    }

    /**
     * 获取类的函数
     *
     * @param c    类型
     * @param name 函数名称
     * @return
     */
    public static List<Method> getClassMethodList(Class<?> c, String name) {
        List<Method> methodList = new ArrayList<>();
        for (Method method : c.getDeclaredMethods()) {
            if (method.getName().equals(name)) {
                methodList.add(method);
            }
        }
        return methodList;
    }

    /**
     * 获取类排除静态的全部属性列表，按照java获取声明标准
     *
     * @param aClass
     * @return
     */
    public static List<Field> getNoStaticFields(Class<?> aClass) {
        //获得某个类的所有的公共（public）的字段，包括父类中的字段。
        List<Field> list = new ArrayList<>();
        for (Field f : aClass.getFields()) {
            if (!Modifier.isStatic(f.getModifiers())) {
                list.add(f);
            }
        }
        //获得某个类的所有声明的字段，即包括public、private和proteced，但是不包括父类的申明字段。
        for (Field f : aClass.getDeclaredFields()) {
            if (!list.contains(f)) {
                if (!Modifier.isStatic(f.getModifiers())) {
                    list.add(f);
                }
            }
        }
        return list;
    }

    /**
     * 设置属性值
     *
     * @param obj   对象
     * @param field 属性
     * @param v     值
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws NumberFormatException
     */
    public static void setFieldValue(Object obj, Field field, Object v) throws IllegalArgumentException, IllegalAccessException, NumberFormatException {
        field.setAccessible(true);
//        if (field.getType() == byte.class) {
//            field.setByte(obj, objectConverter(v, field.getGenericType()));
//            return;
//        }
//        if (field.getType() == Byte.class) {
//            field.setByte(obj, (Byte) v);
//            return;
//        }
//        if (field.getType().equals(short.class)) {
//            field.setShort(obj, (short) v);
//            return;
//        }
//        if (field.getType().equals(Short.class)) {
//            field.setShort(obj, (Short) v);
//            return;
//        }
//        if (field.getType().equals(int.class)) {
//            field.setInt(obj, (int) v);
//            return;
//        }
//        if (field.getType().equals(Integer.class)) {
//            field.setInt(obj, (Integer) v);
//            return;
//        }
//        if (field.getType().equals(long.class)) {
//            field.setLong(obj, (long) v);
//            return;
//        }
//        if (field.getType().equals(Long.class)) {
//            field.setLong(obj, (Long) v);
//            return;
//        }
//        if (field.getType().equals(boolean.class)) {
//            field.setBoolean(obj, (boolean) v);
//            return;
//        }
//        if (field.getType().equals(Boolean.class)) {
//            field.setBoolean(obj, (Boolean) v);
//            return;
//        }
//        if (field.getType().equals(float.class)) {
//            field.setFloat(obj, (float) v);
//            return;
//        }
//        if (field.getType().equals(Float.class)) {
//            field.setFloat(obj, (Float) v);
//            return;
//        }
//        if (field.getType().equals(double.class)) {
//            field.setDouble(obj, (double) v);
//            return;
//        }
//        if (field.getType().equals(Double.class)) {
//            field.setDouble(obj, (Double) v);
//            return;
//        }
//        if (field.getType().equals(BigInteger.class)) {
//            field.set(obj, BigInteger.valueOf((long) v));
//            return;
//        }
//        if (field.getType().equals(BigDecimal.class)) {
//            field.set(obj, BigDecimal.valueOf((double) v));
//            return;
//        }
//        if (field.getType().equals(char.class)) {
//            field.setChar(obj, v.toString().charAt(0));
//            return;
//        }
//        if (field.getType().equals(Date.class)) {
//            field.set(obj, v);
//            return;
//        }
        field.set(obj, objectConverter(v, field.getGenericType()));
    }

    /**
     * 是否为基础类型
     *
     * @param type
     * @return
     */
    public static boolean isBasicType(Type type) {
        if (type == boolean.class || type == Boolean.class) {
            return true;
        }
        if (type == byte.class || type == Byte.class) {
            return true;
        }
        if (type == short.class || type == Short.class) {
            return true;
        }
        if (type == int.class || type == Integer.class) {
            return true;
        }
        if (type == float.class || type == Float.class) {
            return true;
        }
        if (type == double.class || type == Double.class) {
            return true;
        }
        if (type == char.class) {
            return true;
        }
        if (type == long.class || type == Long.class) {
            return true;
        }
        if (type == Date.class) {
            return true;
        }
        if (type == String.class) {
            return true;
        }
        if (type == BigInteger.class) {
            return true;
        }
        if (type == BigDecimal.class) {
            return true;
        }
        if (type == java.sql.Date.class) {
            return true;
        }
        if (type == java.sql.Time.class) {
            return true;
        }
        if (type == java.sql.Timestamp.class) {
            return true;
        }
        return false;
    }

    /**
     * 对象转换器
     *
     * @param o
     * @param type
     * @return
     */
    public static Object objectConverter(Object o, Type type) {
        if (type == boolean.class || type == Boolean.class) {
            if (o == null) {
                return 0;
            }
            return Boolean.valueOf(o.toString());
        }
        if (type == byte.class || type == Byte.class) {
            if (o == null) {
                return 0;
            }
            return Byte.valueOf(o.toString());
        }
        if (type == short.class || type == Short.class) {
            if (o == null) {
                return 0;
            }
            return Short.valueOf(o.toString());
        }
        if (type == int.class || type == Integer.class) {
            if (o == null) {
                return 0;
            }
            return Integer.valueOf(o.toString());
        }
        if (type == float.class || type == Float.class) {
            if (o == null) {
                return 0;
            }
            return Float.valueOf(o.toString());
        }
        if (type == double.class || type == Double.class) {
            if (o == null) {
                return 0;
            }
            return Double.valueOf(o.toString());
        }
        if (type == char.class) {
            if (o == null) {
                return null;
            }
            return o.toString().charAt(0);
        }
        if (type == long.class || type == Long.class) {
            if (o == null) {
                return 0;
            }
            return Long.valueOf(o.toString());
        }
        if (type == Date.class) {
            if (o == null) {
                return null;
            }
            return new Date(o.toString());
        }
        if (type == String.class) {
            if (o == null) {
                return null;
            }
            return o.toString();
        }
        if (type == BigInteger.class) {
            if (o == null) {
                return 0;
            }
            return BigInteger.valueOf(Long.valueOf(o.toString()));
        }
        if (type == BigDecimal.class) {
            if (o == null) {
                return 0;
            }
            return BigDecimal.valueOf(Long.valueOf(o.toString()));
        }
        if (type == java.sql.Date.class) {
            if (o == null) {
                return null;
            }
            return new java.sql.Date(new Date(o.toString()).getTime());
        }
        if (type == java.sql.Time.class) {
            if (o == null) {
                return null;
            }
            return new java.sql.Time(new Date(o.toString()).getTime());
        }
        if (type == java.sql.Timestamp.class) {
            if (o == null) {
                return null;
            }
            return new java.sql.Timestamp(new Date(o.toString()).getTime());
        }
        return o;
    }

    /**
     * 默认类型空与默认值
     *
     * @param type
     * @return
     * @throws IllegalArgumentException
     */
    public static Object basicTypeNullOrDefaultValue(Type type) throws IllegalArgumentException {
        if (type == boolean.class || type == Boolean.class) {
            return false;
        }
        if (type == byte.class || type == Byte.class) {
            return 0;
        }
        if (type == short.class || type == Short.class) {
            return 0;
        }
        if (type == int.class || type == Integer.class) {
            return 0;
        }
        if (type == float.class || type == Float.class) {
            return 0;
        }
        if (type == double.class || type == Double.class) {
            return 0;
        }
        if (type == char.class) {
            return null;
        }
        if (type == long.class || type == Long.class) {
            return 0;
        }
        if (type == Date.class) {
            return null;
        }
        if (type == String.class) {
            return null;
        }
        if (type == BigInteger.class) {
            return 0;
        }
        if (type == BigDecimal.class) {
            return 0;
        }
        if (type == java.sql.Date.class) {
            return null;
        }
        if (type == java.sql.Time.class) {
            return null;
        }
        if (type == java.sql.Timestamp.class) {
            return null;
        }
        throw new IllegalArgumentException();
    }

    /**
     * 获取类指定静态函数
     *
     * @param c    类
     * @param name 指定函数名称
     * @return
     */
    public static List<Method> getClassStaticMethod(Class<?> c, String name) {
        List<Method> list = new ArrayList<>();
        for (Method method : c.getDeclaredMethods()) {
            if (method.getName().equals(name) && Modifier.isStatic(method.getModifiers())) {
                list.add(method);
            }
        }
        return list;
    }

    /**
     * 设置函数值
     *
     * @param o 调用函数对象
     * @param n 调用删除名称
     * @param v 设置函数值
     * @return
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Object setMethod(Object o, String n, Object v) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return setMethod(o, n, new Object[]{v});
    }

    /**
     * 设置函数值
     *
     * @param o 调用函数对象
     * @param n 调用删除名称
     * @param v 设置函数值
     * @return
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Object setMethod(Object o, String n, Object[] v) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        for (Method method : o.getClass().getDeclaredMethods()) {
            if (method.getName().equals(n) && !Modifier.isStatic(method.getModifiers())) {
                method.setAccessible(true);
                return method.invoke(o, v);
            }
        }
        throw new IllegalArgumentException(n);
    }

    /**
     * 创建包对象
     *
     * @param n 包名称
     * @param m 包版本
     * @param u 包文件
     * @param l 包所在的类加载器
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static Package newInstancePackage(String n, Manifest m, URL u, ClassLoader l) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Package pack = null;
        try {
            //获取包初始化列表
            Constructor[] cs = Package.class.getDeclaredConstructors();
            //遍历初始化列表
            for (Constructor c : cs) {
                //判断参数是否正确
                if (c.getParameterCount() == 4) {
                    //找到对的初始化函数
                    c.setAccessible(true);
                    pack = (Package) c.newInstance(new Object[]{n, m, u, l});
                    break;
                }
            }
        } catch (InvocationTargetException e) {
            e.addSuppressed(new Throwable(n));
            e.printStackTrace();
            throw e;
        }
        return pack;
    }

    /**
     * 比较两个字符串是否想等
     * 不忽略大小写
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean stringEquals(String a, String b) {
        if (a == null && b == null) return true;
        if (a == null && b != null) return false;
        if (b == null && a != null) return false;
        if (a.equals(b)) return true;
        return false;
    }

    /**
     * 比较两个字符串是否想等
     * 忽略大小写
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean stringEqualsIgnoreCase(String a, String b) {
        if (a == null && b == null) return true;
        if (a == null && b != null) return false;
        if (b == null && a != null) return false;
        if (a.equalsIgnoreCase(b)) return true;
        return false;
    }

    /**
     * 获取类所属把名称
     *
     * @param name 类名称
     * @return
     */
    public static String getClassPackageName(String name) {
        String[] strings = StringUtils.split(name, ".");
        String s = "";
        int n = 0;
        for (String i : strings) {
            if (s.equals("")) {
                s = i;
            } else {
                s += ("." + i);
            }
            n++;
            //判断是否为.class后缀
            if (name.endsWith(DotClass)) {
                //.class后缀
                if (n == strings.length - 2) {
                    break;
                }
            } else {
                //没有.class后缀
                if (n == strings.length - 1) {
                    break;
                }
            }
        }
        return s;
  }

    /**
     * 获取类名
     *
     * @param name 类全名
     * @return
     */
    public static String getClassName(String name) {
        //判断是否为.class后缀
        if (name.endsWith(DotClass)) {
            //.class后缀
            return name.replaceAll("/", ".").replaceAll("\\\\", ".").substring(0, name.length() - DotClass.length());
        }
        //没有.class后缀
        return name.replaceAll("/", ".").replaceAll("\\\\", ".");
    }

    /**
     * 获取类名
     *
     * @param name 类短名称
     * @return
     */
    public static String getClassSimpleName(String name) {
        String[] strings = StringUtils.split(name.replaceAll("/", ".").replaceAll("\\\\", "."), ".");
        //判断是否为.class后缀
        if (name.endsWith(DotClass)) {
            //.class后缀
            return strings[strings.length - 2];
        } else {
            //没有.class后缀
            return strings[strings.length - 1];
        }
    }

    /**
     * 获取函数值
     *
     * @param o   对象
     * @param n   函数名称
     * @param v   函数值
     * @param <T> 返回类型
     * @return
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static <T> T getMethod(Object o, String n, Object v) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        for (Method method : o.getClass().getDeclaredMethods()) {
            if (method.getName().equals(n) && !Modifier.isStatic(method.getModifiers())) {
                method.setAccessible(true);
                Object r = method.invoke(o, v);
                if (r == null) {
                    break;
                } else {
                    return (T) r;
                }
            }
        }
        return null;
    }

    public static Object setMethod(Object o, String n) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return setMethod(o, n, null);
    }

    /**
     * 设置包版本
     *
     * @param url
     * @param m
     */
    public static void setManifest(URL url, Manifest m) {
        String[] strings = StringUtils.split(url.getPath(), "/");
        String v = strings[strings.length - 1];
        v = v.substring(0, v.length() - DotJAR.length());
        strings = StringUtils.split(v, "-");
        m.getMainAttributes().put(Attributes.Name.IMPLEMENTATION_VENDOR, strings[0]);
        m.getMainAttributes().put(Attributes.Name.IMPLEMENTATION_VERSION, strings[1]);
    }

    /**
     * 比对两个对象是否相等
     *
     * @param a 对象a
     * @param b 对象b
     * @return
     */
    public static boolean objectEquals(Object a, Object b) {
        if (a == null && b == null) return true;
        if (a != null && b == null) return false;
        if (a == null && b != null) return true;
        if (a.equals(b)) return true;
        return false;
    }

    /**
     * 获取类全名路径
     *
     * @param name 类全名
     * @return
     */
    public static String getClassNamePath(String name) {
        return StringUtils.replace(name, ".", "/") + DotClass;
    }

//
//    public static <I> I getInterfaceObject(Object owner, I i) {
//        Type[] interfaces = owner.getClass().getGenericInterfaces();
//        Object io = owner;
//        for (Type t : interfaces) {
//            if (t.getClass() == i) {
//                return (I) io;
//            } else {
//                io = (t)owner;
//            }
//        }
//        return null;
//    }
}
