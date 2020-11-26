package ghost.framework.web.mvc.thymeleaf.context;

import ghost.framework.context.io.IResource;
import ghost.framework.context.io.IResourceDomain;
import ghost.framework.util.Assert;
import ghost.framework.web.context.io.WebIResourceLoader;
import org.thymeleaf.templateresource.ITemplateResource;
import org.thymeleaf.util.StringUtils;

import java.io.*;
/**
 * package: ghost.framework.web.mvc.thymeleaf.context
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模板资源加载
 * @Date: 2020/6/1:21:55
 */
public class ThymeleafTemplateResource implements ITemplateResource {
    /**
     * web资源加载器接口
     */
    private WebIResourceLoader<IResourceDomain> resourceLoader;
    private final IResource resource;
    private final String characterEncoding;

    /**
     * 初始化模板资源加载
     *
     * @param resourceLoader    web资源加载器接口
     * @param location
     * @param characterEncoding
     */
    public ThymeleafTemplateResource(WebIResourceLoader<IResourceDomain> resourceLoader, final String location, final String characterEncoding) {
        this.resourceLoader = resourceLoader;
        Assert.notNull(resourceLoader, "ThymeleafTemplateResource resourceLoader cannot be null");
        Assert.notNullOrEmpty(location, "ThymeleafTemplateResource location cannot be null or empty");
        Assert.notNullOrEmpty(location, "ThymeleafTemplateResource characterEncoding cannot be null or empty");
        // Character encoding CAN be null (system default will be used)
        this.resource = resourceLoader.getResource(location);
        this.characterEncoding = characterEncoding;
    }

    /**
     * 初始化模板资源加载
     *
     * @param resource          资源接口
     * @param characterEncoding
     */
    public ThymeleafTemplateResource(final IResource resource, final String characterEncoding) {
        Assert.notNull(resource, "ThymeleafTemplateResource resource cannot be null or empty");
        // Character encoding CAN be null (system default will be used)
        this.resource = resource;
        this.characterEncoding = characterEncoding;

    }

    @Override
    public String getDescription() {
        return this.resource.getDescription();
    }

    @Override
    public String getBaseName() {
        return computeBaseName(this.resource.getFilename());
    }

    @Override
    public boolean exists() {
        return this.resource.exists();
    }

    @Override
    public Reader reader() throws IOException {

        // Will never return null, but an IOException if not found
        final InputStream inputStream = this.resource.getInputStream();

        if (!StringUtils.isEmptyOrWhitespace(this.characterEncoding)) {
            return new BufferedReader(new InputStreamReader(new BufferedInputStream(inputStream), this.characterEncoding));
        }

        return new BufferedReader(new InputStreamReader(new BufferedInputStream(inputStream)));

    }

    @Override
    public ITemplateResource relative(final String relativeLocation) {
        final IResource relativeResource;
        try {
            relativeResource = this.resourceLoader.getResource(relativeLocation);
        } catch (Exception e) {
            // Given we have delegated the createRelative(...) mechanism to Spring, it's better if we don't do
            // any assumptions on what this IOException means and simply return a resource object that returns
            // no reader and exists() == false.
            return new ResourceInvalidRelativeTemplateResource(getDescription(), relativeLocation, new IOException(e));
        }
        return new ThymeleafTemplateResource(relativeResource, this.characterEncoding);
    }

    static String computeBaseName(final String path) {

        if (path == null || path.length() == 0) {
            return null;
        }

        // First remove a trailing '/' if it exists
        final String basePath = (path.charAt(path.length() - 1) == '/' ? path.substring(0, path.length() - 1) : path);

        final int slashPos = basePath.lastIndexOf('/');
        if (slashPos != -1) {
            final int dotPos = basePath.lastIndexOf('.');
            if (dotPos != -1 && dotPos > slashPos + 1) {
                return basePath.substring(slashPos + 1, dotPos);
            }
            return basePath.substring(slashPos + 1);
        } else {
            final int dotPos = basePath.lastIndexOf('.');
            if (dotPos != -1) {
                return basePath.substring(0, dotPos);
            }
        }

        return (basePath.length() > 0 ? basePath : null);

    }

    private static final class ResourceInvalidRelativeTemplateResource implements ITemplateResource {

        private final String originalResourceDescription;
        private final String relativeLocation;
        private final IOException ioException;

        ResourceInvalidRelativeTemplateResource(
                final String originalResourceDescription,
                final String relativeLocation,
                final IOException ioException) {
            super();
            this.originalResourceDescription = originalResourceDescription;
            this.relativeLocation = relativeLocation;
            this.ioException = ioException;
        }

        @Override
        public String getDescription() {
            return "Invalid relative resource for relative location \"" + this.relativeLocation +
                    "\" and original resource " + this.originalResourceDescription + ": " + this.ioException.getMessage();
        }

        @Override
        public String getBaseName() {
            return "Invalid relative resource for relative location \"" + this.relativeLocation +
                    "\" and original resource " + this.originalResourceDescription + ": " + this.ioException.getMessage();
        }

        @Override
        public boolean exists() {
            return false;
        }

        @Override
        public Reader reader() throws IOException {
            throw new IOException("Invalid relative resource", this.ioException);
        }

        @Override
        public ITemplateResource relative(final String relativeLocation) {
            return this;
        }

        @Override
        public String toString() {
            return getDescription();
        }
    }
}