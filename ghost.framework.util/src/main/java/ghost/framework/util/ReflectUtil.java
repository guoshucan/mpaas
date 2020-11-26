package ghost.framework.util;

import ghost.framework.util.reflect.AnnotationMethod;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.*;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:反射工具类
 * @Date: 20:20 2019/5/13
 */
public final class ReflectUtil {
    /**
     * boot-inf目录
     */
    public final static String BOOT_INF = "BOOT-INF/";
    /**
     * boot-inf/lib目录
     */
    public final static String BOOT_INF_LIB = BOOT_INF + "lib/";
    /**
     *
     */
    public final static String JSON = ".json";
    public static final String PackageInfo = "package-info";
    public static final String DotPackageInfo = ".package-info";
    public static final String PackageInfoClass = "package-info.class";
    public static final String DotPackageInfoClass = ".package-info.class";
    public static final String DotJAR = ".jar";
    public static final String DotPom = ".pom";
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
	 * Determine if the supplied class is an <em>inner class</em>,
	 * i.e. a non-static member of an enclosing class.
	 * @return {@code true} if the supplied class is an inner class
	 * @since 5.0.5
	 * @see Class#isMemberClass()
	 */
	public static boolean isInnerClass(Class<?> clazz) {
		return (clazz.isMemberClass() && !Modifier.isStatic(clazz.getModifiers()));
	}
    /** The package separator character: {@code '.'}. */
    private static final char PACKAGE_SEPARATOR = '.';
    /**
     * Determine the name of the class file, relative to the containing
     * package: e.g. "String.class"
     * @param clazz the class
     * @return the file name of the ".class" file
     */
    public static String getClassFileName(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        return clazz.getSimpleName() + DotClass;
    }
//    /**
//     * 获取类型全部注释
//     * 不获取基础类注释
//     * @param target
//     * @return
//     */
//    public static final Map<Class<? extends Annotation>, Annotation> getClassAnnotations(Class target) {
//        try {
//            target.getDeclaredAnnotations();//伪获取，让类型把全部注释加载到内存
//            Field annotationDataField = findField(target.getClass(), "annotationData");
//            annotationDataField.setAccessible(true);
//            Object annotationData = annotationDataField.get(target);
//            Field declaredAnnotationsField = findField(annotationData.getClass(), "aAnnotations");
//            declaredAnnotationsField.setAccessible(true);
//            //返回注释列表
//            return MapUtils.copy((Map<Class<? extends Annotation>, Annotation>) declaredAnnotationsField.get(annotationData));
//        } catch (Exception e) {
//            return null;
//        }
//    }
//    /**
//     * 获取类型全部注释
//     * @param target
//     * @return
//     */
//    public static final Map<Class<? extends Annotation>, Annotation> getClassDeclaredAnnotations(Class target) {
//        try {
//            target.getDeclaredAnnotations();//伪获取，让类型把全部注释加载到内存
//            Field annotationDataField = findField(target.getClass(), "annotationData");
//            annotationDataField.setAccessible(true);
//            Object annotationData = annotationDataField.get(target);
//            Field declaredAnnotationsField = findField(annotationData.getClass(), "declaredAnnotations");
//            declaredAnnotationsField.setAccessible(true);
//            //返回注释列表
//            return MapUtils.copy((Map<Class<? extends Annotation>, Annotation>) declaredAnnotationsField.get(annotationData));
//        } catch (Exception e) {
//            return null;
//        }
//    }
    /**
     * @param parameters
     * @return
     */
    public static String getLogParameters(Parameter[] parameters) {
        StringBuffer buffer = new StringBuffer();
        for (Parameter parameter : parameters) {
            if (buffer.length() == 0) {
                buffer.append("name:" + parameter.getName() + ">class:" + parameter.getType().getSimpleName());
            } else {
                buffer.append(",");
                buffer.append("name:" + parameter.getName() + ">class:" + parameter.getType().getSimpleName());
            }
        }
        return buffer.toString();
    }
    /**
     * 判断是否为无效bean函数
     * @param method
     * @return
     */
    public static boolean invalidAnnotationMethod(Method method) {
        return Modifier.isStatic(method.getModifiers()) || method.getDeclaredAnnotations().length == 0;
    }
    /**
     * map转object
     * @param map
     * @param beanObject
     * @throws Exception
     * @return
     */
    public static Object mapToObject(Map<String, Object> map, Object beanObject)
            throws Exception {
        if (map == null)
            return null;
        org.apache.commons.beanutils.BeanUtils.populate(beanObject, map);
        return beanObject;
    }

    /**
     * object转map
     * @param obj
     * @return
     */
    public static Map<?, ?> objectToMap(Object obj) {
        if (obj == null) {
            return null;
        }
        return new org.apache.commons.beanutils.BeanMap(obj);
    }
    /**
     * 获取当前运行函数名称
     *
     * @return
     */
    public static String getExecutingMethodName() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stackTrace[2];
        return e.getMethodName();
    }

    /**
     * 调用函数
     * @param target
     * @param methodName
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    public static <T> T invokeMethod(Object target, String methodName) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Method method = findMethod(target.getClass(), methodName);
        method.setAccessible(true);
        return (T) method.invoke(target, null);
    }

    /**
     * 调用函数
     * @param target
     * @param methodName
     * @param args
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws SecurityException
     */
    public static <T> T invokeMethod(Object target, String methodName, Object ... args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException {
        Method method = findMethod(target.getClass(), methodName);
        method.setAccessible(true);
        return (T) method.invoke(target, args);
    }
    /**
     * 获取指定注释的函数列表
     *
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
     *
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
     * 获取类型全部函数
     * 包括基类的全部函数
     * @param c
     * @return
     */
    public static List<Method> getAllMethod(Class<?> c) {
        List<Method> fieldList = new ArrayList<>();
        Class<?> tc = c;
        //当父类为null的时候说明到达了最上层的父类(Object类).
        while (tc != null && !tc.getName().toLowerCase().equals("java.lang.object")) {
            fieldList.addAll(Arrays.asList(tc.getDeclaredMethods()));
            tc = tc.getSuperclass(); //得到父类,然后赋给自己
        }
        return fieldList;
    }
    /**
     * 获取类型全部字段
     * 包括基类的全部字段
     * @param c
     * @return
     */
    public static List<Field> getAllField(Class<?> c) {
        List<Field> fieldList = new ArrayList<>();
        Class<?> tc = c;
        //当父类为null的时候说明到达了最上层的父类(Object类).
        while (tc != null && !tc.getName().toLowerCase().equals("java.lang.object")) {
            fieldList.addAll(Arrays.asList(tc.getDeclaredFields()));
            tc = tc.getSuperclass(); //得到父类,然后赋给自己
        }
        return fieldList;
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
     * 获取对象类型
     * @param object
     * @return
     */
    public static Class<?> getObjectClass(Object object) {
        //获取目标对象类型
        if (object instanceof Class) {
            return (Class<?>) object;
        }
        return object.getClass();
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
     * 是否为静态声明
     * @param field
     * @return
     */
    public static boolean isStaticField(Field field){
        return Modifier.isStatic(field.getModifiers());
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
     * 默认类型默认值
     * @param type
     * @return
     * @throws IllegalArgumentException
     */
    public static Object primitiveDefaultValue(Class<?> type) throws IllegalArgumentException {
        String className = type.getName();
        if (className.equals(java.lang.Integer.class)) {
            return (int) 0;
        } else if (className.equals(java.lang.Byte.class)) {
            return (byte) 0;
        } else if (className.equals(java.lang.Long.class)) {
            return (long) 0L;
        } else if (className.equals(java.lang.Double.class)) {
            return (double) 0.0d;
        } else if (className.equals(java.lang.Float.class)) {
            return (float) 0.0f;
        } else if (className.equals(java.lang.Character.class)) {
            return (char) '\u0000';
        } else if (className.equals(java.lang.Short.class)) {
            return (short) 0;
        } else if (className.equals(java.lang.Boolean.class)) {
            return false;
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

    /**
     * 调用不返回函数
     *
     * @param o 函数拥有者对象
     * @param n 函数名称
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static void setVoidMethod(Object o, String n) throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        setVoidMethod(o, n, null);
    }

    /**
     * 调用不返回函数
     *
     * @param o 函数拥有者对象
     * @param n 函数名称
     * @param v 函数参数
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    private static void setVoidMethod(Object o, String n, Object[] v) throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        for (Method method : o.getClass().getDeclaredMethods()) {
            if (method.getName().equals(n) && !Modifier.isStatic(method.getModifiers())) {
                method.setAccessible(true);
                method.invoke(o, v);
            }
        }
        throw new IllegalArgumentException(n);
    }
    // Method handling
    private static final Class<?>[] EMPTY_CLASS_ARRAY = new Class<?>[0];
    /**
     * Attempt to find a {@link Method} on the supplied class with the supplied name
     * and no parameters. Searches all superclasses up to {@code Object}.
     * <p>Returns {@code null} if no {@link Method} can be found.
     * @param clazz the class to introspect
     * @param name the name of the method
     * @return the Method object, or {@code null} if none found
     */
    /**
     * 搜索函数对象
     * @param c 搜素函数所属类型
     * @param name 函数名称
     * @return
     */
    public static Method findMethod(Class<?> c, String name) {
        Class<?> bc = c;
        while (bc != null && !bc.getName().toLowerCase().equals("java.lang.object")) {
            for (Method method : bc.getDeclaredMethods()) {
                if (method.getName().equals(name)) {
                    return method;
                }
            }
            bc = bc.getSuperclass();
        }
        return null;
    }

    /**
     * 获取指定注释函数对象
     * @param c 查询函数类型
     * @param annotation 注释类型
     * @return
     */
    public static Method findMethod(Class<?> c, Class<? extends Annotation> annotation) {
        for (Method method : c.getDeclaredMethods()){
            if(method.isAnnotationPresent(annotation)){
                return method;
            }
        }
        return null;
    }
    /**
     * 获取指定注释函数对象
     * @param obj 查询函数类型
     * @param annotation 注释类型
     * @return
     */
    public static Object findMethod(Object obj, Class<? extends Annotation> annotation, Object[] parameters) throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        for (Method method : obj.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotation)) {
                return method.invoke(obj, parameters);
            }
        }
        return null;
    }
    /**
     * 获取类型的静态例值
     * @param c 类型
     * @param name 例名称
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static  Object findStaticFieldValue(Class<?> c, String name) throws IllegalArgumentException, IllegalAccessException {
        return findStaticField(c, name).get(c);
    }
    /**
     * 获取静态例
     * @param c 类型
     * @param name 例名称
     * @return
     */
    public static  Field findStaticField(Class<?> c, String name) {
        Field field = null;
        for (; c != Object.class; c = c.getSuperclass()) {
            try {
                field = c.getDeclaredField(name);
                if (Modifier.isStatic(field.getModifiers())) {
                    field.setAccessible(true);
                    return field;
                }
            } catch (Exception e) {
                //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
                //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了
            }
        }
        return null;
    }
    public static List<Method> getAllAnnotationMethods(Object source, Class<? extends Annotation> a) {
        return getAllAnnotationMethods(source.getClass(), a);
    }
    /**
     * 获取指定注释类型的全部函数
     * @param c 类型
     * @param a 指定要判断的注释
     * @return
     */
    public static List<Method> getAllAnnotationMethods(Class<?> c, Class<? extends Annotation> a) {
        List<Method> list = new ArrayList<>();
        Class<?> bc = c;
        while (bc != null && !bc.getName().toLowerCase().equals("java.lang.object")) {
            for (Method method : bc.getDeclaredMethods()) {
                if (method.isAnnotationPresent(a)) {
                    if (!list.contains(method)) {
                        list.add(method);
                    }
                }
            }
            bc = bc.getSuperclass();
        }
        return list;
    }

    public static boolean existsField(Class<?> c, String f) {
        return findField(c, f) != null;
    }

    /**
     * 判断类型是否在数组包中
     * @param c
     * @param packages
     * @return
     */
    public static boolean existsPackage(Class<?> c, String[] packages) {
        String name = c.getPackage().getName();
        for (String s : packages) {
            //判断是否为通配符
            if (s.endsWith(".*") && s.substring(0, s.length() - 2).startsWith(name)) {
                return true;
            } else {
                if (s.equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断类型是否在数组类型中
     * @param c
     * @param classes
     * @return
     */
    public static boolean existsClasse(Class<?> c, Class<?>[] classes) {
        for (Class<?> s : classes) {
            if (s.equals(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取类型存在指定注释的函数列表
     * @param c 要获取注释函数类型
     * @param methodAnnotations 函数数组注释类型
     * @return
     */
    public static List<AnnotationMethod> getAllAnnotationsMethods(Class<?> c, Class<? extends Annotation>[] methodAnnotations) {
        List<AnnotationMethod> list = new ArrayList<>();
        for (Class<? extends Annotation> a : methodAnnotations) {
            List<Method> l = getAllAnnotationMethods(c, a);
            for (Method method : l) {
                list.add(new AnnotationMethod(method, method.getAnnotation(a)));
            }
        }
        return list;
    }

    /**
     * 获取函数
     * @param obj
     * @param name
     * @param classes
     * @return
     */
    public static Method findMethod(Object obj, String name, Class<?> ... classes) {
        try {
            return obj.getClass().getDeclaredMethod(name, Object.class, String.class);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

//    /**
//     * 获取注释的全部注释
//     * @param target
//     * @return
//     */
//    public static Map<Class<? extends Annotation>, Annotation> getDeclaredAnnotations(Annotation target) {
//        return null;
//    }

//    /**
//     * 获取构建全部注释
//     * @param target
//     * @return
//     */
//    public static Map<Class<? extends Annotation>, Annotation> getConstructorDeclaredAnnotations(Constructor target) {
//        try {
//            target.getDeclaredAnnotations();//伪获取，让类型把全部注释加载到内存
//            Field declaredAnnotationsField = findField(target.getClass(), "declaredAnnotations");
//            declaredAnnotationsField.setAccessible(true);
//            //返回注释列表
//            return MapUtils.copy((Map<Class<? extends Annotation>, Annotation>) declaredAnnotationsField.get(target));
//        } catch (Exception e) {
//            return null;
//        }
//    }
//    /**
//     * 获取函数全部注释
//     * @param target
//     * @return
//     */
//    public static Map<Class<? extends Annotation>, Annotation> getMethodDeclaredAnnotations(Method target) {
//        try {
//            target.getDeclaredAnnotations();//伪获取，让类型把全部注释加载到内存
//            Field executableField = findField(target.getClass(), "executable");
//            executableField.setAccessible(true);
//            Executable executable = (Executable)executableField.get(target);
//            Field declaredAnnotationsField = findField(executable.getClass(), "declaredAnnotations");
//            declaredAnnotationsField.setAccessible(true);
//            //返回注释列表
//            return MapUtils.copy((Map<Class<? extends Annotation>, Annotation>) declaredAnnotationsField.get(executable));
//        } catch (Exception e) {
//            return null;
//        }
//    }

//    /**
//     * 获取声明全部注释
//     * @param target
//     * @return
//     */
//    public static Map<Class<? extends Annotation>, Annotation> getFieldDeclaredAnnotations(Field target) {
//        try {
//            target.getDeclaredAnnotations();//伪获取，让类型把全部注释加载到内存
//            Field declaredAnnotationsField = findField(target.getClass(), "declaredAnnotations");
//            declaredAnnotationsField.setAccessible(true);
//            //返回注释列表
//            return MapUtils.copy((Map<Class<? extends Annotation>, Annotation>) declaredAnnotationsField.get(target));
//        } catch (Exception e) {
//            return null;
//        }
//    }

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