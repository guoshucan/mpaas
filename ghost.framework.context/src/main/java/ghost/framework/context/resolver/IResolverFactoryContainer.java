package ghost.framework.context.resolver;

import ghost.framework.beans.resolver.ResolverCompatibleMode;

import java.util.Collection;

/**
 * package: ghost.framework.context.resolver
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:解析器容器接口
 * @Date: 2020/3/18:17:23
 */
public interface IResolverFactoryContainer
        extends Collection<IResolverFactory> {
    /**
     * 解析指定源与目标类型
     *
     * @param source
     * @param target
     * @param compatibleMode
     * @param <S>
     * @param <T>
     * @return
     * @throws ResolverException
     */
    <S, T> void resolver(S source, T target, ResolverCompatibleMode compatibleMode) throws ResolverException;

    /**
     * 解析指定源与目标类型
     *
     * @param source
     * @param target
     * @param <S>
     * @param <T>
     * @return
     * @throws ResolverException
     */
    default <S, T> void resolver(S source, T target) throws ResolverException{
        this.resolver(source, target, ResolverCompatibleMode.IgnoreNullAndEmpty);
    }
}