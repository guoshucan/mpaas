package ghost.framework.context.resolver;

import ghost.framework.context.environment.IEnvironment;

/**
 * package: ghost.framework.context.resolver
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/3/21:17:54
 */
public interface IEnvironmentResolverObjectFactory<S extends IEnvironment, T extends Object>
        extends IResolverObjectFactory<S, T> {
}