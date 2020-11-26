package ghost.framework.web.mvc.groovy.plugin;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.io.IResourceDomain;
import ghost.framework.context.resolver.ResolverException;
import ghost.framework.web.context.bind.annotation.*;
import ghost.framework.web.context.http.request.IHttpRequestMethod;
import ghost.framework.web.context.io.IWebResource;
import ghost.framework.web.context.io.WebIResourceLoader;
import ghost.framework.web.mvc.context.servlet.view.IViewResolver;
import ghost.framework.web.mvc.context.ui.Model;
import groovy.text.SimpleTemplateEngine;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * package: ghost.framework.web.mvc.groovy.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:Groovy模板解析器
 * @Date: 2020/6/2:12:00
 */
@Component
public class GroovyViewResolver extends SimpleTemplateEngine implements IViewResolver {
    public GroovyViewResolver() {
        super();
    }

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
     * 模板后缀
     */
    private final String templateSuffix = ".tpl";
    /**
     * web资源加载器接口
     */
    @Autowired
    private WebIResourceLoader<IResourceDomain> resourceLoader;

    /**
     * 解析模板系统
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
    public void resolveViewName(HttpServletRequest request, HttpServletResponse response, IHttpRequestMethod requestMethod,
                                String viewName) throws ResolverException {
        //判断后缀是否为.html，如果没有侧加载后缀
        if (!viewName.endsWith(templateSuffix)) {
            viewName += templateSuffix;
        }
        if (!viewName.startsWith("/")) {
            viewName = "/" + viewName;
        }
        try {
            //获取路径文件资源
            IWebResource resource = this.resourceLoader.getResource(viewName);
            //判断资源是否有效
            if (resource == null) {
                //找不到资源文件
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            //获取资源流对象
            try (InputStream inputStream = resource.getInputStream()) {
                //构建缓冲读取流对象
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(new BufferedInputStream(inputStream), response.getCharacterEncoding()))) {
                    //模板参数
                    Map<String, Object> context = new HashMap<>();
                    //获取请求模型
                    Model model = (Model) request.getAttribute(Model.class.getName());
                    if (model != null) {
                        context.putAll(model.asMap());
                    }
                    //解析输出模板内容
                    this.createTemplate(reader).
                            make(context).
                            writeTo(response.getWriter());
                } catch (IOException e) {
                    throw new ResolverException(e.getMessage(), e);
                }
            } catch (IOException e) {
                throw new ResolverException(e.getMessage(), e);
            }
            //设置返回状态
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            throw new ResolverException(e.getMessage(), e);
        }
    }
}