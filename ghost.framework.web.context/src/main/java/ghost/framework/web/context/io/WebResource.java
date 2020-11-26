package ghost.framework.web.context.io;

import ghost.framework.context.io.IResourceDomain;
import ghost.framework.util.Assert;
import ghost.framework.util.FileUtil;
import ghost.framework.web.context.utils.ETag;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * package: ghost.framework.web.context.io
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:web资源
 * @Date: 2020/3/14:18:29
 */
public class WebResource implements IWebResource {
    public WebResource(String path, long lastModified, byte[] bytes, IResourceDomain domain) {
        Assert.notNullOrEmpty(path, "WebResource in path null error");
        Assert.notNull(bytes, "WebResource in bytes null error");
        Assert.notNull(domain, "WebResource in domain null error");
        this.path = path;
        //初始化文件扩展名
        this.extensionName = FileUtil.getExtensionName(this.path);
        this.lastModified = lastModified;
        this.bytes = bytes;
        this.domain = domain;
        //初始化资源过期时间
        this.expiredDate = new Date();
    }

    /**
     * 过期时间
     */
    private Date expiredDate;

    /**
     * 获取过期时间
     * 主要在定时器处理超多一定时间时被释放资源
     *
     * @return
     */
    @Override
    public Date getExpiredDate() {
        return expiredDate;
    }

    private final String path;

    @Override
    public boolean exists() {
        return false;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public long contentLength() {
        return this.bytes.length;
    }

    private final long lastModified;

    @Override
    public long lastModified() {
        return lastModified;
    }

    @Override
    public String getFilename() {
        return null;
    }

    @Override
    public String getExtensionName() {
        return extensionName;
    }

    private final String extensionName;

    @Override
    public String getLastModifiedString() {
        return null;
    }

    private final byte[] bytes;

    @Override
    public byte[] getBytes() {
        return bytes;
    }

    /**
     * 重写获取流
     *
     * @return
     * @throws IOException
     */
    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(this.bytes);
    }
    private final IResourceDomain domain;

    @Override
    public IResourceDomain getDomain() {
        return domain;
    }

    @Override
    public ETag getETag() {
        return null;
    }
}