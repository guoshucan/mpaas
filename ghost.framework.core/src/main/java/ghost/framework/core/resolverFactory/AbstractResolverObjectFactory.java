package ghost.framework.core.resolverFactory;

import ghost.framework.context.resolver.IResolverObjectFactory;

/**
 * package: ghost.framework.core.resolver
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:解析对象基础类
 * @Date: 2020/3/18:17:26
 * @param <S> 解析源
 * @param <T> 解析目标
 */
public abstract class AbstractResolverObjectFactory<S, T extends Object>
    extends AbstractResolverFactory<S, T>
        implements IResolverObjectFactory<S, T> {
    @Override
    public boolean isResolver(Class<?> source, Class<?> target) {
        return super.isResolver(source, target) ;
    }
}