package ghost.framework.module.io.support;

import ghost.framework.util.Assert;

/**
 * package: ghost.framework.module.io.support
 *
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2019-11-15:1:34
 */
public class ResourceRegion {
    private final Resource resource;
    private final long position;
    private final long count;

    public ResourceRegion(Resource resource, long position, long count) {
        Assert.notNull(resource, "Resource must not be null");
        Assert.isTrue(position >= 0L, "'position' must be larger than or equal to 0");
        Assert.isTrue(count >= 0L, "'count' must be larger than or equal to 0");
        this.resource = resource;
        this.position = position;
        this.count = count;
    }

    public Resource getResource() {
        return this.resource;
    }

    public long getPosition() {
        return this.position;
    }

    public long getCount() {
        return this.count;
    }
}
