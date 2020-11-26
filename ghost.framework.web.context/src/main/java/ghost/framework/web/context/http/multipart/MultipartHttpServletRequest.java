package ghost.framework.web.context.http.multipart;

import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.web.context.http.HttpHeaders;
import ghost.framework.web.context.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Map;

/**
 * package: ghost.framework.web.module.multipart
 *
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:请求文件的包装请求对象
 * @Date: 2019-11-15:0:22
 */
public interface MultipartHttpServletRequest extends HttpServletRequest, MultipartRequest {
     MultipartFile getFile(String name);
     Map getFileMap();
     Iterator getFileNames();

     /**
      * Return this request's method as a convenient HttpMethod instance.
      */
     @Nullable
     HttpMethod getRequestMethod();

     /**
      * Return this request's headers as a convenient HttpHeaders instance.
      */
     HttpHeaders getRequestHeaders();
     /**
      * Return the headers associated with the specified part of the multipart request.
      * <p>If the underlying implementation supports access to headers, then all headers are returned.
      * Otherwise, the returned headers will include a 'Content-Type' header at the very least.
      */
     @Nullable
     HttpHeaders getMultipartHeaders(String paramOrFileName);
}