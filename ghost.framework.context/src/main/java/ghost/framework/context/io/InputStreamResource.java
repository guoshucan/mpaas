package ghost.framework.context.io;

import ghost.framework.util.Assert;

import java.io.IOException;
import java.io.InputStream;

/**
 * package: ghost.framework.module.io
 *
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2019-11-15:1:37
 */
public class InputStreamResource extends AbstractResource {
    private final InputStream inputStream;
    private final String description;
    private boolean read;

    public InputStreamResource(InputStream inputStream) {
        this(inputStream, "resource loaded through InputStream");
    }

    public InputStreamResource(InputStream inputStream, String description) {
        this.read = false;
        Assert.notNull(inputStream, "InputStream must not be null");
        this.inputStream = inputStream;
        this.description = description != null ? description : "";
    }

    public boolean exists() {
        return true;
    }

    public boolean isOpen() {
        return true;
    }

    @Override
    public String getPath() {
        return null;
    }

    public InputStream getInputStream() throws IOException, IllegalStateException {
        if (this.read) {
            throw new IllegalStateException("InputStream has already been read - do not use InputStreamResource if a stream needs to be read multiple times");
        } else {
            this.read = true;
            return this.inputStream;
        }
    }

    public String getDescription() {
        return "InputStream resource [" + this.description + "]";
    }

    @Override
    public String getExtensionName() {
        return null;
    }


    @Override
    public String getLastModifiedString() {
        return null;
    }

    public boolean equals(Object other) {
        return this == other || other instanceof InputStreamResource && ((InputStreamResource)other).inputStream.equals(this.inputStream);
    }

    public int hashCode() {
        return this.inputStream.hashCode();
    }
}
