package com.example.springbootwebsockettest.jd.deserializer.classfile;

import org.jd.core.v1.model.classfile.ConstantPool;
import org.jd.core.v1.model.classfile.Method;
import org.jd.core.v1.model.classfile.attribute.Attribute;
import org.jd.core.v1.model.classfile.attribute.AttributeMethodParameters;
import org.jd.core.v1.model.classfile.attribute.MethodParameter;
import org.objectweb.asm.Type;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * package: com.example.springbootwebsockettest.jd.deserializer.classfile
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:函数扩展
 * @Date: 2020/5/16:23:01
 */
public class MethodExpand {
    /**
     * {@link org.jd.core.v1.model.classfile.Method} 函数对象
     */
    private Method method;

    /**
     * 初始化函数扩展
     * @param method {@link org.jd.core.v1.model.classfile.Method} 函数对象
     * @throws ClassNotFoundException
     */
    public MethodExpand(Method method) throws ClassNotFoundException {
        this.method = method;
        //转换参数字节码数组参数
        String end = method.getDescriptor().substring(method.getDescriptor().indexOf(")") + 1);
//        if (method.getDescriptor().endsWith("V")) {
//            this.returnType = Void.class;
//        } else {
            this.returnType = Type.getType(end);
//        }
        //转换参数字节码数组参数
        this.argTypes = Type.getArgumentTypes(method.getDescriptor());
        if (this.argTypes == null || this.argTypes.length == 0) {

        } else {
            this.parameterTypes = new Class[this.argTypes.length];
            int i = 0;
//            for (Type type : argTypes) {
//                //转换函数参数字节码类型转换为实质类型
//                this.parameterTypes[i] = typeConverter(type);
//                i++;
//            }
            //初始化函数参数
            this.parameters = new ArrayList<>();
            AttributeMethodParameters methodParameters = method.getAttribute("MethodParameters");
            i = 0;
            for (MethodParameter parameter : methodParameters.getParameters()) {
                this.parameters.add(new MethodExpandParameter(parameter.getName(), this.parameterTypes[i]));
                i++;
            }
        }
    }

    /**
     * 函数的返回类型
     */
    private final Type            returnType;

    /**
     * 获取函数返回类型
     * @return
     */
    public Type getReturnType() {
        return returnType;
    }

    /**
     * 字节码基础类型转换
     * @param type 字节码类型
     * @return 返回字节码类型转换结果
     * @throws ClassNotFoundException
     */
    private static Class<?> typeConverter(Type type) throws ClassNotFoundException{
        //字节码基础类型转换
        if(type.getDescriptor().equals("I")){
            return int.class;
        }
        if(type.getDescriptor().equals("Z")){
            return boolean.class;
        }
        if(type.getDescriptor().equals("B")){
            return byte.class;
        }
        if(type.getDescriptor().equals("S")){
            return short.class;
        }
        if(type.getDescriptor().equals("J")){
            return long.class;
        }
        if(type.getDescriptor().equals("F")){
            return float.class;
        }
        if(type.getDescriptor().equals("D")){
            return double.class;
        }
        if(type.getDescriptor().equals("C")){
            return char.class;
        }
        if(type.getDescriptor().equals("V")){
            return Void.class;
        }
        //实例类型转换
        return Class.forName(Type.getType(type.getDescriptor()).getClassName());
    }
    private  Class[] parameterTypes;
    private  Type[] argTypes;

    public Annotation[] getAnnotations() {
        return null;
    }
    private  List<MethodExpandParameter> parameters;
    public List<MethodExpandParameter> getParameters() {
        return parameters;
    }

    public int getAccessFlags() {
        return method.getAccessFlags();
    }

    public String getName() {
        return method.getName();
    }

    public String getDescriptor() {
        return method.getDescriptor();
    }

    public <T extends Attribute> T getAttribute(String name) {
        return method.getAttribute(name);
    }

    public ConstantPool getConstants() {
        return method.getConstants();
    }
}