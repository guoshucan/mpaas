package ghost.framework.web.module.http;

import ghost.framework.beans.annotation.conditional.ConditionalOnMissingClass;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.module.IModule;
import ghost.framework.context.proxy.ProxyUtil;
import ghost.framework.util.Assert;
import ghost.framework.util.ReflectUtil;
import ghost.framework.util.reflect.AnnotationMethod;
import ghost.framework.web.context.bind.annotation.RestController;
import ghost.framework.web.context.http.IHttpControllerContainer;
import ghost.framework.web.context.http.request.HttpRequestMethodPath;
import ghost.framework.web.context.http.request.IHttpRequestMethod;
import ghost.framework.web.context.http.request.IHttpRequestMethodContainer;
import ghost.framework.web.context.servlet.context.ServletContextInitializer;
import ghost.framework.web.module.exception.WebHttpRequestMethodPathException;
import ghost.framework.web.module.http.request.HttpRequestMethod;
import ghost.framework.web.module.http.request.HttpRequestMethodContainer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * package: ghost.framework.web.module.http
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description: 处理存储 {@link RestController} 等注释类型的容器
 * @Date: 13:26 2020/1/31
 */
@Component
@ConditionalOnMissingClass(HttpRequestMethodContainer.class)//注入依赖
public final class HttpControllerContainer extends AbstractCollection<Object> implements IHttpControllerContainer {
    private Log log = LogFactory.getLog(HttpControllerContainer.class);
    /**
     * 控制注释类型列表
     * 比如 {@link RestController} 注释
     */
    private List<Class<? extends Annotation>> controllerAnnotationList = new ArrayList<>(1);//Arrays.asList(new Class[]{RestController.class});

    /**
     * 控制器注释不存在时添加
     * @param annotation
     * @return
     */
    @Override
    public boolean addIfAbsent(Class<? extends Annotation> annotation) {
        synchronized (controllerAnnotationList) {
            if(controllerAnnotationList.contains(annotation)){
                return false;
            }
            controllerAnnotationList.add(annotation);
            return true;
        }
    }

    /**
     * 获取控制器注释类型列表
     * @return
     */
    @Override
    public List<Class<? extends Annotation>> getControllerAnnotationList() {
        return controllerAnnotationList;
    }

    /**
     * 添加控制器注释类型
     * 一般为 {@link RestController} 一样作为类型的控制器注释类型
     * {@link IHttpControllerContainer#addController(Class)}
     *
     * @param annotation 控制器注释类型
     */
    @Override
    public void addController(Class<? extends Annotation> annotation) {
        synchronized (controllerAnnotationList) {
            controllerAnnotationList.add(annotation);
        }
    }

    /**
     * 判断控制器类型是否存在
     * 一般为 {@link RestController} 一样作为类型的控制器注释类型
     * {@link IHttpControllerContainer#containsController(Class)}
     *
     * @param annotation 控制器注释类型
     * @return 返回是否有当前指定控制器注释类型
     */
    @Override
    public boolean containsController(Class<? extends Annotation> annotation) {
        synchronized (controllerAnnotationList) {
            return controllerAnnotationList.contains(annotation);
        }
    }

    /**
     * 删除控制器注释类型
     * 一般为 {@link RestController} 一样作为类型的控制器注释类型
     * {@link IHttpControllerContainer#removeController(Class)}
     *
     * @param annotation 控制器注释类型
     * @return 返回是否有删除当前指定控制器注释类型
     */
    @Override
    public boolean removeController(Class<? extends Annotation> annotation) {
        synchronized (controllerAnnotationList) {
            return controllerAnnotationList.remove(annotation);
        }
    }

    /**
     * 注入http请求函数容器接口
     */
    @Autowired
    private IHttpRequestMethodContainer httpRequestMethodContainer;
    /**
     * 注入模块接口
     */
    @Autowired
    private IModule module;

    /**
     * 合并http请求函数容器
     * 将本http控制器容器的全部请求函数合并到http请求函数容器中
     */
    @Override
    public void mergeHttpRequestMethodContainer() {
        //锁定存储控制器对象列表同步
        synchronized (this.sortedList) {
            //请函数地图
            Map<HttpRequestMethodPath, IHttpRequestMethod> requestMethodList = this.getRequestMethodMap();
            //获取请求函数容器地图
            Map<HttpRequestMethodPath, IHttpRequestMethod> sortedMap = this.httpRequestMethodContainer.getSortedMap();
            //锁定同步
            synchronized (sortedMap) {
                //添加新的请求函数地图
                for (Map.Entry<HttpRequestMethodPath, IHttpRequestMethod> entry : requestMethodList.entrySet()) {
                    if (!sortedMap.containsKey(entry.getKey())) {
                        sortedMap.put(entry.getKey(), entry.getValue());
                    }
                }
            }
        }
    }

    /**
     * 获取请求函数地图
     *
     * @return 返回请求函数地图
     */
    public Map<HttpRequestMethodPath, IHttpRequestMethod> getRequestMethodMap() {
        //请函数地图
        Map<HttpRequestMethodPath, IHttpRequestMethod> requestMethodList = new HashMap<>();
        //遍历填充请求函数列表
        for (Object o : this.sortedList) {
            //获取控制器对象类型
            Class<?> c = ProxyUtil.getProxyTarget(o).getClass();
            //获取控制器注释
//            RestController controller = c.getAnnotation(RestController.class);
            //遍历控制器注释类型
            Annotation controller = null;
            for (Class<? extends Annotation> a : this.controllerAnnotationList) {
                if (c.isAnnotationPresent(a)) {
                    controller = c.getAnnotation(a);
                    break;
                }
            }
            //判断控制器注释是否有效
            if(controller == null) {
                //没有找到有效的控制器注释
                this.log.error(o.getClass().getName() + " Not Controller Annotation Error");
                continue;
            }
            //获取指定注释的全部注释函数
            List<AnnotationMethod> list = ReflectUtil.getAllAnnotationsMethods(c, methodAnnotations);
            //遍历请求函数
            for (AnnotationMethod method : list) {
                //创建请求函数
                IHttpRequestMethod requestMethod = new HttpRequestMethod(o, controller, method.getMethod(), method.getAnnotation());
                //校验路径冲突
                if (requestMethodList.containsValue(requestMethod.getPath())) {
                    throw new WebHttpRequestMethodPathException(requestMethod.getPath());
                }
                //添加请求函数
                requestMethodList.put(new HttpRequestMethodPath(requestMethod.getPath(), requestMethod), requestMethod);
            }
        }
        return requestMethodList;
    }

    /**
     * 刷新http请求函数容器
     */
    @Override
    public void refreshHttpRequestMethodContainer() {
        //锁定存储控制器对象列表同步
        synchronized (this.sortedList) {
            //请函数地图
            Map<HttpRequestMethodPath, IHttpRequestMethod> requestMethodList = this.getRequestMethodMap();
            //获取请求函数容器地图
            Map<HttpRequestMethodPath, IHttpRequestMethod> sortedMap = this.httpRequestMethodContainer.getSortedMap();
            //锁定同步
            synchronized (sortedMap) {
                //清理http请求控制器容器
                sortedMap.clear();
                //添加新的请求函数地图
                requestMethodList.forEach(sortedMap::put);
            }
        }
    }

    /**
     * 添加控制器对象
     *
     * @param o
     * @return
     */
    @Override
    public boolean add(Object o) {
        Assert.notNull(o, "add is o null error");
        //获取代理目标对象判断注释
        Assert.notAnnotations(ProxyUtil.getProxyTarget(o), controllerAnnotationList, "add is o not Controller Annotation error");
        try {
            return sortedList.add(o);
        } finally {
            this.mergeHttpRequestMethodContainer();
        }
    }

    /**
     * 删除控制器对象
     *
     * @param o
     * @return
     */
    @Override
    public boolean remove(Object o) {
        Assert.notNull(o, "remove is o null error");
        Assert.notAnnotations(ProxyUtil.getProxyTarget(o), controllerAnnotationList, "remove is o not Controller Annotation error");
        return super.remove(o);
    }

    /**
     * 控制器对象列表
     */
    private List<Object> sortedList = new ArrayList<>();

    /**
     * 重写迭代器
     *
     * @return
     */
    @Override
    public Iterator<Object> iterator() {
        return this.sortedList.iterator();
    }

    /**
     * 获取 {@link ServletContextInitializer} 数量
     *
     * @return
     */
    @Override
    public int size() {
        return this.sortedList.size();
    }
}