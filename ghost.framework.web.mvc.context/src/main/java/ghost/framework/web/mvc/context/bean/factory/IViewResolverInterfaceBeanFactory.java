package ghost.framework.web.mvc.context.bean.factory;

import ghost.framework.context.application.IGetApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.*;
import ghost.framework.context.log.IGetLog;
import ghost.framework.web.context.bens.factory.IWebClassBeanFactory;
import ghost.framework.web.mvc.context.bean.annotation.ClassAnnotationViewResolver;
import ghost.framework.web.mvc.context.servlet.view.IViewResolver;

/**
 * package: ghost.framework.web.mvc.context.bean.factory
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:继承 {@link IViewResolver} 视图解析接口类型绑定工厂接口
 * {@link IGetLog}
 * {@link IGetApplication#getApp()}
 * {@link IBeanFactory}
 * {@link IApplicationOwnerBeanFactory}
 * {@link IApplicationExecuteOwnerBeanFactory}
 * {@link IClassBeanFactory}
 * {@link ClassAnnotationViewResolver}
 * {@link IViewResolver}
 * @Date: 2020/6/3:22:09
 */
public interface IViewResolverInterfaceBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassBeanTargetHandle<O, T, V, String, Object>,
                V
                >
        extends IWebClassBeanFactory<O, T, E, V> {
}