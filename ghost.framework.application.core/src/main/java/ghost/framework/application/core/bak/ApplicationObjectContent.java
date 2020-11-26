//package ghost.framework.app.core;
//
//import ghost.framework.app.context.IApplicationObject;
//import ghost.framework.context.module.IModule;
//
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:应用对象内容基础类
// * @Date: 18:44 2019-06-09
// */
//abstract class ApplicationObjectContent extends ApplicationModuleContent implements IApplicationObject {
//    /**
//     * 初始化应用对象内容基础类
//     * @param rootClass       引导类
//     * @throws Exception
//     */
//    protected ApplicationObjectContent(final Class<?> rootClass) throws Exception {
//        super(rootClass);
//        this.getLog().info("~ApplicationObjectContent");
//    }
//
//    /**
//     * 初始化
//     * @throws Exception
//     */
//    @Override
//    protected void init() throws Exception {
//        this.getLog().info("~ApplicationObjectContent.init->Before");
//        super.init();
//        this.getLog().info("~ApplicationObjectContent.init->After");
//    }
//
//
//    /**
//     * 注册对象
//     *
//     * @param module 模块内容
//     * @param o       模块注册的对象
//     */
////    @Override
//    public void registeredObject(IModule module, Object o) {
////        //遍历注册对象函数列表
////        for (Field f : o.getClass().getDeclaredFields()) {
////            //判读是否注释注入
////            if (f.isAnnotationPresent(Autowired.class) && !Modifier.isStatic(f.getModifiers())) {
////                //设置权限
////                f.setAccessible(true);
////                try {
////                    //获取注释
////                    Autowired a = f.getAnnotation(Autowired.class);
////                    //判断注入模式
////                    if (a.module()) {
////                        //从注入的属性获取绑定的对象
////                        f.set(o, content.getBean(f.getAnnotation(Autowired.class), o.getClass().getName()));
////                    } else {
////                        //从应用注入绑定对象
////                        //从注入的属性获取绑定的对象
////                        f.set(o, this.getBean(f.getAnnotation(Autowired.class), o.getClass().getName()));
////                    }
////                } catch (IllegalArgumentException e) {
////                    //
////                    e.printStackTrace();
////                } catch (IllegalAccessException e) {
////                    e.printStackTrace();
////                }
////            }
////        }
//    }
//
//    /**
//     * 注册对象
//     *
//     * @param o 注册的对象
//     */
////    @Override
//    public void registeredObject(Object o) {
////        //遍历注册对象函数列表
////        for (Field f : o.getClass().getDeclaredFields()) {
////            //判读是否注释注入
////            if (f.isAnnotationPresent(Autowired.class) && !Modifier.isStatic(f.getModifiers())) {
////                //设置权限
////                f.setAccessible(true);
////                try {
////                    //获取注释
////                    Autowired a = f.getAnnotation(Autowired.class);
////                    //判断是否为模块注入
////                    if(a.module()) {
////                        //属性为模块注入错误
////                        //因为未指定注入模块错误
////
////                    }else{
////                        //从注入的属性获取绑定的对象
////                        f.set(o, this.getBean(f.getAnnotation(Autowired.class), o.getClass().getName()));
////                    }
////                } catch (IllegalArgumentException e) {
////                    //
////                    e.printStackTrace();
////                } catch (IllegalAccessException e) {
////                    e.printStackTrace();
////                }
////            }
////        }
//    }
//}
