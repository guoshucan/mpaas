package com.example.springbootwebsockettest;

import javassist.*;
import javassist.bytecode.AccessFlag;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.analysis.Type;

import java.lang.reflect.Method;

/**
 * package: com.example.springbootwebsockettest
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/5/16:10:31
 */
public class VLInterface {
    public static void main(String[] args) throws NotFoundException {
        ClassPool pool = ClassPool.getDefault();

        Class[] cs = new Class[] { VLInterfaceTest.class};
        String[] prefixStrings = new String[] { "ptz", "user" ,"streamsutil"};
        for (int i = 0; i < cs.length; i++) {
            CtClass ctClass;
            Class clz = cs[i];

            ctClass = pool.getCtClass(clz.getName());
//            ctClass.setInterfaces(new CtClass[]{pool.getCtClass(VLInterfaceTest.class.getName())});
            Method[] methods = clz.getDeclaredMethods();

            for (Method m : methods) {
                String name = m.getName();
                CtMethod cm = ctClass.getDeclaredMethod(name);
                MethodInfo methodInfo = cm.getMethodInfo();
                Class[] parameterTypes = m.getParameterTypes();

                CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
                LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
                if (attr == null) {
                    // exception
                }
                if(cm.getMethodInfo().getAccessFlags()!= AccessFlag.PUBLIC)continue;
                //System.out.println();
                String[] paramNames = new String[cm.getParameterTypes().length];
                int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
                for (int j = 0; j < paramNames.length; j++)
                    paramNames[j] = attr.variableName(j + pos);
                // paramNames即参数名

                System.out.printf("\"%s.%s\":\r\n\tReturn:%s\t(Parameters ",
                        prefixStrings[i], name, m.getReturnType().getSimpleName());
                for (int i1 = 0; i1 < paramNames.length; i1++) {

                    System.out.printf("%d:[%s %s] ", i1+1, parameterTypes[i1]
                            .getSimpleName(), paramNames[i1]);
                }
                // for (Class class1 : parameterTypes) {
                // System.out.printf("[%s],", class1.getName());
                // }
                System.out.println(")\r\n");
            }
        }
    }
}
