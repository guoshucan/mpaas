package ghost.framework.context.io.resource;

import java.security.ProtectionDomain;

/**
 * package: ghost.framework.context.io.resource
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:资源解析器接口
 * @Date: 2020/6/2:10:23
 */
public interface IResourceResolver {
    /**
     * 获取资源解析器所在的域
     * 一般作为jar的域所在位置
     * @return
     */
    default ProtectionDomain getProtectionDomain() {
        throw new UnsupportedOperationException(IResourceResolver.class.getName() + "#getProtectionDomain()");
    }
}
