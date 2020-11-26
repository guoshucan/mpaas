package com.example.springbootwebsockettest;
import javassist.*;
/**
 * package: com.example.springbootwebsockettest
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/5/13:5:47
 */
public class Proxy<T> {

    private Class<T> t;//接口

    public Proxy(Class<T> t) {
        this.t = t;
    }

    private static final String PROXYFREFIX = "$Proxy";//生成的代理对象名称前缀
    private static final String PROXYSUFFIX = "Impl";//生成的代理对象名称前缀

    //生成代理对象
    public T getProxyOject() {
        T proxyObject = null;
        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass ctClass = pool.makeClass(getPackageName() + getProxyObjectName()); //创建代理类对象

            //设置代理类的接口
            CtClass interf = pool.getCtClass(getPackageName() + "." + t.getSimpleName()); //获取代理对象的接口类
            CtClass[] interfaces = new CtClass[]{interf};


            ctClass.setSuperclass(pool.get(Superclass.class.getName()));
            ctClass.setInterfaces(interfaces);

            CtMethod[] methods = pool.get(IStu.class.getName()).getDeclaredMethods(); //代理类的所有方法
            CtField[] fields = interf.getDeclaredFields();//代理类的所有属性


            for (CtMethod method : methods) {
                String methodName = method.getName();
                CtMethod cm = new CtMethod(method.getReturnType(), methodName, method.getParameterTypes(), ctClass);
                cm.setBody("System.out.println(\"hand up my homework from proxy Object\");");
                ctClass.addMethod(cm);
            }
            Class aClass = ctClass.toClass();
            proxyObject = (T) aClass.newInstance();
        } catch (NotFoundException | CannotCompileException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return proxyObject;
    }

    //获取包名
    public String getPackageName() {
        Package aPackage = t.getPackage();
        String packageName = aPackage.getName();
        return packageName;
    }

    //获取代理对象的名称
    public String getProxyObjectName() {
        return PROXYFREFIX + t.getSimpleName() + PROXYSUFFIX;
    }
}