package ghost.framework.context.io;

import ghost.framework.util.Assert;

import java.io.IOException;
import java.io.InputStream;

/**
 * package: ghost.framework.context.io
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/3/14:17:56
 */
public class AbstractInputStreamResource implements IInputStreamResource {
    public AbstractInputStreamResource(InputStream stream, long lastModified) {
        Assert.notNull(stream, "AbstractInputStreamResource in stream null error");
        this.stream = stream;
        this.lastModified = lastModified;
    }

    private final InputStream stream;
    private final long lastModified;

    @Override
    public long getLastModified() {
        return lastModified;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return stream;
    }
}