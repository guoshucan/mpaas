package ghost.framework.web.context.http.multipart;

import java.io.IOException;
import java.nio.file.Path;

/**
 * package: ghost.framework.web.module.multipart
 *
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:请求文件的包装
 * @Date: 2019-11-15:0:21
 */
public interface MultipartFile {
    byte[] getBytes();
    /**
     * 方法获得文件类型，以此决定允许上传的文件类型。
     * @return
     */
    String getContentType();

    java.io.InputStream getInputStream() throws IOException;

    String getName();

    String getOriginalFilename();

    /**
     * 方法获得文件长度，以此决定允许上传的文件大小。
     * @return
     */
    long getSize();

    /**
     * 方法判断上传文件是否为空文件，以此决定是否拒绝空文件。
     * @return
     */
    boolean isEmpty();
    /**
     * 方法将上传文件写到服务器上指定的文件。
     * @param dest
     */
    void transferTo(Path dest) throws IOException, IllegalStateException;
    /**
     * 方法将上传文件写到服务器上指定的文件。
     * @param dest
     */
    void transferTo(java.io.File dest) throws IOException, IllegalStateException;
}