package ghost.framework.context.resolver;

import ghost.framework.beans.resolver.ResolverCompatibleMode;

/**
 * package: ghost.framework.context.resolver
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:解析器接口
 * @Date: 2020/3/18:17:04
 * @param <S> 解析源
 * @param <T> 解析目标
 */
public interface IResolverFactory<S, T> {
    /**
     * 获取解析器源类型
     *
     * @return
     */
    Class<?> getSourceType();

    /**
     * 判断是否可以解析
     *
     * @param source 解析源
     * @param target 解析目标
     * @return
     */
    boolean isResolver(Class<?> source, Class<?> target);

    /**
     * 解析目标
     *
     * @param source         解析源
     * @param target         解析目标
     * @param compatibleMode 解析兼容模式
     * @throws ResolverException
     */
    void resolver(S source, T target, ResolverCompatibleMode compatibleMode) throws ResolverException;

    /**
     * 解析目标
     *
     * @param source 解析源
     * @param target 解析目标
     * @throws ResolverException
     */
    void resolver(S source, T target) throws ResolverException;
}