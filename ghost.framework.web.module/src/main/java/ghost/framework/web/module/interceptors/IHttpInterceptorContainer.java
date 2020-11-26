package ghost.framework.web.module.interceptors;

import java.util.Set;

/**
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:http拦截器容器接口
 * @Date: 13:09 2019/11/7
 */
public interface IHttpInterceptorContainer<T extends IHttpInterceptor> extends Set<T> {
}
