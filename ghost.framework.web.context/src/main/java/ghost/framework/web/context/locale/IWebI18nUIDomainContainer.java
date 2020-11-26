package ghost.framework.web.context.locale;

import ghost.framework.context.locale.ILocaleDomain;
import ghost.framework.context.locale.ILocaleDomainContainer;

/**
 * package: ghost.framework.web.context.locale
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:作为web页面UI的国际化域容器接口
 * 添加删除后调用 {@link IWebI18nUIDomainContainer#merge()} 合并后将国际化信息合并到 {@link IWebI18nUIContainer} 中。
 * @Date: 2020/6/16:23:48
 */
public interface IWebI18nUIDomainContainer<L extends ILocaleDomain> extends ILocaleDomainContainer<L> {
}