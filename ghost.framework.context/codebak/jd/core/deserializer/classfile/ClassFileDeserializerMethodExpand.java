/*
 * Copyright (c) 2008, 2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package ghost.framework.context.asm.jd.core.deserializer.classfile;

import ghost.framework.context.asm.jd.core.model.classfile.attribute.AttributeMethodParameters;
import  org.jd.core.v1.model.classfile.ClassFile;
import org.jd.core.v1.api.loader.Loader;
import org.jd.core.v1.api.loader.LoaderException;
import org.jd.core.v1.model.classfile.Method;
import  org.jd.core.v1.service.deserializer.classfile.ClassFileDeserializer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * {@see org.jd.core.v1}
 * 函数扩展
 */
public class ClassFileDeserializerMethodExpand extends ClassFileDeserializer {
    public ClassFileDeserializerMethodExpand() {
        super();
    }

    public static void main(String[] args) throws Exception {
        //获取指定函数信息
        Loader loader = new Loader() {
            @Override
            public byte[] load(String internalName) throws LoaderException {
                InputStream is = this.getClass().getResourceAsStream("/" + internalName + ".class");
                if (is == null) {
                    return null;
                } else {
                    try (InputStream in = is; ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                        byte[] buffer = new byte[1024];
                        int read = in.read(buffer);

                        while (read > 0) {
                            out.write(buffer, 0, read);
                            read = in.read(buffer);
                        }

                        return out.toByteArray();
                    } catch (IOException e) {
                        throw new LoaderException(e);
                    }
                }
            }

            @Override
            public boolean canLoad(String internalName) {
                return this.getClass().getResource("/" + internalName + ".class") != null;
            }
        };
        ClassFileDeserializerMethodExpand methodExpand = new ClassFileDeserializerMethodExpand();
        List<MethodExpand> methodExpandList = methodExpand.getMethods(loader, "codebak/core/deserializer/classfile/testInterface");
//        List<MethodExpand> methodExpandList = methodExpand.getMethods(Loader.class, "load");

        System.out.println(methodExpandList.size());
    }

    /**
     * 获取全部函数
     *
     * @param loader
     * @param internalTypeName 类型明天，格式为com/xxx/xxx
     * @return 返回全部函数列表
     * @throws Exception
     */
    public List<MethodExpand> getMethods(Loader loader, String internalTypeName) throws Exception {
        List<MethodExpand> methodExpandList = new ArrayList<>();
        ClassFile classFile = this.innerLoadClassFile(loader, internalTypeName);
        if (classFile.getMethods() != null) {
            for (Method method : classFile.getMethods()) {
//                methodExpandList.add(new MethodExpand(method));
                System.out.println(method.getName());
                AttributeMethodParameters methodParameters = method.getAttribute("MethodParameters");
                if(methodParameters!=null) {
                    System.out.println(Arrays.toString(methodParameters.getParameters()));
                }
            }
        }
        return methodExpandList;
    }

    /**
     * 获取指定名称函数
     *
     * @param loader
     * @param internalTypeName 类型明天，格式为com/xxx/xxx
     * @param methodName       函数名称
     * @return 返回指定函数列表
     * @throws Exception
     */
    public List<MethodExpand> getMethods(Loader loader, String internalTypeName, String methodName) throws Exception {
        List<MethodExpand> methodExpandList = new ArrayList<>();
        ClassFile classFile = this.innerLoadClassFile(loader, internalTypeName);
        if (classFile.getMethods() != null) {
            for (Method method : classFile.getMethods()) {
                if (method.getName().equals(methodName)) {
//                    methodExpandList.add(new MethodExpand(method));
                }
            }
        }
        return methodExpandList;
    }

//    /**
//     * 获取全部函数
//     *
//     * @param type 类型
//     * @return 返回全部函数列表
//     * @throws Exception
//     */
//    public List<MethodExpand> getMethods(Class<?> type) throws Exception {
//        List<MethodExpand> methodExpandList = new ArrayList<>();
//        ClassFile classFile = this.loadClassFile(type);
//        if (classFile.getMethods() != null) {
//            for (Method method : classFile.getMethods()) {
//                methodExpandList.add(new MethodExpand(method));
//            }
//        }
//        return methodExpandList;
//    }
//
//    /**
//     * 获取指定名称函数
//     *
//     * @param type       类型
//     * @param methodName 函数名称
//     * @return 返回指定函数列表
//     * @throws Exception
//     */
//    public List<MethodExpand> getMethods(Class<?> type, String methodName) throws Exception {
//        List<MethodExpand> methodExpandList = new ArrayList<>();
//        ClassFile classFile = this.loadClassFile(type);
//        if (classFile.getMethods() != null) {
//            for (Method method : classFile.getMethods()) {
//                if (method.getName().equals(methodName)) {
//                    methodExpandList.add(new MethodExpand(method));
//                }
//            }
//        }
//        return methodExpandList;
//    }
}