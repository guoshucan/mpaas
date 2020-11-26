//package ghost.framework.core.event.annotation.reflect.environment.factory.built;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.base.ICoreInterface;
//import ghost.framework.core.event.ApplicationOwnerEventFactory;
//import ghost.framework.context.event.annotation.IClassAnnotationEventTargetHandle;
//
//import java.lang.annotation.Annotation;
//import java.util.ArrayList;
//import java.util.List;
///**
// * package: ghost.framework.core.event.annotation.reflect.environment.factory.built
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:对象注释替换env事件工厂类，${}中间内容替换事件工厂类
// * @Date: 18:07 2020/1/16
// */
//public class DefaultObjectAnnotationGoldReflectEnvironmentEventFactory<
//        O extends ICoreInterface,
//        T extends Object,
//        E extends IClassAnnotationEventTargetHandle<O, T, Object, String, Object>
//        >
//        extends ApplicationOwnerEventFactory
//        implements IObjectAnnotationGoldReflectEnvironmentEventFactory<O, T, E> {
//    /**
//     * @param app
//     */
//    public DefaultObjectAnnotationGoldReflectEnvironmentEventFactory(@Autowired IApplication app) {
//        super(app);
//    }
//
//    /**
//     * 获取替换前缀
//     * @return
//     */
//    @Override
//    public String getPrefix() {
//        return "${";
//    }
//
//    /**
//     * 获取替换后缀
//     * @return
//     */
//    @Override
//    public String getSuffix() {
//        return "}";
//    }
//
//    /**
//     * 替换注释列表
//     * 如果没人指定任何注释侧替换全部注释参数
//     */
//    private List<Class<? extends Annotation>> list = new ArrayList<>();
//
//    /**
//     * 获取替换注释列表
//     * @return
//     */
//    @Override
//    public List<Class<? extends Annotation>> getAnnotationList() {
//        return list;
//    }
//
//    /**
//     * 替换事件
//     * @param event 事件对象
//     */
//    @Override
//    public void reflect(E event) {
//        this.getLog().info("reflect:" + event.toString());
//
//    }
//}
////    private void setAnnotationReflectEnvValue(IEnvironment env, Annotation a) throws AnnotationReflectEnvironmentException {
////        try {
////            //反射代理
////            InvocationHandler ih = Proxy.getInvocationHandler(a);
////            //获取注释的map参数属性
////            Field f = ih.getClass().getDeclaredField(AnnotationReflectUtil.MEMBER_VALUES);
////            //设置权限
////            f.setAccessible(true);
////            //获取属性的map对象注释参数
////            Map<String, Object> m = (Map) f.get(ih);
////            //遍历注释参数是否带${xxx.xx}的格式在env中搜索指定的参数值填充
////            for (String key : new ArrayList<>(m.keySet())) {
////                Object value = m.get(key);
////                //验证注释参数类型是否为字符串类型跟需要替换配置参数
////                if (value instanceof String) {
////                    String str = value.toString();
////                    if (StringUtils.isEmpty(str)) {
////                        continue;
////                    }
////                    try {
////                        m.put(key, env.replaceMiddle(str));
////                        System.out.println(m.get(key).toString());
////                    } catch (EnvironmentInvalidException e) {
////                        //判断错误是否引发错误，主要看注释是否带错误参数
////                        if (m.containsKey(AnnotationReflectUtil.ERROR) &&
////                                m.get(AnnotationReflectUtil.ERROR) instanceof Boolean &&
////                                (boolean) m.get(AnnotationReflectUtil.ERROR)) {
////                            throw e;
////                        } else {
////                            throw e;
////                        }
////                    }
//////                if (StringUtil.isMiddle(str, "${", "}")) {
//////                    m.put(value,
//////                            //替换${xxx.xx}中间内容
//////                            StringUtil.replaceMiddle(str, "${", "}",
//////                                    //获取env的${xxx.xx}中间的配置内容
//////                                    env.get(StringUtil.getMiddle(str, "${", "}"))));
//////                }
////                }
////            }
////            //遍历下级注释
////            for (Map.Entry<String, Object> entry : m.entrySet()) {
////                //判断注释参数是否为注释
////                if (Annotation.class.isAssignableFrom(entry.getOrderValue().getClass())) {
////                    //设置注释参数替换，同时继续向注释下级替换
////                    this.setAnnotationReflectEnvValue(env, (Annotation) entry.getOrderValue());
////                    continue;
////                }
////                //判断注释参数是否为数组对象
////                if (entry.getOrderValue().getClass().isArray()) {
////                    //获取数组注释参数
////                    Object[] objects = (Object[]) entry.getOrderValue();
////                    //遍历数组注释参数
////                    for (Object o : objects) {
////                        //判断注释参数是否为注释
////                        if (Annotation.class.isAssignableFrom(o.getClass())) {
////                            //设置注释参数替换，同时继续向注释下级替换
////                            this.setAnnotationReflectEnvValue(env, (Annotation) o);
////                            continue;
////                        }
////                    }
////                }
////            }
////        } catch (Exception e) {
////            throw new AnnotationReflectEnvironmentException(e);
////        }
////    }
