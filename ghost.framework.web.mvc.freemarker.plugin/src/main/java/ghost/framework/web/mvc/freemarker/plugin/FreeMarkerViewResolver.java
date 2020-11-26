package ghost.framework.web.mvc.freemarker.plugin;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.io.IResourceDomain;
import ghost.framework.context.resolver.ResolverException;
import ghost.framework.web.context.http.request.IHttpRequestMethod;
import ghost.framework.web.context.io.WebIResourceLoader;
import ghost.framework.web.mvc.context.servlet.view.IViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * package: ghost.framework.web.mvc.freemarker.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/2:12:00
 */
public class FreeMarkerViewResolver implements IViewResolver {
    /**
     * 获取模板后缀
     * @return 返回模板后缀，格式比如.xxx
     */
    @Override
    public String getTemplateSuffix() {
        return templateSuffix;
    }
    private final String templateSuffix = ".ftl";
    @Autowired
    private  freemarker.template.Configuration configuration;
    /**
     * web资源加载器接口
     */
    @Autowired
    private WebIResourceLoader<IResourceDomain> resourceLoader;
    @Override
    public void resolveViewName(HttpServletRequest request, HttpServletResponse response,
                                IHttpRequestMethod requestMethod, String viewName) throws ResolverException {
        //3.根据模板名称创建模板对象
//        Template template = configuration.getTemplate("test.ftl");
    }
}