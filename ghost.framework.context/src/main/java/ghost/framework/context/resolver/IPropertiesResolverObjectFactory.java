package ghost.framework.context.resolver;

import java.util.Properties;

/**
 * package: ghost.framework.context.resolver
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description: {@link Properties}解析对象接口
 * @Date: 2020/3/21:16:50
 * @param <S> 解析源
 * @param <T> 解析目标
 */
public interface IPropertiesResolverObjectFactory<S extends Properties, T extends Object>
        extends IResolverObjectFactory<S, T> {
}