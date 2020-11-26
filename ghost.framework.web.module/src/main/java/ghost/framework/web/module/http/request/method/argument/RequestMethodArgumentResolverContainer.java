package ghost.framework.web.module.http.request.method.argument;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.parameter.NameParameter;
import ghost.framework.context.resolver.ResolverException;
import ghost.framework.context.valid.IAnnotationValidFactoryContainer;
import ghost.framework.util.Assert;
import ghost.framework.web.context.http.request.IHttpRequestMethod;
import ghost.framework.web.context.http.request.method.argument.IRequestMethodArgumentAnnotationResolver;
import ghost.framework.web.context.http.request.method.argument.IRequestMethodArgumentResolver;
import ghost.framework.web.context.http.request.method.argument.IRequestMethodArgumentResolverContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * package: ghost.framework.web.module.http.request.method.argument
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:处理程序方法参数解析器容器
 * 存放继承 {@link IRequestMethodArgumentResolver} 接口的处理函数参数解析器接口对象
 * @Date: 2020/2/29:0:06
 */
@Component
public final class RequestMethodArgumentResolverContainer<V extends IRequestMethodArgumentResolver>
        implements IRequestMethodArgumentResolverContainer<V> {

    /**
     * 参数类型解析器
     */
    private List<IRequestMethodArgumentResolver> list = new ArrayList<>();
    /**
     * 参数注释解析器列表
     */
    private List<IRequestMethodArgumentAnnotationResolver> annotationList = new ArrayList<>();
    /**
     * 添加参数解析器
     *
     * @param argumentResolver
     * @return
     */
    @Override
    public boolean add(V argumentResolver) {
        Assert.notNull(argumentResolver, "add is argumentResolver null error");
        //判断是否为注释解析器
        if(argumentResolver instanceof IRequestMethodArgumentAnnotationResolver){
            //添加注释解析器
            synchronized (annotationList) {
                return annotationList.add((IRequestMethodArgumentAnnotationResolver)argumentResolver);
            }
        }
        //添加类型解析器
        synchronized (list) {
            return list.add(argumentResolver);
        }
    }

    /**
     * 删除参数解析器
     *
     * @param argumentResolver
     * @return
     */
    @Override
    public boolean remove(V argumentResolver) {
        Assert.notNull(argumentResolver, "remove is argumentResolver null error");
        //判断是否为注释解析器
        if(argumentResolver instanceof IRequestMethodArgumentAnnotationResolver){
            //删除注释解析器
            synchronized (annotationList) {
                return annotationList.remove(argumentResolver);
            }
        }
        //删除类型解析器
        synchronized (list) {
            return list.remove(argumentResolver);
        }
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
     * 注入验证工厂容器
     */
    @Autowired
    private IAnnotationValidFactoryContainer validFactoryContainer;
    /**
     * 函数解析器
     *
     * @param requestMethod 请求函数
     * @return 返回调用函数的数组参数
     * @throws ResolverException
     */
    @Override
    public Object[] resolveMethod(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                  @NotNull IHttpRequestMethod requestMethod) throws ResolverException {
        //判断是否有参数
        if (requestMethod.getMethod().getParameterCount() == 0) {
            //没有函数参数
            return null;
        }
        //声明函数数组参数
        Object[] parameters = new Object[requestMethod.getMethod().getParameterCount()];
        int i = 0;
        //参数是否已经处理
        boolean is = false;
        for (NameParameter parameter : requestMethod.getParameters()) {
            //重置未解析状态
            is = false;
            //遍历注释解析器
            for (IRequestMethodArgumentAnnotationResolver annotationResolver : this.annotationList) {
                //判断是否有对应解析器
                if (annotationResolver.isResolver(request, response, requestMethod, parameter)) {
                    //解析参数
                    parameters[i] = annotationResolver.resolveArgument(request, response, requestMethod, parameter);
                    i++;
                    //退出解析器循环，执行下一个函数参数
                    is = true;
                    break;
                }
            }
            if (is) {
                continue;
            }
            //遍历类型解析器
            for (IRequestMethodArgumentResolver argumentResolver : this.list) {
                //判断是否有对应解析器
                if (argumentResolver.isResolver(request, response, requestMethod, parameter)) {
                    //解析参数
                    parameters[i] = argumentResolver.resolveArgument(request, response, requestMethod, parameter);
                    i++;
                    //退出解析器循环，执行下一个函数参数
                    break;
                }
            }
        }
        return parameters;
    }
}