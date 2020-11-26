package ghost.framework.core.resolverFactory;

import ghost.framework.beans.annotation.factory.ResolverFactory;
import ghost.framework.beans.resolver.ResolverCompatibleMode;
import ghost.framework.context.resolver.IPropertiesResolverObjectFactory;
import ghost.framework.context.resolver.ResolverException;

import java.util.Properties;

/**
 * package: ghost.framework.core.resolver
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description: {@link Properties}解析对象类
 * @Date: 2020/3/18:17:29
 * @param <S> 解析源
 * @param <T> 解析目标
 */
@ResolverFactory
public class PropertiesResolverObjectFactory<S extends Properties, T extends Object>
        extends AbstractResolverObjectFactory<S, T>
        implements IPropertiesResolverObjectFactory<S, T> {
    private final Class<?> sourceType = Properties.class;

    @Override
    public Class<?> getSourceType() {
        return sourceType;
    }

    /**
     * 解析目标
     *
     * @param source 解析源
     * @param target 解析目标
     * @throws ResolverException
     */
    @Override
    public void resolver(S source, T target) throws ResolverException {
        this.resolver(source, target, ResolverCompatibleMode.IgnoreNullAndEmpty);
    }

    /**
     * 解析目标
     *
     * @param source         解析源
     * @param target         解析目标
     * @param compatibleMode 解析兼容模式
     * @throws ResolverException
     */
    @Override
    public void resolver(S source, T target, ResolverCompatibleMode compatibleMode) throws ResolverException {
        try {
            switch (compatibleMode) {
                case Default:

                    return;
                case IgnoreNull:

                    return;
                case IgnoreEmpty:

                    return;
                case IgnoreNullAndEmpty:

                    return;
            }
        } catch (Exception e) {
            throw new ResolverException(e.getMessage(), e);
        }
    }
}