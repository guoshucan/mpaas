package ghost.framework.web.context.http;

import ghost.framework.web.context.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;

/**
 * package: ghost.framework.web.module.http
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:处理 {@link RestController} 等注释类型的接口
 * @Date: 13:45 2020/1/31
 */
public interface IHttpControllerContainer extends Collection<Object> {
    /**
     * 获取控制器注释类型列表
     * @return
     */
    List<Class<? extends Annotation>> getControllerAnnotationList();

    /**
     * 控制器注释不存在时添加
     * @param annotation
     * @return
     */
    boolean addIfAbsent(Class<? extends Annotation> annotation);
    /**
     * 判断控制器类型是否存在
     * 一般为 {@link RestController} 一样作为类型的控制器注释类型
     * @param annotation 控制器注释类型
     * @return 返回是否有当前指定控制器注释类型
     */
    boolean containsController(Class<? extends Annotation> annotation);

    /**
     * 添加控制器注释类型
     * 一般为 {@link RestController} 一样作为类型的控制器注释类型
     * @param annotation 控制器注释类型
     */
    void addController(Class<? extends Annotation> annotation);

    /**
     * 删除控制器注释类型
     * 一般为 {@link RestController} 一样作为类型的控制器注释类型
     * @param annotation 控制器注释类型
     * @return 返回是否有删除当前指定控制器注释类型
     */
    boolean removeController(Class<? extends Annotation> annotation);
    /**
     * 数组请求函数注释类型
     */
    Class<? extends Annotation>[] methodAnnotations = new Class[]
            {RequestMapping.class, DeleteMapping.class, GetMapping.class, PatchMapping.class, PostMapping.class, PutMapping.class};
    /**
     * 刷新http请求函数容器
     */
    void refreshHttpRequestMethodContainer();

    /**
     * 合并http请求函数容器
     * 将本http控制器容器的全部请求函数合并到http请求函数容器中
     */
    void mergeHttpRequestMethodContainer();
}