package ghost.framework.web.context.http.request;

import ghost.framework.web.context.bind.annotation.RequestMethod;

import java.util.Arrays;
import java.util.Objects;

/**
 * package: ghost.framework.web.context.http.request
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:请求函数 {@link IHttpRequestMethodContainer} 的键
 * @Date: 2020/6/3:12:59
 */
public class HttpRequestMethodPath {
    private final String path;

    public String getPath() {
        return path;
    }

    private final IHttpRequestMethod requestMethod;

    public RequestMethod[] getRequestMethods() {
        return requestMethod.getRequestMethods();
    }

    public HttpRequestMethodPath(final String path, final IHttpRequestMethod requestMethod) {
        this.path = path;
        this.requestMethod = requestMethod;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpRequestMethodPath that = (HttpRequestMethodPath) o;
        return path.equals(that.path) &&
                Arrays.equals(requestMethod.getRequestMethods(), that.getRequestMethods());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(path);
        result = 31 * result + Arrays.hashCode(requestMethod.getRequestMethods());
        return result;
    }

    @Override
    public String toString() {
        return "HttpRequestMethodPath{" +
                "path='" + path + '\'' +
                ", requestMethods=" + Arrays.toString(requestMethod.getRequestMethods()) +
                '}';
    }
}