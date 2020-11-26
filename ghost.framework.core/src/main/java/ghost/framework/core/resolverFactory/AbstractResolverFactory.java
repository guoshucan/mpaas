package ghost.framework.core.resolverFactory;

import ghost.framework.context.resolver.IResolverFactory;

/**
 * package: ghost.framework.core.resolverFactory
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/3/21:18:00
 */
public abstract class AbstractResolverFactory<S, T extends Object>
        implements IResolverFactory<S, T> {
    @Override
    public boolean isResolver(Class<?> source, Class<?> target) {
        //获取类型的<S><T>两个泛型参数类型
//        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
//        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
//        if(source.getName().equals(actualTypeArguments[0].getTypeName())
        return this.getSourceType().equals(source) ||
                this.getSourceType().isAssignableFrom(source);
    }
}