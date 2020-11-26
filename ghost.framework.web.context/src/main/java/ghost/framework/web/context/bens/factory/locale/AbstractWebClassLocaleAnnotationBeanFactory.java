package ghost.framework.web.context.bens.factory.locale;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.context.bean.factory.locale.AbstractClassLocaleAnnotationBeanFactory;
import ghost.framework.context.module.IModule;

/**
 * package: ghost.framework.web.context.bens.factory.locale
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/16:23:36
 */
public abstract class AbstractWebClassLocaleAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V extends Object
                >
        extends AbstractClassLocaleAnnotationBeanFactory<O, T, E, V>
       implements IClassWebI18nAnnotationBeanFactory<O, T, E, V> {
    /**
     * 注入 ghost.framework.web.module 模块接口
     */
    @Autowired
    private IModule module;

    /**
     * 获取web模块接口
     * 本身也是作为获取 ghost.framework.web.module 模块本身对象接口
     *
     * @return 返回 ghost.framework.web.module 模块接口
     */
    @Override
    public IModule getWebModule() {
        return module;
    }
}