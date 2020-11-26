package ghost.framework.context.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.List;
/**
 * 通过资源加载器 {@link IResourceLoader#getResource(String)} 接口实现加载资源
 */
public interface IResource extends ResourceBytes, IInputStreamSource {
	/**
	 * 获取路径
	 *
	 * @return
	 */
	default String getPath() {
		throw new UnsupportedOperationException(IResource.class.getName() + "#getPath()");
	}
	/**
	 * 资源文件
	 */
	default File getFile() throws IOException{
		throw new UnsupportedOperationException(IResource.class.getName() + "#getFile()");
	}
	/**
	 * 判断资源文件是否存在或有效
	 */
	boolean exists();
	/**
	 * 判断资源文件是否可读
	 * @see #getInputStream()
	 * @see #exists()
	 */
	default boolean isReadable() {
		return exists();
	}
	/**
	 * 判断资源文件是否打开
	 */
	default boolean isOpen() {
		return false;
	}
	/**
	 * 判断是否为文件资源
	 * 如果不为文件资源一般为URL资源
	 * @see #getFile()
	 */
	default boolean isFile() {
		return false;
	}
	/**
	 * Return a {@link ReadableByteChannel}.
	 * <p>It is expected that each call creates a <i>fresh</i> channel.
	 * <p>The default implementation returns {@link Channels#newChannel(InputStream)}
	 * with the result of {@link #getInputStream()}.
	 * @return the byte channel for the underlying resource (must not be {@code null})
	 * @throws NullPointerException 空指针错误
	 * @throws IOException io错误
	 * @see #getInputStream()
	 */
	default ReadableByteChannel readableChannel() throws IOException {
		return Channels.newChannel(getInputStream());
	}
	/**
	 * 资源长度
	 * @throws IOException
	 */
	long contentLength() throws IOException;
	/**
	 * 资源修改时间
	 * @throws IOException
	 */
	long lastModified() throws IOException;
	/**
	 * Determine a filename for this resource, i.e. typically the last
	 * part of the path: for example, "myfile.txt".
	 * <p>Returns {@code null} if this depend of resource does not
	 * have a filename.
	 */
	default String getFilename() {
		try {
			return this.getFile().getName();
		} catch (IOException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}
	/**
	 * Return a description for this resource,
	 * to be used for error output when working with the resource.
	 * <p>Implementations are also encouraged to return this value
	 * from their {@code toString} method.
	 * @see Object#toString()
	 */
	default String getDescription(){
		return null;
	}
	/**
	 * 获取文件扩展名
	 * @return
	 */
    String getExtensionName();
//
	/**
	 * 资源是否为目录
	 * @return
	 */
	default boolean isDirectory(){
		return false;
	}

	/**
	 * 如果资源属于目录，这里将为目录列表
	 * @return
	 */
	default List<IResource> list(){
		return null;
	}

//	/**
//	 *  @return The last modified date of this resource, or null if this cannot be determined
//	 */
//	Date getLastModified();
	/**
	 * 获取资源修改时间String格式内容
	 * @return
	 */
	String getLastModifiedString();
	default   URI getURI() throws IOException {
		return null;
	}
	default URL getURL() throws IOException {
		return null;
	}

	default IResource createRelative(String filename) throws IOException{
		return null;
	}
}