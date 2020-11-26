package ghost.framework.web.mvc.plugin.servlet.view;

import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.beans.utils.OrderAnnotationUtil;
import ghost.framework.context.resolver.ResolverException;
import ghost.framework.web.context.bind.annotation.*;
import ghost.framework.web.context.http.request.IHttpRequestMethod;
import ghost.framework.web.mvc.context.servlet.view.IViewResolver;
import ghost.framework.web.mvc.context.servlet.view.IViewResolverContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * package: ghost.framework.web.module.servlet
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:视图解析容器
 * @Date: 2020/2/13:21:09
 */
@Component
public class ViewResolverContainer<V extends IViewResolver> implements IViewResolverContainer<V> {
    /**
     * 数组视图解析器
     * 使用 {@link List} 接口操作后按照 {@link ghost.framework.beans.annotation.order.Order} 注释排序位置，
     * 如果未注释排序侧使用默认排序位置
     */
    private IViewResolver[] viewResolvers = new IViewResolver[0];

    /**
     * 添加视图解析器
     *
     * @param iViewResolver
     */
    @Override
    public boolean add(V iViewResolver) {
        boolean is;
        List<IViewResolver> list = new ArrayList<>(Arrays.asList(viewResolvers));
        is = list.add(iViewResolver);
        if (is) {
            OrderAnnotationUtil.listSort(list);
            synchronized (viewResolvers) {
                viewResolvers = list.toArray(new IViewResolver[list.size()]);
            }
        }
        return is;
    }

    /**
     * 删除视图解析器
     *
     * @param iViewResolver
     * @return
     */
    @Override
    public boolean remove(V iViewResolver) {
        boolean is;
        List<IViewResolver> list = new ArrayList<>(Arrays.asList(viewResolvers));
        is = list.remove(iViewResolver);
        if (is) {
            OrderAnnotationUtil.listSort(list);
            synchronized (viewResolvers) {
                viewResolvers = list.toArray(new IViewResolver[list.size()]);
            }
        }
        return is;
    }

    /**
     * 获取视图解析器数量
     *
     * @return
     */
    @Override
    public int size() {
        return viewResolvers.length;
    }

    /**
     * 验证视图解析器是否存在
     *
     * @param iViewResolver
     * @return
     */
    @Override
    public boolean contains(IViewResolver iViewResolver) {
        return new ArrayList<>(Arrays.asList(viewResolvers)).contains(iViewResolver);
    }

    /**
     * 解析视图
     *
     * @param request       请求对象
     * @param response      响应对象
     * @param requestMethod 请求函数
     * @param viewName      视图名称
     *                      {@link RequestMapping}
     *                      {@link DeleteMapping}
     *                      {@link GetMapping}
     *                      {@link PatchMapping}
     *                      {@link PostMapping}
     *                      {@link PutMapping}
     *                      等注释函数返回的{@link String}类型的模板文件名称，以html扩展名为主的模板路径
     * @throws ResolverException
     */
    @Override
    public void resolveViewName(HttpServletRequest request, HttpServletResponse response, IHttpRequestMethod requestMethod, String viewName) throws ResolverException {
        //遍历视图解析器
        for (IViewResolver resolver : viewResolvers) {
            //解析视图内容
            resolver.resolveViewName(request, response, requestMethod, viewName);
            //判断是否无需再解析
            if (response.getStatus() == HttpServletResponse.SC_OK) {
                //设置返回内容类型与编码
                response.setContentType("text/html;charset=" + response.getCharacterEncoding());
                response.setHeader("Pragma", "no-cache");
                response.setHeader("Cache-Control", "no-cache");
                //已经处理了无需再解析
                return;
            }
        }
    }
}