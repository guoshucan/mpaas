package ghost.framework.web.mvc.thymeleaf.plugin;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.invoke.Loader;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.io.IResourceDomain;
import ghost.framework.web.context.io.WebIResourceLoader;
import ghost.framework.web.mvc.thymeleaf.context.ThymeleafTemplateResourceResolver;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.messageresolver.StandardMessageResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

/**
 * package: ghost.framework.web.mvc.thymeleaf.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:Thymeleaf配置
 * @Date: 2020/6/1:22:45
 */
@Component
public class TemplateEngineConfig extends TemplateEngine {
    public TemplateEngineConfig() {
        super();
    }

    /**
     * web资源加载器接口
     */
    @Autowired
    private WebIResourceLoader<IResourceDomain> resourceLoader;

    /**
     * 绑定模板解析器
     *
     * @return
     */
    private ITemplateResolver templateHtmlResolver() {
        ThymeleafTemplateResourceResolver resolver = new ThymeleafTemplateResourceResolver(this.resourceLoader);
        resolver.setPrefix("/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
//        resolver.setOrder(0);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCheckExistence(false);
        resolver.setCacheable(false);
        return resolver;
    }

    /**
     * 绑定模板解析器
     *
     * @return
     */
    private ITemplateResolver templateCssResolver() {
        ThymeleafTemplateResourceResolver resolver = new ThymeleafTemplateResourceResolver(this.resourceLoader);
//        resolver.setPrefix("/");
        resolver.setSuffix(".css");
        resolver.setTemplateMode(TemplateMode.CSS);
//        resolver.setOrder(1);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCheckExistence(false);
        resolver.setCacheable(false);
        return resolver;
    }

    /**
     * 绑定模板解析器
     *
     * @return
     */
    private ITemplateResolver templateJavaScriptResolver() {
        ThymeleafTemplateResourceResolver resolver = new ThymeleafTemplateResourceResolver(this.resourceLoader);
//        resolver.setPrefix("/");
        resolver.setSuffix(".js");
        resolver.setTemplateMode(TemplateMode.JAVASCRIPT);
//        resolver.setOrder(2);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCheckExistence(false);
        resolver.setCacheable(false);
        return resolver;
    }

//    /**
//     * 绑定模板解析器
//     *
//     * @return
//     */
//    private ITemplateResolver templateTxtResolver() {
//        ThymeleafTemplateResourceResolver resolver = new ThymeleafTemplateResourceResolver(this.resourceLoader);
////        resolver.setPrefix("/");
//        resolver.setSuffix(".txt");
//        resolver.setTemplateMode(TemplateMode.TEXT);
////        resolver.setOrder(2);
//        resolver.setCharacterEncoding("UTF-8");
//        resolver.setCheckExistence(false);
//        resolver.setCacheable(false);
//        return resolver;
//    }

//    /**
//     * 绑定模板解析器
//     *
//     * @return
//     */
//    private ITemplateResolver templateXmlResolver() {
//        ThymeleafTemplateResourceResolver resolver = new ThymeleafTemplateResourceResolver(this.resourceLoader);
////        resolver.setPrefix("/");
//        resolver.setSuffix(".xml");
//        resolver.setTemplateMode(TemplateMode.XML);
////        resolver.setOrder(2);
//        resolver.setCharacterEncoding("UTF-8");
//        resolver.setCheckExistence(false);
//        resolver.setCacheable(false);
//        return resolver;
//    }

    /**
     * 绑定布局解析器
     *
     * @return
     */
    private LayoutDialect layoutDialectResolver() {
        return new LayoutDialect();
    }

    /**
     * 区域化语言解析器
     *
     * @return
     */
    private StandardMessageResolver messageResolver() {
        return new StandardMessageResolver();
    }

    /**
     * 绑定模板解析器
     *
     * @return
     */
    @Loader
    public void loader() {
        this.addTemplateResolver(templateHtmlResolver());
        this.addTemplateResolver(templateCssResolver());
        this.addTemplateResolver(templateJavaScriptResolver());
//        templateEngine.addTemplateResolver(templateTxtResolver());
//        templateEngine.addTemplateResolver(templateXmlResolver());
        //布局方言解析
        this.addDialect(layoutDialectResolver());
        //多语言解析
        this.addMessageResolver(messageResolver());
    }
}