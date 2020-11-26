package ghost.framework.context.proxy.javassist;

import ghost.framework.util.CollectionUtils;
import javassist.*;

/**
 * package: ghost.framework.context.proxy.javassist
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:类型包装器
 * @Date: 2020/5/13:16:07
 */
public final class JavassistTypeWrapper {
    /**
     * 生成代理的默认后缀
     */
    public static final String PROXYSUFFIX = "$Impl";
    /**
     * 生成代理的默认前缀
     */
    public static final String PROXYFREFIX = "$Javassist$Proxy$";

    /**
     * 创建类型包装池
     * @param c
     * @param interfaces
     * @return
     * @throws NotFoundException
     * @throws CannotCompileException
     */
    public static JavassistClassWrapperPool createClassWrapperPool(Class<?> c, Class[] interfaces) throws NotFoundException, CannotCompileException {
        return createClassWrapperPool(c, interfaces, JavassistTypeWrapper.class.getPackage().getName() + PROXYFREFIX + c.getSimpleName() + PROXYSUFFIX, null, null, null);
    }

    /**
     * @param c
     * @param interfaces
     * @param argumentTypes
     * @param argumentNames
     * @return
     * @throws NotFoundException
     * @throws CannotCompileException
     */
    public static JavassistClassWrapperPool createClassWrapperPool(
            Class<?> c,
            Class[] interfaces,
            Class[] argumentTypes,
            String[] argumentNames,
            Object[] parameters) throws NotFoundException, CannotCompileException {
        return createClassWrapperPool(c, interfaces, JavassistTypeWrapper.class.getPackage().getName() + PROXYFREFIX + c.getSimpleName() + PROXYSUFFIX, argumentTypes, argumentNames, parameters);
    }

    /**
     * 默认类型池
     */
    private static ClassPool pool = new ClassPool(true);
    /**
     * 创建类型包装池
     *
     * @param c
     * @param interfaces
     * @param makeClassName
     * @param argumentTypes
     * @param argumentNames
     * @return
     * @throws NotFoundException
     * @throws CannotCompileException
     */
    public static JavassistClassWrapperPool createClassWrapperPool(Class<?> c,
                                                                   Class[] interfaces,
                                                                   String makeClassName,
                                                                   Class[] argumentTypes,
                                                                   String[] argumentNames,
                                                                   Object[] parameters) throws NotFoundException, CannotCompileException {
        //创建类池，true 表示使用默认路径
//        ClassPool pool = new ClassPool(true);
        CtClass ctClass = pool.makeClass(makeClassName); //创建代理类对象
        try {
            pool.get(c.getName());
        } catch (NotFoundException e) {
            pool.appendClassPath(new ClassClassPath(c));
        }
        //设置超类
        ctClass.setSuperclass(pool.get(c.getName()));
        //设置代理类的接口
        CtClass[] ctClassInterfaces = null;
        if (interfaces != null && interfaces.length > 0) {
            ctClassInterfaces = new CtClass[interfaces.length];
            int i = 0;
            for (Class<?> cc : interfaces) {
                ctClassInterfaces[i] = pool.getCtClass(cc.getName()); //获取代理对象的接口类
                i++;
            }
            ctClass.setInterfaces(ctClassInterfaces);
        }
        //添加构造函数
        if (!CollectionUtils.isEmpty(parameters)) {
            //拼接构建函数参数
            String parameterText = "";
            String body = "super();";
            int i = 0;
            for (Class<?> at : argumentTypes) {
                if (parameterText.equals("")) {
                    parameterText += "" + at.getName() + " " + argumentNames[i];
                } else {
                    parameterText += "," + at.getName() + " " + argumentNames[i];
                }
                body += "this." + at.getName() + " = " + at.getName() + ";";
                addField(ctClass, argumentNames[i], at);
                i++;
            }
            CtConstructor constructor = CtNewConstructor.make("public " + ctClass.getName() + "(" + parameterText + "){" + body + "}", ctClass);
            //添加注释
            ctClass.addConstructor(constructor);
        }
        return new JavassistClassWrapperPool(pool, ctClass, ctClassInterfaces, interfaces, makeClassName);
    }

    /**
     * 添加声明
     *
     * @param ctClass
     * @param fieldName
     * @param parameterType
     * @throws NotFoundException
     * @throws CannotCompileException
     */
    private static void addField(CtClass ctClass, String fieldName, Class<?> parameterType) throws NotFoundException, CannotCompileException {
        //获取函数返回值声明是否存在
        CtField field = ctClass.getDeclaredField(fieldName);
        //判断继承接口是否有声明函数返回值
        if (field == null) {
            //没有函数返回值声明，创建函数返回值声明
            field = CtField.make("private " + parameterType.getName() + " " + fieldName + ";", ctClass);
            ctClass.addField(field);
        }
    }
}