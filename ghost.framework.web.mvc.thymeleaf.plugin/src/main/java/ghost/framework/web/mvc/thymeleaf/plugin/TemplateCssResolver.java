//package ghost.framework.web.mvc.thymeleaf.plugin;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.annotation.invoke.Loader;
//import ghost.framework.beans.annotation.stereotype.Component;
//import ghost.framework.context.io.IResourceDomain;
//import ghost.framework.web.context.io.WebIResourceLoader;
//import ghost.framework.web.mvc.thymeleaf.context.ThymeleafTemplateResourceResolver;
//import org.thymeleaf.TemplateEngine;
//import org.thymeleaf.templatemode.TemplateMode;
//
///**
// * package: ghost.framework.web.mvc.thymeleaf.css.plugin
// *
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:thymeleaf css解析器
// * @Date: 2020/6/7:14:39
// */
//@Component
//public class TemplateCssResolver extends ThymeleafTemplateResourceResolver {
//    /**
//     * 注入模板解析容器
//     */
//    @Autowired
//    private TemplateEngine engine;
//
//    /**
//     * 初始化thymeleaf css解析器
//     *
//     * @param resourceLoader 注入web资源加载接口
//     */
//    public TemplateCssResolver(@Autowired WebIResourceLoader<IResourceDomain> resourceLoader) {
//        super(resourceLoader);
//    }
//
//    /**
//     * 加载css解析器
//     */
//    @Loader
//    public void loader() {
//        this.setPrefix("/");
//        this.setSuffix(".css");
//        this.setTemplateMode(TemplateMode.CSS);
////        resolver.setOrder(2);
//        this.setCharacterEncoding("UTF-8");
//        this.setCheckExistence(false);
//        this.setCacheable(false);
//        this.engine.addTemplateResolver(this);
//    }
//}