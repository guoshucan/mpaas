package ghost.framework.context.io;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
/**
 * 读取流源接口
 * @see InputStream
 * @see IResource
 * @see ByteArrayInputStream
 */
public interface IInputStreamSource {

	/**
	 * 使用 {@link ByteArrayInputStream} 作为流的源对象，在此函数返回
	 *
	 * @return
	 * @throws IOException
	 */
	default InputStream getInputStream() throws IOException {
		throw new UnsupportedOperationException(IInputStreamSource.class.getName() + "#getInputStream()");
	}
}