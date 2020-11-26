package ghost.framework.context.io.resource;

import java.util.Collection;

/**
 * package: ghost.framework.context.io.resource
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:资源解析器容器接口
 * @Date: 2020/6/2:10:23
 * @param <T> 资源解析器接口
 */
public interface IResourceResolverContainer<T extends IResourceResolver> extends Collection<T> {
}