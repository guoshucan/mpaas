package ghost.framework.context.asm;

import ghost.framework.context.parameter.NameParameter;
import ghost.framework.util.ReflectUtil;
import net.bytebuddy.jar.asm.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

/**
 * package: ghost.framework.util
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:参数工具类
 * 引用 byte-buddy 包，byte-buddy 引用asm基础包实现反射函数与构件的参数名称
 * @Date: 2020/3/4:19:32
 */
public final class ParameterUtils {
    /**
     * 初始化函数数组名称参数
     *
     * @param m
     * @return
     */
    public static NameParameter[] getParameters(Method m) {
        //创建带名称的数组参数
        if (m.getParameterCount() == 0) {
            return null;
        } else {
            NameParameter[] nps = new NameParameter[m.getParameterCount()];
            //获取数组参数名称
            String[] ps = ParameterUtils.getMethodParamNames(m);
            int i = 0;
            for (Parameter parameter : m.getParameters()) {
                nps[i] = new NameParameter(ps[i], parameter);
                i++;
            }
            return nps;
        }
    }
    /**
     * 获取构建函数参数名称
     *
     * @param constructor
     * @return
     */
    public static String[] getConstructorParamNames(final Constructor constructor) {
        final Class<?>[] constructorParameterTypes = constructor.getParameterTypes();
        final String[] constructorParametersNames = new String[constructorParameterTypes.length];
        ClassReader cr;
        //使用InputStream构建ClassReader，不会存在使用类型在ClassReader中构建找不到类型资源问题
        try (InputStream stream = constructor.getDeclaringClass().getResourceAsStream(ReflectUtil.getClassFileName(constructor.getDeclaringClass()))) {
            cr = new ClassReader(stream);
        } catch (IOException e) {
            return null;
        }
        ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM7) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                final Type[] args = Type.getArgumentTypes(descriptor);
                // 方法名相同并且参数个数相同
                if (!name.equals("<init>") || !matchTypes(args, constructor.getParameterTypes())) {
                    return super.visitMethod(access, name, descriptor, signature, exceptions);
                }
                MethodVisitor v = super.visitMethod(access, name, descriptor, signature, exceptions);
                return new MethodVisitor(Opcodes.ASM7, v) {
                    /**
                     * 获取本地变量表
                     * LocalVariableTable
                     */
                    @Override
                    public void visitLocalVariable(String name, String desc,
                                                   String signature, Label start, Label end, int index) {
                        int i = index - 1;
                        // 如果是静态方法，则第一就是参数
                        // 如果不是静态方法，则第一个是"this"，然后才是方法的参数
                        if (Modifier.isStatic(constructor.getModifiers())) {
                            i = index;
                        }
                        if (i >= 0 && i < constructorParametersNames.length) {
                            constructorParametersNames[i] = name;
                        }
                        super.visitLocalVariable(name, desc, signature, start, end, index);
                    }

                };
            }
        };
        cr.accept(classVisitor, ClassReader.SKIP_FRAMES);
        return constructorParametersNames;
    }

    /**
     * 获取类方法的参数名
     *
     * @param method
     * @return
     * @throws IOException
     */
    public static String[] getMethodParamNames(final Method method) {
        final Class<?>[] methodParameterTypes = method.getParameterTypes();
        final String[] methodParametersNames = new String[methodParameterTypes.length];
        ClassReader cr;
        //使用InputStream构建ClassReader，不会存在使用类型在ClassReader中构建找不到类型资源问题
        try (InputStream stream = method.getDeclaringClass().getResourceAsStream(ReflectUtil.getClassFileName(method.getDeclaringClass()))){
            cr = new ClassReader(stream);
        } catch (IOException e) {
            return null;
        }
        ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM7) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                final Type[] args = Type.getArgumentTypes(descriptor);
                // 方法名相同并且参数个数相同
                if (!name.equals(method.getName()) || !matchTypes(args, method.getParameterTypes())) {
                    return super.visitMethod(access, name, descriptor, signature, exceptions);
                }
                MethodVisitor v = super.visitMethod(access, name, descriptor, signature, exceptions);
                return new MethodVisitor(Opcodes.ASM7, v) {
                    /**
                     * 获取本地变量表
                     * LocalVariableTable
                     */
                    @Override
                    public void visitLocalVariable(String name, String desc,
                                                   String signature, Label start, Label end, int index) {
                        int i = index - 1;
                        // 如果是静态方法，则第一就是参数
                        // 如果不是静态方法，则第一个是"this"，然后才是方法的参数
                        if (Modifier.isStatic(method.getModifiers())) {
                            i = index;
                        }
                        if (i >= 0 && i < methodParametersNames.length) {
                            methodParametersNames[i] = name;
                        }
                        super.visitLocalVariable(name, desc, signature, start, end, index);
                    }

                };
            }
        };
        cr.accept(classVisitor, ClassReader.SKIP_FRAMES);
        return methodParametersNames;
    }

    /**
     * 获取接口方法的参数名（抽象方法也可以）
     * 编译时增加参数  -parameters
     *
     * @param method
     * @return
     */
    public static String[] getInterfaceMethodParamNames(Method method) {
        final String[] methodParametersNames = new String[0];
        //获取指定函数信息
        ClassReader cr;
        //使用InputStream构建ClassReader，不会存在使用类型在ClassReader中构建找不到类型资源问题
        try (InputStream stream = method.getDeclaringClass().getResourceAsStream(ReflectUtil.getClassFileName(method.getDeclaringClass()))) {
            cr = new ClassReader(stream);
        } catch (IOException e) {
            return null;
        }
        ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM7) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                final Type[] args = Type.getArgumentTypes(descriptor);
                // 方法名相同并且参数个数相同
                if (!name.equals(method.getName()) || !matchTypes(args, method.getParameterTypes())) {
                    return super.visitMethod(access, name, descriptor, signature, exceptions);
                }
                MethodVisitor v = super.visitMethod(access, name, descriptor, signature, exceptions);
                return new MethodVisitor(Opcodes.ASM7, v) {
                    /**
                     * 获取 MethodParameters 参数
                     */
                    @Override
                    public void visitParameter(String name, int access) {
                        //methodParametersNames.add(name);
                        super.visitParameter(name, access);
                    }
                    /**
                     * 获取本地变量表
                     * LocalVariableTable
                     */
                    @Override
                    public void visitLocalVariable(String name, String desc,
                                                   String signature, Label start, Label end, int index) {
                        int i = index - 1;
                        // 如果是静态方法，则第一就是参数
                        // 如果不是静态方法，则第一个是"this"，然后才是方法的参数
                        if (Modifier.isStatic(method.getModifiers())) {
                            i = index;
                        }
                        if (i >= 0 && i < methodParametersNames.length) {
                            methodParametersNames[i] = name;
                        }
                        super.visitLocalVariable(name, desc, signature, start, end, index);
                    }
                };
            }
        };
        cr.accept(classVisitor, ClassReader.SKIP_FRAMES);
        return methodParametersNames;
    }

    /**
     * 比较类型参数是否一致
     */
    private static boolean matchTypes(Type[] types, Class<?>[] parameterTypes) {
        if (types.length != parameterTypes.length) {
            return false;
        }
        for (int i = 0; i < types.length; i++) {
            if (!Type.getType(parameterTypes[i]).equals(types[i])) {
                return false;
            }
        }
        return true;
    }
}