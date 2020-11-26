package com.example.springbootwebsockettest;

import javassist.*;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

/**
 * package: com.example.springbootwebsockettest
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/5/16:1:18
 */
public class Test1Main {
    public static void main(String[] args) {
        Class<?> clazz = Test1Main.class;
        ClassPool pool = ClassPool.getDefault();
        try {
            CtClass ctClass = pool.get(dddd.class.getName());
            CtMethod ctMethod = ctClass.getDeclaredMethod("test");

            // 使用javassist的反射方法的参数名
            MethodInfo methodInfo = ctMethod.getMethodInfo();
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
            AttributeInfo attributeInfo = methodInfo.getAttribute("test");
            LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
            if (attr != null) {
                int len = ctMethod.getParameterTypes().length;
                // 非静态的成员函数的第一个参数是this
                int pos = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1;
                System.out.print("test : ");
                for (int i = 0; i < len; i++) {
                    System.out.print(attr.variableName(i + pos) + ' ');
                }
                System.out.println();
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }
    interface dddd {
        void test(String param1, int param2);
    }
}
