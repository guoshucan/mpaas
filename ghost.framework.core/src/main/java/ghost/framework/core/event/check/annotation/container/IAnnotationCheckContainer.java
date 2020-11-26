//package ghost.framework.core.event.check.annotation.container;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.event.factory.IEventFactoryContainer;
//import org.apache.commons.lang3.StringUtils;
//
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.lang.reflect.Parameter;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:注入冲突注入容器接口，主要是一处注存在一组不可一同注释的错误问题
// * @Date: 9:17 2020/1/12
// * @param <L> 注释列表
// */
//public interface IAnnotationCheckContainer<L extends List<Class<? extends Annotation>>> extends IEventFactoryContainer<L> {
//    /**
//     * 获取应用接口
//     *
//     * @return
//     */
//    IApplication getApp();
//
//    /**
//     * 获取排除验证注释
//     *
//     * @return
//     */
//    List<Class<? extends Annotation>> getExcludeAnnotationList();
//
//    /**
//     * 添加数组注释类型
//     *
//     * @param t
//     */
//    default void add(Class<? extends Annotation>[] t) {
//        L list = (L) new ArrayList<Class<? extends Annotation>>();
//        for (Class<? extends Annotation> l : t) {
//            list.add(l);
//        }
//        this.add(list);
//    }
//
//    /**
//     * 判断是否注释冲突
//     *
////     * @param target
//     * @param annotations
//     */
//    default void annotations(Object target, List<Annotation> annotations) {
//        //判断注释是否存在
//        if (annotations.size() == 0) {
//            return;
//        }
//        //排除注释
//        for (Class<? extends Annotation> a : this.getExcludeAnnotationList()) {
//            Annotation del = null;
//            for (Annotation b : annotations) {
//                if (a.isAssignableFrom(b.getClass())) {
//                    del = b;
//                    break;
//                }
//            }
//            if (del != null) {
//                annotations.remove(del);
//            }
//        }
//        //判断注释是否存在
//        if (annotations.size() == 0) {
//            return;
//        }
//        if (annotations.size() == 1) {
//            //打印注释
//            this.print(target, annotations);
//            return;
//        }
////        //遍历组数注释冲突列表
////        for (List<Class<? extends Annotation>> list : this.getEventExecuteList()) {
////            //是否存在列表注释重复
////            int is = 0;
////            //遍历冲突注释列表
////            int i = 0;
////            for (Class<? extends Annotation> c : list) {
////                //判断源注释最大位置
////                if (i >= annotations.size()) {
////                    break;
////                }
////                //获取注释类型并比对
////                if (ProxyUtil.getProxyObjectClass(annotations.get(i)).equals(c)) {
////                    is++;
////                }
////                i++;
////            }
////            //判断是否重复
////            if (is >= list.size()) {
////                throw new AnnotationCheckException(target, list);
////            }
////        }
//        //打印注释
//        this.print(target, annotations);
//    }
//
//    /**
//     * 打印注释
//     *
//     * @param target
//     * @param annotations
//     */
//    default void print(Object target, List<Annotation> annotations) {
//        String annotationList = "";
//        for (Annotation annotation : annotations) {
//            if (annotationList.equals("")) {
//                annotationList += this.getAnnotationString(annotation);
//            } else {
//                annotationList += ("," + this.getAnnotationString(annotation));
//            }
//        }
//        annotationList = "(" + annotationList + ")";
//        //注释验证通过
//        if (target instanceof Class) {
//            this.getLog().info("Class:" + ((Class) target).getSimpleName() + "Verified Annotations " + annotationList);
//            return;
//        }
//        if (target instanceof Field) {
//            Field field = (Field) target;
//            this.getLog().info(
//                    "Class:" + field.getDeclaringClass().getSimpleName() +
//                            ">Field:" +
//                            field.getType().getSimpleName() + ">" +
//                            "(Name:" + field.getName() + ">Class:" + field.getType().getSimpleName() + ") Verified Annotations " + annotationList);
//            return;
//        }
//        if (target instanceof Method) {
//            Method method = (Method) target;
//            String methodClass = method.getReturnType().equals(Void.TYPE) ? "Void" : method.getReturnType().getSimpleName();
//            this.getLog().info(
//                    "Class:" + method.getDeclaringClass().getSimpleName() +
//                            "Method:" +
//                            method.getDeclaringClass().getSimpleName() + ">" +
//                            "(Name:" + method.getName() + ">Class:" + methodClass + ") Verified Annotations " + annotationList);
//            return;
//        }
//        if (target instanceof Parameter) {
//            Parameter parameter = (Parameter) target;
//            this.getLog().info(
//                    "Parameter:" +
//                            parameter.getDeclaringExecutable().getDeclaringClass().getSimpleName() + ">" +
//                            "(Name:" + parameter.getName() + ">Class:" + parameter.getType().getSimpleName() + ") Verified Annotations " + annotationList);
//            return;
//        }
//    }
//
//    /**
//     * 获取注释
//     *
//     * @param annotation
//     * @return
//     */
//    default String getAnnotationString(Annotation annotation) {
//        String s = annotation.toString();
//        String[] strings = StringUtils.split(StringUtils.split(s, "(")[0], ".");
//        String ss = strings[strings.length - 1];
//        s = s.substring(s.indexOf("("), s.length());
//        return "@" + ss + s;
//    }
//
//    /**
//     * 判断注释冲突
//     *
//     * @param type
//     */
//    default void type(Class<?> type) {
//        this.annotations(type, new ArrayList(Arrays.asList(type.getDeclaredAnnotations())));
//    }
//
//    /**
//     * 判断注释冲突
//     *
//     * @param field
//     */
//    default void field(Field field) {
//        this.annotations(field, new ArrayList(Arrays.asList(field.getDeclaredAnnotations())));
//    }
//
//    /**
//     * 判断注释冲突
//     *
//     * @param method
//     */
//    default void method(Method method) {
//        this.annotations(method, new ArrayList(Arrays.asList(method.getDeclaredAnnotations())));
//    }
//
//    /**
//     * 判断注释冲突
//     *
//     * @param parameter
//     */
//    default void parameter(Parameter parameter) {
//        this.annotations(parameter, new ArrayList(Arrays.asList(parameter.getDeclaredAnnotations())));
//    }
//}