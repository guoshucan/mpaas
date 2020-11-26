package ghost.framework.web.module.http.request.method.returnValue;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.resolver.ReturnValueResolverException;
import ghost.framework.util.Assert;
import ghost.framework.web.context.bind.annotation.ResponseStatus;
import ghost.framework.web.context.http.MediaType;
import ghost.framework.web.context.http.request.IHttpRequestMethod;
import ghost.framework.web.context.http.request.method.argument.IRequestMethodArgumentResolverContainer;
import ghost.framework.web.context.http.request.method.returnValue.IRequestMethodReturnValueResolver;
import ghost.framework.web.context.http.request.method.returnValue.IRequestMethodReturnValueResolverContainer;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * package: ghost.framework.web.module.http.request.method.returnValue
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:处理请求函数返回值解析器容器
 * 处理由 {@link IRequestMethodArgumentResolverContainer#resolveMethod} 处理返回的值做解析
 * @Date: 2020/2/29:16:53
 */
@Component
public class RequestMethodReturnValueResolverContainer<V extends IRequestMethodReturnValueResolver>
        implements IRequestMethodReturnValueResolverContainer<V> {
    private List<IRequestMethodReturnValueResolver> list = new ArrayList<>();
    /**
     * 添加返回值解析器
     *
     * @param returnValueResolver 解析器对象
     * @return
     */
    @Override
    public boolean add(V returnValueResolver) {
        Assert.notNull(returnValueResolver, "add is returnValueResolver null error");
        synchronized (list) {
            if (returnValueResolver.isDefault()) {
                for (IRequestMethodReturnValueResolver resolver : this.list) {
                    if (resolver.isDefault()) {
                        resolver.setDefault(false);
                    }
                }
            }
            boolean is = list.add(returnValueResolver);
            if (returnValueResolver.isDefault()) {
                this.defaultResolver = returnValueResolver;
            }
            return is;
        }
    }

    /**
     * 删除返回值解析器
     *
     * @param returnValueResolver 解析器对象
     * @return
     */
    @Override
    public boolean remove(V returnValueResolver) {
        Assert.notNull(returnValueResolver, "remove is returnValueResolver null error");
        synchronized (list) {
            IRequestMethodReturnValueResolver resolver = (IRequestMethodReturnValueResolver) returnValueResolver;
            boolean is = list.remove(resolver);
            //判断删除解析器是否为默认解析器
            if (is && resolver.isDefault()) {
                //默认解析器被删除，重新选择默认使用 MediaType.APPLICATION_JSON_VALUE 格式解析的截器作为默认解析器
                for (IRequestMethodReturnValueResolver valueResolver : this.list) {
                    //遍历返回解析参数
                    if (valueResolver.getProduces() != null) {
                        for (String p : valueResolver.getProduces()) {
                            //比对解析类型
                            if (p.equals(MediaType.APPLICATION_JSON_VALUE)) {
                                //找到对应 MediaType.APPLICATION_JSON_VALUE 解析器，设置为默认解析器
                                valueResolver.setDefault(true);
                                //设置其它解析器不为默认解析器
                                for (IRequestMethodReturnValueResolver falseDefaultValueResolver : this.list) {
                                    if (!falseDefaultValueResolver.equals(valueResolver)) {
                                        falseDefaultValueResolver.setDefault(false);
                                    }
                                }
                                //从新设置容器默认解析器
                                this.defaultResolver = valueResolver;
                                return is;
                            }
                        }
                    }
                }
            }
            return is;
        }
    }

    /**
     * 默认解析器
     */
    private IRequestMethodReturnValueResolver defaultResolver;

    /**
     * 获取默认解析器
     *
     * @return
     */
    @Override
    public IRequestMethodReturnValueResolver getDefault() {
        return defaultResolver;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean contains(V v) {
        return list.contains(v);
    }

    /**
     * 解析函数返回值
     *
     * @param requestMethod 请求函数
     * @param returnValue   请求函数返回值对象
     * @throws ReturnValueResolverException
     */
    @Override
    public void resolveReturnValue(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                   @NotNull IHttpRequestMethod requestMethod, @NotNull Object returnValue) throws ReturnValueResolverException {
        //请求默认请求格式
        if (this.logger.isDebugEnabled()) {
            Enumeration<String> enumeration = request.getHeaderNames();
            while (enumeration.hasMoreElements()) {
                String s = enumeration.nextElement();
                this.logger.debug("Header:" + s + "=" + request.getHeader(s));
            }
        }
        //返回值解析器处理完成后的事情
        try {
            //遍历判断注释指定控制器注释的返回值解析器
            for (IRequestMethodReturnValueResolver resolver : this.list) {
                if (resolver.isAnnotationController(requestMethod.getControllerClass())) {
                    resolver.handleReturnValue(request, response, requestMethod, returnValue);
                    return;
                }
            }
            //判断是否有指定返回处理格式
            if (requestMethod.getProduces().length == 0) {
                this.defaultResolver.handleReturnValue(request, response, requestMethod, returnValue);
                return;
            }
            //有指定处理标准
            for (IRequestMethodReturnValueResolver resolver : this.list) {
                //判断是否可以解析
                if (resolver.isResolver(request, response, requestMethod, returnValue)) {
                    //没有注释返回值控制器注释处理返回值
                    resolver.handleReturnValue(request, response, requestMethod, returnValue);
                    return;
                }
//                //判断没有注释控制器类型的返回值解析器
//                if (CollectionUtils.isEmpty(resolver.getAnnotationControllers())) {
//                    //判断是否可以解析
//                    if (resolver.isResolver(request, response, requestMethod, returnValue)) {
//                        //没有注释返回值控制器注释处理返回值
//                        resolver.handleReturnValue(request, response, requestMethod, returnValue);
//                        return;
//                    }
//                } else {
//                    //判断是否有注释返回值控制器注释
//                    if (resolver.isAnnotationController(requestMethod.getControllerClass()) &&
//                            resolver.isResolver(request, response, requestMethod, returnValue)) {
//                        resolver.handleReturnValue(request, response, requestMethod, returnValue);
//                        return;
//                    }
//                }
//                //寻找不为默认解析器与有指定解析参数
//                if (resolver.getProduces() != null) {
//                    for (String s : resolver.getProduces()) {
//                        for (String p : requestMapping.produces()) {
//                            if (s.equals(p)) {
//                                //找到解析器
//                                resolver.handleReturnValue(context, requestMethod, returnValue);
//                                return;
//                            }
//                        }
//                    }
//                }
            }
        } finally {
            //处理全局错误代码
            //判断函数是否注释返回状态码
            if (requestMethod.getMethod().isAnnotationPresent(ResponseStatus.class)) {
                ResponseStatus status = requestMethod.getMethod().getAnnotation(ResponseStatus.class);
                if (status.reason().equals("")) {
                    response.setStatus(status.value().value());
                } else {
                    try {
                        response.sendError(status.value().value(), status.reason());
                    } catch (IOException e) {
                        throw new ReturnValueResolverException(e.getMessage(), e);
                    }
                }
            }
        }
    }

    private Logger logger = Logger.getLogger(RequestMethodReturnValueResolverContainer.class);
}