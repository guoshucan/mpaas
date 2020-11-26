//package ghost.framework.core.event.annotation.factory.other;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.data.annotation.CustomDataSource;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.base.ICoreInterface;
//import ghost.framework.core.event.ApplicationOwnerEventFactory;
//import ghost.framework.context.event.annotation.IClassAnnotationEventTargetHandle;
//
//import java.lang.annotation.Annotation;
//
///**
// * package: ghost.framework.core.event.annotation.factory.built
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:类型 {@link ghost.framework.beans.data.annotation.CustomDataSource} 注释事件工厂类
// * @Date: 15:43 2020/1/11
// * @param <O> 发起方类型
// * @param <T> 目标类型
// * @param <E> 注入绑定事件目标处理类型
// * @param <V> 返回类型
// */
//public class DefaultClassDataSourceAnnotationEventFactory<
//        O extends ICoreInterface,
//        T extends Class<?>,
//        E extends IClassAnnotationEventTargetHandle<O, T, V, String, Object>,
//        V extends Object
//        >
//        extends ApplicationOwnerEventFactory<O, T, E>
//        implements IClassDataSourceAnnotationEventFactory<O, T, E, V> {
//    /**
//     * 初始化类型 {@link ghost.framework.beans.data.annotation.CustomDataSource} 注释事件工厂类
//     * @param app 应用接口
//     */
//    public DefaultClassDataSourceAnnotationEventFactory(@Autowired IApplication app){
//        super(app);
//    }
//    /**
//     * 注释类型
//     */
//    private final Class<? extends Annotation> annotation = CustomDataSource.class;
//
//    /**
//     * 重新注释类型
//     *
//     * @return
//     */
//    @Override
//    public Class<? extends Annotation> getAnnotation() {
//        return annotation;
//    }
//
//    @Override
//    public void loader(E event) {
//        //判断是否没有此注释或此注释已经在排除执行列表中
//        if (this.isLoader(event)) {
//            return;
//        }
//    }
//
//    @Override
//    public void unloader(E event) {
//        //判断是否没有此注释或此注释已经在排除执行列表中
//        if (this.isLoader(event)) {
//            return;
//        }
//    }
//}
