package ghost.framework.context.asm.jd.core.deserializer.classfile;

import ghost.framework.context.asm.jd.core.model.classfile.ConstantPool;
import ghost.framework.context.asm.jd.core.model.classfile.Method;
import ghost.framework.context.asm.jd.core.model.classfile.attribute.Attribute;
import ghost.framework.context.asm.jd.core.model.classfile.attribute.AttributeMethodParameters;
import org.objectweb.asm.Type;

import java.lang.annotation.Annotation;

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
     * {@see org.jd.core.v1.entity.classfile.Method} 函数对象
     */
    private Method method;

    /**
     * 初始化函数扩展
     * @param method {@see org.jd.core.v1.entity.classfile.Method} 函数对象
     * @throws ClassNotFoundException
     */
    public MethodExpand(Method method) throws ClassNotFoundException {
        this.method = method;
        //转换参数字节码数组参数
        String end = method.getDescriptor().substring(method.getDescriptor().indexOf(")") + 1);
        this.returnType = Type.getType(end);
        //转换参数字节码数组参数
        this.argTypes = Type.getArgumentTypes(method.getDescriptor());
        if (this.argTypes == null || this.argTypes.length == 0) {

        } else {
            //初始化函数参数
            this.parameters = new MethodExpandParameter[this.argTypes.length];
            int i = 0;
//            for (MethodParameter parameter : methodParameters.getParameters()) {
//                this.parameters[i] = new MethodExpandParameter(parameter.getName(), this.argTypes[i]);
//                i++;
//            }
            //获取参数信息
            AttributeMethodParameters methodParameters = method.getAttribute("MethodParameters");
//
             i = 0;
//            for (Type type : argTypes) {
//                //转换函数参数字节码类型转换为实质类型
////                this.parameterTypes[i] = typeConverter(type);
//                this.parameters.add(new MethodExpandParameter())
//                i++;
//            }
//            //初始化函数参数
//            this.parameters = new ArrayList<>();
//            AttributeMethodParameters methodParameters = method.getAttribute("MethodParameters");
//            i = 0;
//            for (MethodParameter parameter : methodParameters.getParameters()) {
//                this.parameters.add(new MethodExpandParameter(parameter.getName(), this.parameterTypes[i]));
//                i++;
//            }
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

//    /**
//     * 字节码基础类型转换
//     * @param type 字节码类型
//     * @return 返回字节码类型转换结果
//     * @throws ClassNotFoundException
//     */
//    private static Class<?> typeConverter(Type type) throws ClassNotFoundException{
//        String  t = type.getClassName();
//        t = byte.class.getName();
//        //字节码基础类型转换
//        if(type.getDescriptor().equals("I")){
//            return int.class;
//        }
//        if(type.getDescriptor().equals("Z")){
//            return boolean.class;
//        }
//        if(type.getDescriptor().equals("B")){
//            return byte.class;
//        }
//        if(type.getDescriptor().equals("S")){
//            return short.class;
//        }
//        if(type.getDescriptor().equals("J")){
//            return long.class;
//        }
//        if(type.getDescriptor().equals("F")){
//            return float.class;
//        }
//        if(type.getDescriptor().equals("D")){
//            return double.class;
//        }
//        if(type.getDescriptor().equals("C")){
//            return char.class;
//        }
//        if(type.getDescriptor().equals("V")){
//            return Void.class;
//        }
////        if(type.getDescriptor().equals("[B")){
////            return byte[].class;
////        }
////        if(type.getDescriptor().equals("[C")){
////            return char[].class;
////        }
//        //实例类型转换
//        t = type.getClassName();
//        return Class.forName(type.getClassName());
//    }
//    private  Class[] parameterTypes;
    private  Type[] argTypes;

    public Annotation[] getAnnotations() {
        return null;
    }
    private  MethodExpandParameter[] parameters;
    public MethodExpandParameter[] getParameters() {
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