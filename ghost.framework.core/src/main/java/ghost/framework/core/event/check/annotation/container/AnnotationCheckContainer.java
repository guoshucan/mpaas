//package ghost.framework.core.event.check.annotation.container;
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.stereotype.Component;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.core.bean.factory.AbstractEventFactoryContainer;
//import java.lang.annotation.Annotation;
//import java.util.ArrayList;
//import java.util.List;
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:注释冲突容器，主要处理注释冲突问题报错
// * @Date: 9:16 2020/1/12
// */
//@Component
//public class AnnotationCheckContainer<L extends List<Class<? extends Annotation>>> extends AbstractEventFactoryContainer<L> implements IAnnotationCheckContainer<L> {
//    /**
//     * 获取应用接口
//     *
//     * @return
//     */
//    @Override
//    public IApplication getApp() {
//        return app;
//    }
//
//    /**
//     * 应用接口
//     */
//    private IApplication app;
//    /**
//     * 排除验证注释
//     */
//    private final List<Class<? extends Annotation>> excludeAnnotationList;// = new ArrayList<>();
//
//    /**
//     * 获取排除验证注释
//     *
//     * @return
//     */
//    @Override
//    public List<Class<? extends Annotation>> getExcludeAnnotationList() {
//        return excludeAnnotationList;
//    }
//
//    /**
//     * 初始化注入冲突注入容器，主要处理注释冲突问题报错
//     *
//     * @param app 应用接口
//     */
//    public AnnotationCheckContainer(@Autowired IApplication app) {
//        this.app = app;
//        this.excludeAnnotationList = new ArrayList<>();
//        //添加默认排除注释
//        this.excludeAnnotationList.add(Override.class);
//        this.excludeAnnotationList.add(Deprecated.class);
//        this.excludeAnnotationList.add(SuppressWarnings.class);
//        this.getLog().info("~" + this.getClass().getSimpleName());
//    }
//}