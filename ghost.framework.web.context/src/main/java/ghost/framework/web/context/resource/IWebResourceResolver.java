package ghost.framework.web.context.resource;

import ghost.framework.context.IGetDomain;
import ghost.framework.context.io.resource.IResourceResolver;
import ghost.framework.web.context.io.IWebResource;
import ghost.framework.web.context.server.MimeMappings;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * package: ghost.framework.web.context.http.response.resource
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:资源解析器接口
 * 解析完成将向 {@link HttpServletResponse} 输出解析数据
 * @Date: 2020/3/14:23:49
 */
public interface IWebResourceResolver extends IGetDomain, IResourceResolver {
    /**
     * 获取资源所域
     * @return
     */
    @Override
    default Object getDomain(){return null;}
    /**
     * 获取是否为默认解析器
     *
     * @return
     */
    default boolean isDef() {
        return false;
    }

    /**
     * 设置是否为默认解析器
     *
     * @param def
     */
    default void setDef(boolean def) {

    }

    /**
     * 获取类型地图
     * 格式对应 {@link MimeMappings.Mapping#getMimeType()} 的类型
     *
     * @return
     */
    default List<String> getMapping() {
        return null;
    }

    /**
     * 判断资源是否需要解析
     *
     * @param resource
     * @return
     */
    default boolean isResolver(String path, IWebResource resource) {
        if (this.isDef()) {
            return true;
        }
        if (getMapping() != null) {
            for (String s : getMapping()) {
                //*表示任何类型，一般对应默认类型
                if (s.equals(resource.getExtensionName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 解析
     *
     * @param request
     * @param response
     */
    void resolver(String path, HttpServletRequest request, HttpServletResponse response, final IWebResource resource) throws IOException;
}