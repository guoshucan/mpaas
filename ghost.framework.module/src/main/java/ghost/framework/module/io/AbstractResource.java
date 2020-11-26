package ghost.framework.module.io;

import ghost.framework.module.io.support.Resource;
import ghost.framework.util.NestedIOException;
import ghost.framework.util.ResourceUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * package: ghost.framework.module.io
 *
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2019-11-15:1:38
 */
public abstract class AbstractResource implements Resource {
    public AbstractResource() {
    }

    public boolean exists() {
        Log logger;
        if (this.isFile()) {
            try {
                return this.getFile().exists();
            } catch (IOException var4) {
                logger = LogFactory.getLog(this.getClass());
                if (logger.isDebugEnabled()) {
                    logger.debug("Could not retrieve File for existence check of " + this.getDescription(), var4);
                }
            }
        }

        try {
            this.getInputStream().close();
            return true;
        } catch (Throwable var3) {
            logger = LogFactory.getLog(this.getClass());
            if (logger.isDebugEnabled()) {
                logger.debug("Could not retrieve InputStream for existence check of " + this.getDescription(), var3);
            }

            return false;
        }
    }

    public boolean isReadable() {
        return this.exists();
    }

    public boolean isOpen() {
        return false;
    }

    public boolean isFile() {
        return false;
    }

    public URL getURL() throws IOException {
        throw new FileNotFoundException(this.getDescription() + " cannot be resolved to URL");
    }

    public URI getURI() throws IOException {
        URL url = this.getURL();

        try {
            return ResourceUtils.toURI(url);
        } catch (URISyntaxException var3) {
            throw new NestedIOException("Invalid URI [" + url + "]", var3);
        }
    }

    public File getFile() throws IOException {
        throw new FileNotFoundException(this.getDescription() + " cannot be resolved to absolute file path");
    }

    public ReadableByteChannel readableChannel() throws IOException {
        return Channels.newChannel(this.getInputStream());
    }

    public long contentLength() throws IOException {
        InputStream is = this.getInputStream();
        boolean var16 = false;

        long var6;
        try {
            var16 = true;
            long size = 0L;
            byte[] buf = new byte[256];

            while(true) {
                int read;
                if ((read = is.read(buf)) == -1) {
                    var6 = size;
                    var16 = false;
                    break;
                }

                size += (long)read;
            }
        } finally {
            if (var16) {
                try {
                    is.close();
                } catch (IOException var17) {
                    Log logger = LogFactory.getLog(this.getClass());
                    if (logger.isDebugEnabled()) {
                        logger.debug("Could not close content-length InputStream for " + this.getDescription(), var17);
                    }
                }

            }
        }

        try {
            is.close();
        } catch (IOException var18) {
            Log logger = LogFactory.getLog(this.getClass());
            if (logger.isDebugEnabled()) {
                logger.debug("Could not close content-length InputStream for " + this.getDescription(), var18);
            }
        }

        return var6;
    }

    public long lastModified() throws IOException {
        File fileToCheck = this.getFileForLastModifiedCheck();
        long lastModified = fileToCheck.lastModified();
        if (lastModified == 0L && !fileToCheck.exists()) {
            throw new FileNotFoundException(this.getDescription() + " cannot be resolved in the file system for checking its last-modified timestamp");
        } else {
            return lastModified;
        }
    }

    protected File getFileForLastModifiedCheck() throws IOException {
        return this.getFile();
    }

    public Resource createRelative(String relativePath) throws IOException {
        throw new FileNotFoundException("Cannot create a relative resource for " + this.getDescription());
    }

    
    public String getFilename() {
        return null;
    }

    public boolean equals( Object other) {
        return this == other || other instanceof Resource && ((Resource)other).getDescription().equals(this.getDescription());
    }

    public int hashCode() {
        return this.getDescription().hashCode();
    }

    public String toString() {
        return this.getDescription();
    }
}
