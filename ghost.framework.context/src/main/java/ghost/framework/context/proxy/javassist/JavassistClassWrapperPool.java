package ghost.framework.context.proxy.javassist;

import ghost.framework.context.asm.ParameterUtils;
import ghost.framework.util.CollectionUtils;
import ghost.framework.util.ReflectUtil;
import ghost.framework.util.generic.Array;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.annotation.*;

import java.lang.reflect.Method;
import java.security.ProtectionDomain;

/**
 * package: ghost.framework.context.proxy.javassist
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/5/13:16:20
 */
public class JavassistClassWrapperPool {
    public JavassistClassWrapperPool(ClassPool pool, CtClass ctClass, CtClass[] ctClassInterfaces, Class[] interfaces, String makeClassName) {
        this.pool = pool;
        this.ctClass = ctClass;
        this.ctClassInterfaces = ctClassInterfaces;
        this.interfaces = interfaces;
        this.makeClassName = makeClassName;
    }

    private ClassPool pool;
    private CtClass ctClass;
    private CtClass[] ctClassInterfaces;
    private Class[] interfaces;
    private String makeClassName;
    /**
     * 布尔值获取值函数名称前缀
     */
    private static final String BooleanFrefix = "is";
    /**
     * 不属于布尔值的获取函数名称前缀
     */
    private static final String GetFrefix = "get";
    /**
     * 设置函数名称前缀
     */
    private static final String SetFrefix = "set";

    /**
     * 初始化接口函数
     */
    public void initMethod() throws NotFoundException, CannotCompileException {
        if (this.ctClassInterfaces != null && this.ctClassInterfaces.length > 0) {
            //遍历全部继承接口
            int i = 0;
            for (CtClass interf : this.ctClassInterfaces) {
                //获取添加参数函数所在接口类型
                Class<?> c = this.interfaces[i];
                //
                CtMethod[] methods = interf.getDeclaredMethods(); //代理类的所有方法
                //遍历代理类函数
                for (CtMethod method : methods) {
                    //判断静态函数将不处理
                    if (Modifier.isStatic(method.getModifiers())) {
                        continue;
                    }
                    String body = "";
                    String parameters = "";
                    //函数名称
                    String methodName = method.getName();
//                    //创建函数
                    CtMethod cm = null;// = new CtMethod(method.getReturnType(), methodName, method.getParameterTypes(), ctClass);
//                    //设置公开函数
//                    cm.setModifiers(javassist.Modifier.PUBLIC);
//                    //设置参数值
//                    this.addParameters(i, cm, methodName);
                    String fieldName = methodName;
                    //获取当前添加函数的函数对象
                    Method m = ReflectUtil.findMethod(c, methodName);
                    //获取函数数组参数
                    Class[] parameterTypes = m.getParameterTypes();
                    //判断函数是否有参数
//                    if (CollectionUtils.isEmpty(parameterTypes)) {
                    //函数没有参数，直接退出构建函数参数
                    //判断是否有返回值
                    if (m.getReturnType().equals(void.class)) {
                        //判断set函数处理
//                            cm = CtMethod.make("public void " + methodName + "(){}", ctClass);
                        if (methodName.startsWith(SetFrefix)) {
                            fieldName = fieldName.substring(SetFrefix.length());
                            fieldName = fieldName.substring(0, 1).toLowerCase().concat(fieldName.substring(1));
                            this.addField(fieldName, parameterTypes[0]);
                            //判断是否有返回值
//                            if (method.getReturnType().equals(Void.class)) {
                            //判断set函数处理
                            cm = CtMethod.make("public void " + methodName + "(" + parameterTypes[0].getName() + " " + fieldName + "){ this." + fieldName + " = " + fieldName + "; }", ctClass);
//                            } else {
//                                cm = CtMethod.make("public " + cm.getReturnType().getName() + " " + methodName + "(" + parameters + "){" + body + "}", ctClass);
//                            }
                        }
                    } else {
                        if (method.getReturnType().getName().equals(Boolean.class.getName()) || method.getReturnType().getName().equals(boolean.class.getName())) {
                            fieldName = fieldName.substring(BooleanFrefix.length());
                        } else {
                            fieldName = fieldName.substring(GetFrefix.length());
                        }
                        fieldName = fieldName.substring(0, 1).toLowerCase().concat(fieldName.substring(1));
                        body += "return this." + fieldName + ";";
                        this.addField(fieldName, m.getReturnType());
                        cm = CtMethod.make("public " + m.getReturnType().getName() + " " + methodName + "(){" + body + "}", ctClass);
                    }
//                    } else if(parameterTypes.length == 1) {
                    //获取函数数组参数名称
//                        String[] parameterNames = ParameterUtils.getMethodParamNames(m);
//                        int n = 0;
//                        for (String p : parameterNames) {
//                            //构建函数参数
//                            if (parameters.equals("")) {
//                                parameters += "" + parameterTypes[n].getName() + " " + p;
//                            } else {
//                                parameters += "," + parameterTypes[n].getName() + " " + p;
//                            }
//                            //构建函数内容
//                            body += "this. " + p + " = " + p + ";";
//                            n++;
//                        }

//                    }
                    //设置函数注释
                    /*
                    ClassFile ccFile = this.ctClass.getClassFile();
                    ConstPool constpool = ccFile.getConstPool();
                    //获取函数注释
                    java.lang.annotation.Annotation[] annotations = m.getAnnotations();
                    //判断是否有函数注释
                    if (!CollectionUtils.isEmpty(annotations)) {
                        for (java.lang.annotation.Annotation annotation : annotations) {
                            AnnotationsAttribute attr = new AnnotationsAttribute(constpool, AnnotationsAttribute.visibleTag);
                            Annotation annot = new Annotation(annotation.annotationType().getName(), constpool);
                            Map<String, Object> map = ProxyUtil.getProxyObjectAnnotationMemberValues(annotation);
                            for (Map.Entry<String, Object> entry : map.entrySet()) {
                                annot.addMemberValue(entry.getKey(), this.getMemberValue(entry.getValue(), constpool));
                            }
//                            annot.addMemberValue("name", new StringMemberValue("World!! (dynamic annotation)", ccFile.getConstPool()));
                            attr.addAnnotation(annot);
                            cm.getMethodInfo().addAttribute(attr);
                        }
                    }*/
//                    AnnotationsAttribute attr = new AnnotationsAttribute(constpool, AnnotationsAttribute.visibleTag);
//                    Annotation annot = new Annotation(Override.class.getName(), constpool);
////                    annot.addMemberValue("name", new StringMemberValue("World!! (dynamic annotation)",ccFile.getConstPool()));
//                    attr.addAnnotation(annot);
//                    cm.getMethodInfo().addAttribute(attr);
//                    cm.setBody("{" + this.body + "}");

                    if (cm != null) {
                        //设置公开函数
                        cm.setModifiers(javassist.Modifier.PUBLIC);
                        //
                        this.ctClass.addMethod(cm);
                    }
                }
                i++;
            }
        }
    }

    /**
     * 获取函数参数
     * @param method
     * @return
     * @throws NotFoundException
     */
    private String[] getMethodParamNames(CtMethod method) throws NotFoundException {
        // 使用javassist的反射方法的参数名
        MethodInfo methodInfo = method.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        if (attr == null) {
            return null;
        }
        int len = method.getParameterTypes().length;
        // 非静态的成员函数的第一个参数是this
        int pos = Modifier.isStatic(method.getModifiers()) ? 0 : 1;
        String[] names = new String[len];
        for (int i = 0; i < len; i++) {
            names[i] = attr.variableName(i + pos);
        }
        return names;
    }
//    public static String[] getParameterNamesByAsm5(Class<?> clazz,
//                                                   final Method method) {
//        final Class<?>[] parameterTypes = method.getParameterTypes();
//        if (parameterTypes == null || parameterTypes.length == 0) {
//            return null;
//        }
//        final Type[] types = new Type[parameterTypes.length];
//        for (int i = 0; i < parameterTypes.length; i++) {
//            types[i] = Type.getType(parameterTypes[i]);
//        }
//        final String[] parameterNames = new String[parameterTypes.length];
//
//        String className = clazz.getName();
//        int lastDotIndex = className.lastIndexOf(".");
//        className = className.substring(lastDotIndex + 1) + ".class";
//        InputStream is = clazz.getResourceAsStream(className);
//        try {
//            ClassReader classReader = new ClassReader(is);
//            classReader.accept(new ClassVisitor(Opcodes.ASM5) {
//                @Override
//                public MethodVisitor visitMethod(int access, String name,
//                                                 String desc, String signature, String[] exceptions) {
//                    // 只处理指定的方法
//                    Type[] argumentTypes = Type.getArgumentTypes(desc);
//                    if (!method.getName().equals(name)
//                            || !Arrays.equals(argumentTypes, types)) {
//                        return super.visitMethod(access, name, desc, signature,
//                                exceptions);
//                    }
//                    return new MethodVisitor(Opcodes.ASM5) {
//                        @Override
//                        public void visitLocalVariable(String name, String desc,
//                                                       String signature, org.objectweb.asm.Label start,
//                                                       org.objectweb.asm.Label end, int index) {
//                            // 非静态成员方法的第一个参数是this
//                            if (Modifier.isStatic(method.getModifiers())) {
//                                parameterNames[index] = name;
//                            } else if (index > 0) {
//                                parameterNames[index - 1] = name;
//                            }
//                        }
//                    };
//                }
//            }, 0);
//        } catch (IOException e) {
//        } finally {
//            try {
//                if (is != null) {
//                    is.close();
//                }
//            } catch (Exception e2) {
//            }
//        }
//        return parameterNames;
//    }
    /**
     * 获取类型注释参数
     * @param v
     * @param constpool
     * @return
     * @throws NotFoundException
     */
    private MemberValue getMemberValue(Object v, ConstPool constpool) throws NotFoundException {
        String name = v.getClass().getName();
        if (name.equals(Boolean.class.getName()) || name.equals(boolean.class.getName())) {
            return new BooleanMemberValue((Boolean) v, constpool);
        }
        if (name.equals(char.class.getName())) {
            return new CharMemberValue((char) v, constpool);
        }
        if (name.equals(Byte.class.getName()) || name.equals(byte.class.getName())) {
            return new ByteMemberValue((Byte) v, constpool);
        }
        if (name.equals(Short.class.getName()) || name.equals(short.class.getName())) {
            return new ShortMemberValue((Short) v, constpool);
        }
        if (name.equals(Integer.class.getName()) || name.equals(int.class.getName())) {
            return new IntegerMemberValue((Integer) v, constpool);
        }
        if (name.equals(Long.class.getName()) || name.equals(long.class.getName())) {
            return new LongMemberValue((Long) v, constpool);
        }
        if (name.equals(Float.class.getName()) || name.equals(float.class.getName())) {
            return new FloatMemberValue((Float) v, constpool);
        }
        if (name.equals(Double.class.getName()) || name.equals(double.class.getName())) {
            return new DoubleMemberValue((Double) v, constpool);
        }
        if (v instanceof Enum) {
//            Enum<?> e = Enum.valueOf(v.getClass(), v.toString());
//            return new EnumMemberValue(Enum.valueOf(v.getClass(), v.toString()), constpool);
        }
        if (v instanceof Array) {
            return new ArrayMemberValue((MemberValue) v, constpool);
        }
        if (v instanceof Annotation) {
            return new AnnotationMemberValue((Annotation) v, constpool);
        }
        if (v instanceof String) {
            return new StringMemberValue((String) v, constpool);
        }
        if (v instanceof Class) {
            return new ClassMemberValue(((Class) v).getName(), constpool);
        }
        throw new NotFoundException(name);
    }
    /**
     * 添加设置函数
     * @param interfaceIndex 函数所在接口位置
     * @param cm
     * @param methodName
     * @throws NotFoundException
     * @throws CannotCompileException
     */
    private void addSet(int interfaceIndex, CtMethod cm, String methodName) throws NotFoundException, CannotCompileException {
        //获取添加参数函数所在接口类型
        Class<?> c = this.interfaces[interfaceIndex];
        //判断返回函数声明是否存在，如果不存在创建声明
        String fieldName = methodName;
        //判断布尔值函数名称前缀是否有效
        if (!methodName.startsWith(SetFrefix)) {
            //设置函数
            throw new NotFoundException(methodName);
        }
        fieldName = fieldName.substring(SetFrefix.length());
        fieldName = fieldName.substring(0, 1).toLowerCase().concat(fieldName.substring(1));
        //判断返回函数声明是否存在，如果不存在创建声明
        //获取当前添加函数的函数对象
        Method m = ReflectUtil.findMethod(c, methodName);
        Class[] parameterTypes = m.getParameterTypes();
        //判断函数是否有参数
        if (CollectionUtils.isEmpty(parameterTypes)) {
            //函数没有参数，直接退出构建函数参数
            return;
        }
        //获取函数数组参数名称
        String[] parameterNames = ParameterUtils.getMethodParamNames(m);
        //遍历添加函数对应参数声明
        int i = 0;
        for (String name : parameterNames) {
            this.addField(name, parameterTypes[i]);
            i++;
        }
    }

    /**
     * @param interfaceIndex 函数所在接口位置
     * @param cm
     * @param methodName
     * @throws NotFoundException
     * @throws CannotCompileException
     */
    private void addParameters(int interfaceIndex, CtMethod cm, String methodName) throws NotFoundException, CannotCompileException {
        //获取添加参数函数所在接口类型
        Class<?> c = this.interfaces[interfaceIndex];
        //获取当前添加函数的函数对象
        Method m = ReflectUtil.findMethod(c, methodName);
        MethodInfo methodInfo = cm.getMethodInfo();
        Class[] parameterTypes = m.getParameterTypes();
        //判断函数是否有参数
        if(CollectionUtils.isEmpty(parameterTypes)){
            //函数没有参数，直接退出构建函数参数
            return;
        }
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        if (attr == null) {
            // exception
        }
//        if (cm.getMethodInfo().getAccessFlags() != AccessFlag.PUBLIC) {
//            return;
//        }
        String[] paramNames = new String[cm.getParameterTypes().length];
        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
        for (int i = 0; i < paramNames.length; i++) {
            paramNames[i] = attr.variableName(i + pos);
        }
        // paramNames即参数名
        for (int i = 0; i < paramNames.length; i++) {
            System.out.printf("%d:[%s %s] ", i + 1, parameterTypes[i].getSimpleName(), paramNames[i]);
            cm.addParameter(pool.get(parameterTypes[i].getName()));
            this.addField(paramNames[i], parameterTypes[i]);
        }
    }
//    private String body = "";
    /**
     * 添加声明
     * @param fieldName
     * @param parameterType
     * @throws CannotCompileException
     */
    private void addField(String fieldName, Class<?> parameterType) throws CannotCompileException {
        //获取函数返回值声明是否存在
        CtField field = null;
        try {
            field = this.ctClass.getDeclaredField(fieldName);
        } catch (NotFoundException e) {
        }
        //判断继承接口是否有声明函数返回值
        if (field == null) {
            //没有函数返回值声明，创建函数返回值声明
            field = CtField.make("private " + parameterType.getName() + " " + fieldName + ";", this.ctClass);
            this.ctClass.addField(field);
        }
    }

    /**
     * 添加除了布尔值类型的返回值
     *
     * @param cm
     * @param methodName
     * @throws NotFoundException
     * @throws CannotCompileException
     */
    private void addReturn(CtMethod cm, String methodName) throws NotFoundException, CannotCompileException {
        //判断布尔值函数名称前缀是否有效
        if (!methodName.startsWith(GetFrefix)) {
//            throw new NotFoundException(methodName);
            return;
        }
        //判断返回函数声明是否存在，如果不存在创建声明
        String fieldName = methodName.substring(GetFrefix.length());
        this.addReturnField(cm, fieldName.substring(0, 1).toLowerCase().concat(fieldName.substring(1)));
    }

    /**
     *
     * @param cm
     * @param fieldName
     * @throws NotFoundException
     * @throws CannotCompileException
     */
    private void addReturnField(CtMethod cm, String fieldName) throws NotFoundException, CannotCompileException {
        //获取函数返回值声明是否存在
        CtField field = null;
        try {
            field = this.ctClass.getDeclaredField(fieldName);
        } catch (NotFoundException e) {
        }
        //判断继承接口是否有声明函数返回值
        if (field == null) {
            //没有函数返回值声明，创建函数返回值声明
            field = CtField.make("private " + cm.getReturnType().getName() + " " + fieldName + ";", this.ctClass);
            this.ctClass.addField(field);
            //设置函数返回声明
//            cm.setBody("{ return this." + fieldName + "; }");
//            this.body += "return this." + fieldName + ";";
        }
    }

    /**
     * 添加布尔值函数返回参数
     *
     * @param cm
     * @param methodName
     * @throws NotFoundException
     * @throws CannotCompileException
     */
    private void addBooleanReturn(CtMethod cm, String methodName) throws NotFoundException, CannotCompileException {
        //判断布尔值函数名称前缀是否有效
        if (!methodName.startsWith(BooleanFrefix)) {
//            throw new NotFoundException(methodName);
            return;
        }
        //判断返回函数声明是否存在，如果不存在创建声明
        String fieldName = methodName.substring(BooleanFrefix.length());
        this.addReturnField(cm, fieldName.substring(0, 1).toLowerCase().concat(fieldName.substring(1)));
    }

    public Class createClassWrapper(ClassLoader loader, ProtectionDomain domain) throws CannotCompileException {
        return ctClass.toClass(loader, domain);
    }

    public Class createClassWrapper() throws CannotCompileException {
        return ctClass.toClass();
    }
}