package ghost.framework.web.context.http.multipart;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.util.MultiValueMap;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * package: ghost.framework.web.module.multipart
 *
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:请求文件的包装请求对象接口
 * @Date: 2019-11-15:0:32
 */
public interface MultipartRequest {
    /**
     * Return an {@link java.util.Iterator} of String objects containing the
     * parameter names of the multipart files contained in this request. These
     * are the field names of the form (like with normal parameters), not the
     * original file names.
     * @return the names of the files
     */
    Iterator<String> getFileNames();

    /**
     * Return the contents plus description of an uploaded file in this request,
     * or {@code null} if it does not exist.
     * @param name a String specifying the parameter name of the multipart file
     * @return the uploaded content in the form of a {@link MultipartFile} object
     */
    @Nullable
    MultipartFile getFile(String name);

    /**
     * Return the contents plus description of uploaded files in this request,
     * or an empty list if it does not exist.
     * @param name a String specifying the parameter name of the multipart file
     * @return the uploaded content in the form of a {@link MultipartFile} list
     * @since 3.0
     */
    List<MultipartFile> getFiles(String name);

    /**
     * Return a {@link java.util.Map} of the multipart files contained in this request.
     * @return a map containing the parameter names as keys, and the
     * {@link MultipartFile} objects as values
     */
    Map<String, MultipartFile> getFileMap();

    /**
     * Return a {@link MultiValueMap} of the multipart files contained in this request.
     * @return a map containing the parameter names as keys, and a list of
     * {@link MultipartFile} objects as values
     * @since 3.0
     */
    MultiValueMap<String, MultipartFile> getMultiFileMap();

    /**
     * Determine the content type of the specified request part.
     * @param paramOrFileName the name of the part
     * @return the associated content type, or {@code null} if not defined
     * @since 3.1
     */
    @Nullable
    String getMultipartContentType(String paramOrFileName);

}
