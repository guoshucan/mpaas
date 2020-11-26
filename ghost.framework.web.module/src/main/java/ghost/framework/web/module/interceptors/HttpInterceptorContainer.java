package ghost.framework.web.module.interceptors;

import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.bean.factory.AbstractBeanFactoryContainer;

/**
 * package: ghost.framework.web.module.interceptors
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:http拦截器容器
 * @Date: 2020/2/1:19:41
 */
@Component
public class HttpInterceptorContainer extends AbstractBeanFactoryContainer<IHttpInterceptor> implements IHttpInterceptorContainer<IHttpInterceptor> {
}
