package ghost.framework.context.io;

import ghost.framework.util.Assert;

/**
 * package: ghost.framework.context.io
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/3/15:1:30
 */
public class AbstractResourceBytes implements ResourceBytes {
    public AbstractResourceBytes(byte[] bytes, long lastModified) {
        Assert.notNull(bytes, "AbstractResourceBytes in stream null error");
        this.bytes = bytes;
        this.lastModified = lastModified;
    }

    private final long lastModified;

    @Override
    public long lastModified() {
        return lastModified;
    }

    private final byte[] bytes;

    @Override
    public byte[] getBytes() {
        return bytes;
    }
}