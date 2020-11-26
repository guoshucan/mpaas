//package ghost.framework.web.mvc.thymeleaf.plugin;
//
//import ghost.framework.beans.annotation.constructor.Constructor;
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.invoke.Loader;
//import ghost.framework.beans.annotation.stereotype.Component;
//import nz.net.ultraq.thymeleaf.LayoutDialect;
//import nz.net.ultraq.thymeleaf.decorators.SortingStrategy;
//import org.thymeleaf.TemplateEngine;
//
///**
// * package: ghost.framework.web.mvc.thymeleaf.layout.plugin
// *
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:thymeleaf布局方言
// * @Date: 2020/6/7:14:39
// */
//@Component
//public class TemplateLayoutDialect extends LayoutDialect {
//    /**
//     * 注入模板解析容器
//     */
//    @Autowired
//    private TemplateEngine engine;
//
//    /**
//     * 初始化模板布局方言
//     * @param sortingStrategy
//     * @param autoHeadMerging
//     */
//    public TemplateLayoutDialect(SortingStrategy sortingStrategy, boolean autoHeadMerging) {
//        super(sortingStrategy, autoHeadMerging);
//    }
//
//    /**
//     * 初始化模板布局方言
//     * @param sortingStrategy
//     */
//    public TemplateLayoutDialect(SortingStrategy sortingStrategy) {
//        super(sortingStrategy);
//    }
//
//    /**
//     * 注释构建此入口
//     * 初始化模板布局方言
//     */
//    @Constructor
//    public TemplateLayoutDialect() {
//        super();
//    }
//
//    /**
//     * 卸载本方言
//     */
//    @Loader
//    public void loader() {
//        this.engine.addDialect(this);
//    }
//}