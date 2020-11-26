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
//
///**
// * package: ghost.framework.core.event.annotation.reflect.environment.factory.built
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:对象注释替换env事件工厂类，@{}中间内容替换事件工厂类
// * @Date: 18:55 2020/1/16
// */
//public class DefaultObjectAnnotationPostalReflectEnvironmentEventFactory<
//        O extends ICoreInterface,
//        T extends Object,
//        E extends IClassAnnotationEventTargetHandle<O, T, Object, String, Object>
//        >
//        extends ApplicationOwnerEventFactory
//        implements IObjectAnnotationPostalReflectEnvironmentEventFactory<O, T, E> {
//    /**
//     * @param app
//     */
//    public DefaultObjectAnnotationPostalReflectEnvironmentEventFactory(@Autowired IApplication app) {
//        super(app);
//    }
//
//    /**
//     * 获取替换前缀
//     *
//     * @return
//     */
//    @Override
//    public String getPrefix() {
//        return "@{";
//    }
//
//    /**
//     * 获取替换后缀
//     *
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
//     *
//     * @return
//     */
//    @Override
//    public List<Class<? extends Annotation>> getAnnotationList() {
//        return list;
//    }
//
//    /**
//     * 替换事件
//     *
//     * @param event 事件对象
//     */
//    @Override
//    public void reflect(E event) {
//        this.getLog().info("reflect:" + event.toString());
//    }
//}