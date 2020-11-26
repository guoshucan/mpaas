package ghost.framework.web.module.http.request;

import ghost.framework.context.method.MethodTarget;
import ghost.framework.context.parameter.NameParameter;
import ghost.framework.context.proxy.ProxyUtil;
import ghost.framework.util.Assert;
import ghost.framework.web.context.bind.annotation.*;
import ghost.framework.web.context.http.request.HttpRequestMethodProxy;
import ghost.framework.web.context.http.request.IHttpRequestMethod;
import ghost.framework.web.context.http.request.IHttpRequestMethodContainer;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:http请求函数
 * @Date: 11:20 2019-10-06
 */
public final class HttpRequestMethod extends MethodTarget implements IHttpRequestMethod {
    /**
     * 路径规范
     *
     * @param controllerPath 控制器注释
     * @param mappingPath    请求路径
     * @return 返回合并后的请求完整路径
     */
    static String pathSpecification(String controllerPath, String mappingPath) {
        //规范路径
        if (controllerPath.equals("")) {
            if (mappingPath.startsWith("/")) {
                return mappingPath;
            } else {
                return "/" + mappingPath;
            }
        } else {
            if (controllerPath.startsWith("/")) {
                if (mappingPath.startsWith("/")) {
                    return controllerPath + mappingPath;
                } else {
                    return controllerPath + "/" + mappingPath;
                }
            } else {
                if (mappingPath.startsWith("/")) {
                    return "/" + controllerPath + mappingPath;
                } else {
                    return "/" + controllerPath + "/" + mappingPath;
                }
            }
        }
    }

    /**
     * 函数扩展属性地图
     */
    private Map<String, Object> attributes = new HashMap<>();

    /**
     * 设置函数扩展属性
     * {@link IHttpRequestMethod#setAttribute(String, Object)}
     *
     * @param name 属性名称
     * @param o    属性对象
     */
    @Override
    public void setAttribute(String name, Object o) {
        this.attributes.put(name, o);
    }

    /**
     * 获取函数扩展属性
     * {@link IHttpRequestMethod#getAttribute(String)}
     *
     * @param name 属性名称
     * @return
     */
    @Override
    public Object getAttribute(String name) {
        return this.attributes.get(name);
    }

    /**
     * 获取函数扩展属性列表
     * {@link IHttpRequestMethod#getAttributeNames()}
     *
     * @return
     */
    @Override
    public Set<String> getAttributeNames() {
        return this.attributes.keySet();
    }

    /**
     * 请求函数路径注释
     * {@link RequestMapping}
     * {@link DeleteMapping}
     * {@link GetMapping}
     * {@link PatchMapping}
     * {@link PostMapping}
     * {@link PutMapping}
     */
    protected Annotation requestMapping;

    /**
     * 获取请求路径注释
     * {@link IHttpRequestMethod#getRequestMapping()}
     * {@link RequestMapping}
     * {@link DeleteMapping}
     * {@link GetMapping}
     * {@link PatchMapping}
     * {@link PostMapping}
     * {@link PutMapping}
     *
     * @return
     */
    @Override
    public Annotation getRequestMapping() {
        return requestMapping;
    }

    /**
     * 获取指定处理请求的提交内容类型（Content-Type），例如application/json, text/html;
     * {@link IHttpRequestMethod#getConsumes()}
     * {@link RequestMapping#consumes()}
     * {@link DeleteMapping#consumes()}
     * {@link GetMapping#consumes()}
     * {@link PatchMapping#consumes()}
     * {@link PostMapping#consumes()}
     * {@link PutMapping#consumes()}
     *
     * @return
     */
    @Override
    public String[] getConsumes() {
        return consumes;
    }

    /**
     * {@link RequestMapping#consumes()}
     * {@link DeleteMapping#consumes()}
     * {@link GetMapping#consumes()}
     * {@link PatchMapping#consumes()}
     * {@link PostMapping#consumes()}
     * {@link PutMapping#consumes()}
     */
    private final String[] consumes;

    /**
     * {@link IHttpRequestMethod#getProduces()}
     * {@link RequestMapping#produces()}
     * {@link DeleteMapping#produces()}
     * {@link GetMapping#produces()}
     * {@link PatchMapping#produces()}
     * {@link PostMapping#produces()}
     * {@link PutMapping#produces()}
     *
     * @return
     */
    @Override
    public String[] getProduces() {
        return produces;
    }

    /**
     * {@link RequestMapping#produces()}
     * {@link DeleteMapping#produces()}
     * {@link GetMapping#produces()}
     * {@link PatchMapping#produces()}
     * {@link PostMapping#produces()}
     * {@link PutMapping#produces()}
     */
    private final String[] produces;

    /**
     * {@link IHttpRequestMethod#getHeaders()}
     * {@link RequestMapping#headers()}
     * {@link DeleteMapping#headers()}
     * {@link GetMapping#headers()}
     * {@link PatchMapping#headers()}
     * {@link PostMapping#headers()}
     * {@link PutMapping#headers()}
     *
     * @return
     */
    @Override
    public String[] getHeaders() {
        return headers;
    }

    /**
     * {@link RequestMapping#headers()}
     * {@link DeleteMapping#headers()}
     * {@link GetMapping#headers()}
     * {@link PatchMapping#headers()}
     * {@link PostMapping#headers()}
     * {@link PutMapping#headers()}
     */
    private final String[] headers;

    /**
     * {@link IHttpRequestMethod#getParams()}
     * {@link RequestMapping#params()}
     * {@link DeleteMapping#params()}
     * {@link GetMapping#params()}
     * {@link PatchMapping#params()}
     * {@link PostMapping#params()}
     * {@link PutMapping#params()}
     *
     * @return
     */
    @Override
    public String[] getParams() {
        return params;
    }

    /**
     * {@link RequestMapping#params()}
     * {@link DeleteMapping#params()}
     * {@link GetMapping#params()}
     * {@link PatchMapping#params()}
     * {@link PostMapping#params()}
     * {@link PutMapping#params()}
     */
    private final String[] params;

    /**
     * 初始化http请求函数
     *
     * @param target         函数拥有者
     * @param controller     控制器注释
     *                       {@link RestController}
     *                       {@see Controller}
     * @param method         函数对象
     * @param requestMapping 请求路径注释
     *                       {@link RequestMapping}
     *                       {@link DeleteMapping}
     *                       {@link GetMapping}
     *                       {@link PatchMapping}
     *                       {@link PostMapping}
     *                       {@link PutMapping}
     */
    public HttpRequestMethod(Object target, Annotation controller, Method method, Annotation requestMapping) {
        super(target, method);
        Assert.notNull(target, "HttpRequestMethod is controller null error");
        Assert.notNull(target, "HttpRequestMethod is method null error");
        Assert.notNull(target, "HttpRequestMethod is requestMapping null error");
        //控制器注释
        this.controller = controller;
        //获取控制器注释类型
        this.controllerClass = ProxyUtil.getProxyObjectAnnotationClass(this.controller);
        this.requestMapping = requestMapping;
        //获取注释的map地图
        Map<String, Object> map = ProxyUtil.getProxyAnnotationMap(controller);
        //规范路径
        if (requestMapping instanceof RequestMapping) {
            RequestMapping mapping = (RequestMapping) requestMapping;
            this.path = pathSpecification(map.get("value").toString(), mapping.value());
            this.consumes = mapping.consumes();
            this.produces = mapping.produces();
            this.headers = mapping.headers();
            this.params = mapping.params();
            this.requestMethods = mapping.method();
        } else if (requestMapping instanceof DeleteMapping) {
            DeleteMapping mapping = (DeleteMapping) requestMapping;
            this.path = pathSpecification(map.get("value").toString(), mapping.value());
            this.consumes = mapping.consumes();
            this.produces = mapping.produces();
            this.headers = mapping.headers();
            this.params = mapping.params();
            this.requestMethods = new RequestMethod[]{RequestMethod.DELETE};
        } else if (requestMapping instanceof GetMapping) {
            GetMapping mapping = (GetMapping) requestMapping;
            this.path = pathSpecification(map.get("value").toString(), mapping.value());
            this.consumes = mapping.consumes();
            this.produces = mapping.produces();
            this.headers = mapping.headers();
            this.params = mapping.params();
            this.requestMethods = new RequestMethod[]{RequestMethod.GET};
        } else if (requestMapping instanceof PatchMapping) {
            PatchMapping mapping = (PatchMapping) requestMapping;
            this.path = pathSpecification(map.get("value").toString(), mapping.value());
            this.consumes = mapping.consumes();
            this.produces = mapping.produces();
            this.headers = mapping.headers();
            this.params = mapping.params();
            this.requestMethods = new RequestMethod[]{RequestMethod.PATCH};
        } else if (requestMapping instanceof PostMapping) {
            PostMapping mapping = (PostMapping) requestMapping;
            this.path = pathSpecification(map.get("value").toString(), mapping.value());
            this.consumes = mapping.consumes();
            this.produces = mapping.produces();
            this.headers = mapping.headers();
            this.params = mapping.params();
            this.requestMethods = new RequestMethod[]{RequestMethod.POST};
        } else if (requestMapping instanceof PutMapping) {
            PutMapping mapping = (PutMapping) requestMapping;
            this.path = pathSpecification(map.get("value").toString(), mapping.value());
            this.consumes = mapping.consumes();
            this.produces = mapping.produces();
            this.headers = mapping.headers();
            this.params = mapping.params();
            this.requestMethods = new RequestMethod[]{RequestMethod.PUT};
        } else {
            throw new IllegalArgumentException(requestMapping.toString());
        }
    }

    /**
     * 获取数组http请求模型枚举
     * {@link RequestMapping#method()}
     *
     * @return
     */
    @Override
    public RequestMethod[] getRequestMethods() {
        return requestMethods;
    }

    /**
     * {@link RequestMapping#method()}
     */
    private RequestMethod[] requestMethods;

    /**
     * 获取请求函数
     *
     * @return
     */
    @Override
    public Method getMethod() {
        return super.getMethod();
    }

    /**
     * 控制器注释类型
     */
    private final Class<? extends Annotation> controllerClass;

    /**
     * 获取控制器注释类型
     *
     * @return
     */
    @Override
    public Class<? extends Annotation> getControllerClass() {
        return controllerClass;
    }

    /**
     * 控制器注释
     * 比如 {@link RestController}
     * 比如 {@see Controller}
     */
    private final Annotation controller;

    /**
     * 获取控制器注释
     *
     * @return
     */
    public Annotation getController() {
        return controller;
    }

    /**
     * 路径
     * {@link RestController#value()}控制器路径加下面其它函数注释路径，如果函数未指定路径时函数名称作为请求路径。
     * {@link RequestMapping#value()}
     * {@link DeleteMapping#value()}
     * {@link GetMapping#value()}
     * {@link PatchMapping#value()}
     * {@link PostMapping#value()}
     * {@link PutMapping#value()}
     */
    protected String path;

    /**
     * 获取请求路径
     *
     * @return
     */
    @Override
    public String getPath() {
        return path;
    }

    /**
     * 获取 {@link RestController} 注释目标对象
     *
     * @return
     */
    @Override
    public Object getTarget() {
        return super.getTarget();
    }

    /**
     * 获取函数的数组参数名称
     *
     * @return
     */
    @Override
    public NameParameter[] getParameters() {
        return super.getParameters();
    }

    /**
     * 重写哈希值
     *
     * @return
     */
    @Override
    public int hashCode() {
        return this.path.hashCode() + 31;
    }

    /**
     * 重写比对
     * 只按照路径参数比对
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof HttpRequestMethod) {
            HttpRequestMethod requestMethod = (HttpRequestMethod) obj;
            return Objects.equals(this.path, requestMethod.getPath());
        }
        return super.equals(obj);
    }

    /**
     * 重写转换字符
     *
     * @return
     */
    @Override
    public String toString() {
        return "HttpRequestMethod{" +
                "attributes=" + attributes.toString() +
                ", requestMapping=" + requestMapping.toString() +
                ", consumes=" + Arrays.toString(consumes) +
                ", produces=" + Arrays.toString(produces) +
                ", headers=" + Arrays.toString(headers) +
                ", params=" + Arrays.toString(params) +
                ", requestMethods=" + Arrays.toString(requestMethods) +
                ", controller=" + controller.toString() +
                ", path='" + path + '\'' +
                '}';
    }

    /**
     * 调用函数
     * {@link IHttpRequestMethodContainer#execute(HttpServletRequest, HttpServletResponse, FilterChain)}
     * 此函数在 {@link HttpRequestMethodProxy#invoke(Object, Method, Object[])} 中被代理调用
     *
     * @param parameters
     * @return
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Override
    public Object invoke(Object[] parameters) throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        return this.method.invoke(this.target, parameters);
    }
}