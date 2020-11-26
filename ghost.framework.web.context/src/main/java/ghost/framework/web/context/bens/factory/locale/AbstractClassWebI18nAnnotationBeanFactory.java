package ghost.framework.web.context.bens.factory.locale;

import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.context.io.IResourceDomain;
import ghost.framework.context.io.ResourceDomain;
import ghost.framework.web.context.bens.annotation.locale.WebI18nNav;
import ghost.framework.web.context.bens.annotation.locale.WebI18nUI;
import ghost.framework.web.context.io.WebIResourceLoader;

/**
 * package: ghost.framework.web.context.bens.factory.locale
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:web i18n注释绑定工厂的基础类
 * {@link WebI18nNav}
 * {@link WebI18nUI}
 * @Date: 2020/6/9:14:19
 */
public abstract class AbstractClassWebI18nAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V extends Object
                >
        extends AbstractWebClassLocaleAnnotationBeanFactory<O, T, E, V>
        implements IClassWebI18nAnnotationBeanFactory<O, T, E, V> {
    /**
     * 加载
     * @param path jar资源路径
     * @param virtualPath url虚拟路径
     * @param event 事件对象
     */
    protected void loaderLocale(E event, String path, String virtualPath) {
        //获取web资源加载器接口
        WebIResourceLoader<IResourceDomain> resourceLoader = this.getWebModule().getBean(WebIResourceLoader.class);
        //遍历注册路径
        //添加web资源加载
        resourceLoader.add(new ResourceDomain(event.getValue(),
                this.getAnnotationClass(),
                event.getTarget().getProtectionDomain().getCodeSource().getLocation(),
                path, virtualPath));
        if (getLog().isDebugEnabled()) {
            getLog().debug("put WebResourcePath:" + path);
        }
    }
}