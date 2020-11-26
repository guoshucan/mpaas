package ghost.framework.web.module.http.request.method.argument;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.order.FirstOrder;
import ghost.framework.beans.annotation.order.Order;
import ghost.framework.context.parameter.NameParameter;
import ghost.framework.context.resolver.ResolverException;
import ghost.framework.web.context.bens.annotation.HandlerMethodArgumentResolver;
import ghost.framework.web.context.bind.annotation.RequestParam;
import ghost.framework.web.context.http.multipart.MultipartFile;
import ghost.framework.web.context.http.multipart.MultipartHttpServletRequest;
import ghost.framework.web.context.http.request.IHttpRequestMethod;
import ghost.framework.web.context.http.request.method.argument.AbstractRequestMethodArgumentAnnotationResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * package: ghost.framework.web.module.http.request.method.argument
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/3/7:15:14
 */
@FirstOrder
@Order(Integer.MAX_VALUE)
@HandlerMethodArgumentResolver
public class RequestMethodArgumentMultipartFileRequestParamAnnotationResolver
        extends AbstractRequestMethodArgumentAnnotationResolver {
    private final Class<? extends Annotation> annotation = RequestParam.class;

    /**
     * 重写注释类型
     *
     * @return
     */
    @Override
    public Class<? extends Annotation> getAnnotation() {
        return annotation;
    }

    private final Class<?>[] types = new Class[]
            {
                    MultipartFile.class,
                    MultipartFile[].class
            };

    @Override
    public boolean isResolver(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                              @NotNull IHttpRequestMethod requestMethod, @NotNull NameParameter parameter) throws ResolverException {
        return this.isAnnotation(request, response, requestMethod, parameter) && this.isResolverType(request, response, requestMethod, parameter);
    }
//    private final String[] methods = new String[]{RequestMethod.POST.name()};
//
//    @Override
//    public String[] getMethods() {
//        return methods;
//    }

    /**
     * 重写解析类型
     *
     * @return
     */
    @Override
    public Class<?>[] getResolverTypes() {
        return types;
    }

    /**
     * 解析参数
     *
     * @param requestMethod 请求函数
     * @param parameter     控制器函数参数
     * @return
     * @throws ResolverException
     */
    @Override
    public Object resolveArgument(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                  @NotNull IHttpRequestMethod requestMethod, @NotNull NameParameter parameter) throws ResolverException {
        //声明文件接口
        MultipartFile file = null;
        //获取注释
        RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
        //获取图片请求对象
        MultipartHttpServletRequest servletRequest = (MultipartHttpServletRequest) request;
        //判断为单个文件参数
        if (parameter.getType().equals(MultipartFile.class)) {
            //获取文件
            file = servletRequest.getFile(requestParam.value().equals("") ? parameter.getName() : requestParam.value());
            //判断参数是否有效，默认为空参数错误
            if (requestParam.required() && file == null) {
                throw new ResolverException(parameter.toString());
            }
        }
        //判断为数组文件参数
        //格式MultipartFile[]
        if (parameter.getType().equals(MultipartFile[].class)) {
            MultipartFile[] files = null;
            if (requestParam.value().equals("")) {
                files = (MultipartFile[]) servletRequest.getFileMap().values().toArray();
            } else {
                files = (MultipartFile[]) servletRequest.getFiles(requestParam.value()).toArray();
            }
            //判断参数是否有效，默认为空参数错误
            if (requestParam.required() && files == null) {
                throw new ResolverException(parameter.toString());
            }
            return files;
        }
        //判断是否为列表参数
        //格式List<MultipartFile>
        if (parameter.getType().equals(List.class)) {
            //判断参数是否有效，默认为空参数错误
            if (requestParam.required() && servletRequest.getFileMap().size() == 0) {
                throw new ResolverException(parameter.toString());
            }
            return new ArrayList(servletRequest.getFileMap().values());
        }
        //判断是否为列表参数
        //格式Map<String, MultipartFile>
        if (parameter.getType().equals(Map.class)) {
            //判断参数是否有效，默认为空参数错误
            if (requestParam.required() && servletRequest.getFileMap().size() == 0) {
                throw new ResolverException(parameter.toString());
            }
            return servletRequest.getFileMap();
        }
        //解析类型
        return file;
    }
}