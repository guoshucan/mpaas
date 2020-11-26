package com.example.springbootwebsockettest;

import org.objectweb.asm.*;

import java.lang.reflect.InvocationHandler;

/**
 * package: com.example.springbootwebsockettest
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/5/16:12:44
 */
public class InterfaceAnalysis implements Opcodes {

    public static byte[] dump() throws Exception {
        ClassWriter cw = new ClassWriter(0);
        FieldVisitor fv;
        MethodVisitor mv;
        AnnotationVisitor av0;
        cw.visit(V1_7, ACC_PUBLIC + ACC_ABSTRACT + ACC_INTERFACE, "cn/com/*/*/gateway/service/IIndividualTax", null, "java/lang/Object", null);
        av0 = cw.visitAnnotation("Lcn/com/*/*/gateway/service/ServiceType;", false);
        av0.visitEnd();
        mv = cw.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "request", "(Lcn/com/*/*/gateway/dto/BusinessRequest;)Lcn/com/*/*/gateway/dto/BusinessResponse;", null, new String[]{"cn/com/*/*/gateway/exception/GatewayException"});
        mv.visitEnd();
        cw.visitEnd();
        return cw.toByteArray();
    }
//    private static void addSetterMethod(ClassWriter writer, String owner) throws Exception {
//
//        ClassReader reader = new ClassReader("");
//        reader.accept();
//        String methodDesc = "(" + Type.getDescriptor(InvocationHandler.class) + ")V";
//        MethodVisitor methodVisitor = writer.visitMethod(Opcodes.ACC_PUBLIC, METHOD_SETTER, methodDesc, null, null);
//        methodVisitor.visitCode();
//        methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
//        methodVisitor.visitVarInsn(Opcodes.ALOAD, 1);
//        methodVisitor.visitFieldInsn(Opcodes.PUTFIELD, owner, FIELD_INVOCATIONHANDLER, Type.getDescriptor(InvocationHandler.class));
//        methodVisitor.visitInsn(Opcodes.RETURN);
//        methodVisitor.visitMaxs(2, 2);
//        methodVisitor.visitEnd();
//    }

}