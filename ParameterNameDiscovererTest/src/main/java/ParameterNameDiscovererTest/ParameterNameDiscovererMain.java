//package ParameterNameDiscovererTest;
//
//import ghost.framework.context.parameter.LocalVariableTableParameterNameDiscoverer;
//
///**
// * package: ParameterNameDiscovererTest
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:测试从函数或析构获取相对的参数名称
// * @Date: 2020/3/4:13:20
// */
//public class ParameterNameDiscovererMain {
//    public static void main(String[] args) {
//        ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
//        try {
//            String[] parameterNames = parameterNameDiscoverer
//                    .getParameterNames(ParameterNameDiscovererMain.class.getDeclaredMethod("test",
//                            new Class[]{String.class, int.class}));
//            System.out.print("test : ");
//            for (String parameterName : parameterNames) {
//                System.out.print(parameterName + ' ');
//            }
//        } catch (NoSuchMethodException | SecurityException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void test(String param1, int param2) {
//        System.out.println(param1 + param2);
//    }
//
//}
