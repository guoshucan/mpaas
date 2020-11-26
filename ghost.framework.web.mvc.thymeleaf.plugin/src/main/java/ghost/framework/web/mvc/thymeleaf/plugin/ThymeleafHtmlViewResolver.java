package ghost.framework.web.mvc.thymeleaf.plugin;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.resolver.ResolverException;
import ghost.framework.web.context.WebConstant;
import ghost.framework.web.context.bind.annotation.*;
import ghost.framework.web.context.http.request.IHttpRequestMethod;
import ghost.framework.web.mvc.context.servlet.view.IViewResolver;
import ghost.framework.web.mvc.context.ui.Model;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
/**
 * package: ghost.framework.web.mvc.thymeleaf.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:Thymeleaf的Html模板解析器
 * @Date: 2020/6/2:12:00
 */
public class ThymeleafHtmlViewResolver implements IViewResolver {
    /**
     * 获取模板后缀
     *
     * @return 返回模板后缀，格式比如.xxx
     */
    @Override
    public String getTemplateSuffix() {
        return templateSuffix;
    }

    /**
     * 声明解析后缀
     */
    private final String templateSuffix = ".html";

    /**
     * 重写为可以解析没有带 {@link ThymeleafHtmlViewResolver#templateSuffix} 后缀也可以带后缀支持
     *
     * @param viewName 模板视图名称
     * @return
     */
    @Override
    public boolean isResolve(String viewName) {
        return true;
    }

    /**
     * 注入模板解析器
     */
    @Autowired
    private TemplateEngine engine;

    /**
     * 解析模板内容
     *
     * @param request       请求对象
     * @param response      响应对象
     * @param requestMethod 请求函数
     * @param viewName      视图名称
     *                      {@link RequestMapping}
     *                      {@link DeleteMapping}
     *                      {@link GetMapping}
     *                      {@link PatchMapping}
     *                      {@link PostMapping}
     *                      {@link PutMapping}
     *                      等注释函数返回的{@link String}类型的模板文件名称，以html扩展名为主的模板路径
     * @throws ResolverException
     */
    @Override
    public void resolveViewName(HttpServletRequest request, HttpServletResponse response,
                                IHttpRequestMethod requestMethod, String viewName) throws ResolverException {
        //判断后缀是否为.html，如果没有侧加载后缀
//        if (!viewName.endsWith(templateSuffix)) {
//            viewName += templateSuffix;
//        }
//        if (!viewName.startsWith("/")) {
//            viewName = "/" + viewName;
//        }
        Object locale = request.getAttribute(WebConstant.HttpServletRequest.HTTP_SERVLET_REQUEST_LOCALE_ATTRIBUTE);
        //处理错误
        try {
            //获取请求模型
            Model model = (Model) request.getAttribute(Model.class.getName());
            //创建模板解析内容接口
            WebContext context;
            if (model == null) {
                if (locale == null) {
                    context = new WebContext(request, response, request.getServletContext());
                } else {
                    context = new WebContext(request, response, request.getServletContext(), (Locale) locale);
                }
            } else {
                if (locale == null) {
                    context = new WebContext(request, response, request.getServletContext());
                    context.setVariables(model.asMap());
                } else {
                    context = new WebContext(request, response, request.getServletContext(), (Locale) locale, model.asMap());
                }
            }
            Form form = new Form();
            context.setVariable("name", form.getName());
            context.setVariable("url", form.getUrl());
            context.setVariable("tags", form.getTags().split(" "));
            context.setVariable("text", "this.engine.process");
            context.setVariable("form", form);
            //解析
            System.out.println("开始解析:" + viewName);
            this.engine.process(viewName, context, response.getWriter());
            //设置返回状态
            response.setStatus(HttpServletResponse.SC_OK);
            System.out.println("解析完成:" + viewName);
        } catch (Exception e) {
            throw new ResolverException(e.getMessage(), e);
        }
    }

    public static class Form {
        private String name = "spring.io";
        private String url = "http://spring.io";
        private String tags = "#spring #framework #java";

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }
    }
}