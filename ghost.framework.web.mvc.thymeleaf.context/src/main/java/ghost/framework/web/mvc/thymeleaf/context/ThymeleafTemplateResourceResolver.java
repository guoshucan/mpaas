package ghost.framework.web.mvc.thymeleaf.context;

import ghost.framework.context.io.IResourceDomain;
import ghost.framework.web.context.io.WebIResourceLoader;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.templateresolver.AbstractConfigurableTemplateResolver;
import org.thymeleaf.templateresource.ITemplateResource;

import java.util.Map;

/**
 * package: ghost.framework.web.mvc.thymeleaf.context
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模板资源解析器
 * @Date: 2020/6/1:21:33
 */
public class ThymeleafTemplateResourceResolver extends AbstractConfigurableTemplateResolver {
    /**
     * web资源解析容器接口
     */
    private final WebIResourceLoader<IResourceDomain> resourceLoader;

    /**
     * 初始化模板资源解析器
     * @param resourceLoader web资源解析容器接口
     */
    public ThymeleafTemplateResourceResolver(WebIResourceLoader<IResourceDomain> resourceLoader){
        this.resourceLoader = resourceLoader;
    }
    /**
     * 重写引用资源模板加载
     * @param configuration
     * @param ownerTemplate
     * @param template
     * @param resourceName
     * @param characterEncoding
     * @param templateResolutionAttributes
     * @return
     */
    @Override
    protected ITemplateResource computeTemplateResource(IEngineConfiguration configuration, String ownerTemplate, String template, String resourceName, String characterEncoding, Map<String, Object> templateResolutionAttributes) {
        return new ThymeleafTemplateResource(this.resourceLoader, resourceName, characterEncoding);
    }
}
