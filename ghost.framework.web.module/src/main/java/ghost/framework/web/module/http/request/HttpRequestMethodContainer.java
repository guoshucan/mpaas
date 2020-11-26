package ghost.framework.web.module.http.request;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.utils.AntPathMatcher;
import ghost.framework.util.Assert;
import ghost.framework.util.CollectionUtils;
import ghost.framework.util.ReflectUtil;
import ghost.framework.util.StringUtils;
import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.bind.annotation.RequestMethod;
import ghost.framework.web.context.bind.annotation.RestController;
import ghost.framework.web.context.controller.IControllerExceptionHandlerContainer;
import ghost.framework.web.context.http.request.HttpRequestMethodPath;
import ghost.framework.web.context.http.request.IHttpRequestMethod;
import ghost.framework.web.context.http.request.IHttpRequestMethodContainer;
import ghost.framework.web.context.http.request.method.argument.IRequestMethodArgumentResolver;
import ghost.framework.web.context.http.request.method.argument.IRequestMethodArgumentResolverContainer;
import ghost.framework.web.context.http.request.method.returnValue.IRequestMethodReturnValueResolver;
import ghost.framework.web.context.http.request.method.returnValue.IRequestMethodReturnValueResolverContainer;
import ghost.framework.web.context.utils.WebUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.*;
/**
 * package: ghost.framework.web.module.http
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:http请求函数容器类
 * {@link HttpRequestMethodPath} 作为容器的键
 * @Date: 13:28 2020/1/31
 */
@Component
public final class HttpRequestMethodContainer extends AbstractMap<HttpRequestMethodPath, IHttpRequestMethod> implements IHttpRequestMethodContainer {
    /**
     * 日志
     */
     private Log log = LogFactory.getLog(HttpRequestMethodContainer.class);
    /**
     * 请求函数列表
     */
    private Map<HttpRequestMethodPath, IHttpRequestMethod> sortedMap = new LinkedHashMap<>();

    /**
     * 获取请求函数列表
     *
     * @return
     */
    @Override
    public Map<HttpRequestMethodPath, IHttpRequestMethod> getSortedMap() {
        return sortedMap;
    }

    /**
     * 添加请求对象
     *
     * @param key   请求路径
     * @param value 请求函数
     * @return
     */
    @Override
    public IHttpRequestMethod put(HttpRequestMethodPath key, IHttpRequestMethod value) {
        Assert.notNullOrEmpty(key, "add is key null error");
        Assert.notNull(value, "add is value null error");
        sortedMap.put(key, value);
        this.log.info("put path " + key + " method " + value.getMethod().getName());
        return value;
    }

    /**
     * 删除请求对象
     * 1、指定删除路径字符串
     * 2、指定删除 {@link IHttpRequestMethod} 对象
     * 3、指定删除注释 {@link RestController} 的控制器对象，指定键为控制器对象删除时将删除该控制器对象下全部请求函数
     *
     * @param key
     * @return
     */
    @Override
    public IHttpRequestMethod remove(Object key) {
        Assert.notNullOrEmpty(key, "remove is key null error");
        //判断删除键为请求对象处理
        if (key instanceof IHttpRequestMethod) {
            return super.remove(((IHttpRequestMethod) key).getPath());
        }
        //判断不为字符串类型处理
        if (!(key instanceof String)) {
            Class<?> c = key.getClass();
            Assert.notAnnotation(key, RestController.class, "remove is key not RestController Annotation error");
            //获取指定注释的全部注释函数
            List<Method> list = ReflectUtil.getAllAnnotationMethods(c, RequestMapping.class);
            //删除控制对象下全部请求函数
            synchronized (this.sortedMap) {
                for (Method method : list) {
                    this.sortedMap.remove(method.getAnnotation(RequestMapping.class).value());
                }
                return null;
            }
        }
        return super.remove(key);
    }

    /**
     * 注入处理函数参数解析器容器接口
     */
    @Autowired
    private IRequestMethodArgumentResolverContainer<IRequestMethodArgumentResolver> methodArgumentResolverContainer;
    /**
     * 注入处理请求函数返回值解析容器接口
     */
    @Autowired
    private IRequestMethodReturnValueResolverContainer<IRequestMethodReturnValueResolver> methodReturnValueResolverContainer;
    /**
     * 控制器全局错误处理容器接口
     */
    @Autowired
    private IControllerExceptionHandlerContainer exceptionHandlerContainer;
    /**
     * 路径匹配器
     */
    private AntPathMatcher pathMatcher = new AntPathMatcher();
    /**
     * 执行http请求函数处理
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param chain
     * @return 返回是否已经处理，如果已经处理将不再循环过滤器
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public boolean execute(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //请求路径
        String path = request.getRequestURI();
        if (this.log.isDebugEnabled()) {
            this.log.debug(request.getContentType() + ">" + path);
        }
        if(path.equals("/")){
            return false;
        }
        //获取请求函数
        IHttpRequestMethod requestMethod = null;
        Map<String, String> stringMap = new HashMap<>();
        System.out.println(pathMatcher.match("/i18n/{language}.json", path));
        //遍历路径匹配
        for (Map.Entry<HttpRequestMethodPath, IHttpRequestMethod> entry : this.sortedMap.entrySet()) {
            //匹配路径
            if (this.pathMatcher.match(entry.getKey().getPath(), path) ||
                    this.pathMatcher.matchStart(entry.getKey().getPath(), path)) {
                //找到匹配路径，获取路径请求函数对象
                requestMethod = entry.getValue();
                //判断请求模式
                if (!CollectionUtils.isEmpty(requestMethod.getRequestMethods())) {
                    //遍历比对函数注释的请求模式列表
                    boolean isMethod = false;
                    for (RequestMethod method : requestMethod.getRequestMethods()) {
                        //判断函数支持的请求模式
                        if (request.getMethod().equalsIgnoreCase(method.name())) {
                            //找到有效的请求模式注释
                            isMethod = true;
                            break;
                        }
                    }
                    //判断是否找到有效的请求模式注释
                    if (!isMethod) {
                        //未找到有效的注释请求模式，清理请求函数从新遍历往下继续寻找支持当前请求模式的请求函数
                        requestMethod = null;
                        continue;
                    }
                }
                //判断请求类型
                if (!CollectionUtils.isEmpty(requestMethod.getConsumes())) {
                    boolean is = false;
                    for (String consume : requestMethod.getConsumes()) {
                        if (consume.equals(request.getContentType())) {
                            is = true;
                            break;
                        }
                    }
                    //判断内容类型是否正确
                    if (!is) {
                        return false;
                    }
                }
                //解析地址参数值列表，为 PathVariable 注释先解析参数
                stringMap = this.pathMatcher.extractUriTemplateVariables(entry.getKey().getPath(), path);
                break;
            }
        }
        //判断没有请求函数执行下一个过滤器
        if (requestMethod == null) {
            return false;
        }
        //解析地址参数值列表，为 PathVariable 注释先解析参数
        //对地址参数进行编码
        for (Map.Entry<String, String> entry : stringMap.entrySet()) {
            entry.setValue(URLDecoder.decode(entry.getValue(), request.getCharacterEncoding()));
        }
        request.setAttribute(WebUtils.URI_TEMPLATE_VARIABLES_ATTRIBUTE, stringMap);
        //判断 RequestMapping#params 参数校验
        if (!CollectionUtils.isEmpty(requestMethod.getParams())) {
            //遍历请求是否有没带执行参数
            for (String p : requestMethod.getParams()) {
                //判断参数
                if (!stringMap.containsKey(p)) {
                    //没有带指定参数，直接返回不做处理
                    if (this.log.isDebugEnabled()) {
                        this.log.debug("request " + requestMethod.getPath() + " not param:" + p);
                    }
                    return false;
                }
            }
        }
        //处理headers参数
        //判断 RequestMapping#headers 参数校验
        if (!CollectionUtils.isEmpty(requestMethod.getHeaders())) {
            //遍历请求是否有没带执行参数
            for (String h : requestMethod.getHeaders()) {
                //判断参数
                if (StringUtils.isEmpty(request.getHeader(h))) {
                    //没有带指定参数，直接返回不做处理
                    if (this.log.isDebugEnabled()) {
                        this.log.debug("request " + requestMethod.getPath() + " not header:" + h);
                    }
                    return false;
                }
            }
        }
        //
        Throwable exception = null;
        try {
            //处理函数调用整个流程是否出现错误
            try {
                //判断函数是否返回类型
                if (requestMethod.getMethod().getReturnType().equals(Void.TYPE)) {
                    //不属于返回类型
                    requestMethod.invoke(this.methodArgumentResolverContainer.resolveMethod(request, response, requestMethod));
                } else {
                    //解析返回值
                    this.methodReturnValueResolverContainer.resolveReturnValue(
                            request,
                            response,
                            requestMethod,
                            //调用请求函数
                            requestMethod.invoke(
                                    //获取调用函数数组参数
                                    this.methodArgumentResolverContainer.resolveMethod(request, response, requestMethod)
                            )
                    );
                }
            } catch (InvocationTargetException e) {
                exception = e.getTargetException();
                //处理错误解析返回值
                this.exceptionHandlerContainer.exception(request,
                        response, requestMethod, exception);
            } catch (Exception e) {
                exception = e;
                //处理错误解析返回值
                this.exceptionHandlerContainer.exception(request,
                        response, requestMethod, exception);
            } finally {
                if (exception == null) {
                    if (response.getStatus() <= 0) {
                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                }
            }
        } catch (Throwable throwable) {
            throw new ServletException(throwable);
        }
        //判断是否已经处理了
        return response.getStatus() > 0;
    }

    /**
     * 重写实体集合
     *
     * @return
     */
    @Override
    public Set<Entry<HttpRequestMethodPath, IHttpRequestMethod>> entrySet() {
        return this.sortedMap.entrySet();
    }
}