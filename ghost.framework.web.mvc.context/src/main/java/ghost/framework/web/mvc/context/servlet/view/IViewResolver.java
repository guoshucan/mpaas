package ghost.framework.web.mvc.context.servlet.view;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.context.resolver.ResolverException;
import ghost.framework.web.context.bind.annotation.*;
import ghost.framework.web.context.http.request.IHttpRequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 视图解析接口
 */
public interface IViewResolver {
	/**
	 * 判断模板是否可以解析
	 * 一般默认使用 {@link IViewResolver#getTemplateSuffix()} 判断模板后缀是否属于当前模板解析器解析
	 * @param viewName 模板视图名称
	 * @return 是否可以解析的模板
	 */
	default boolean isResolve(@NotNull String viewName){
		return viewName.endsWith(getTemplateSuffix());
	}
	/**
	 * 获取模板后缀
	 * @return 返回模板后缀，格式比如.xxx
	 */
	String getTemplateSuffix();
	/**
	 * 解析视图
	 *
	 * @param request  请求对象
	 * @param response 响应对象
	 * @param requestMethod 请求函数
	 * @param viewName 视图名称
	 *                 {@link RequestMapping}
	 *                 {@link DeleteMapping}
	 *                 {@link GetMapping}
	 *                 {@link PatchMapping}
	 *                 {@link PostMapping}
	 *                 {@link PutMapping}
	 *                 等注释函数返回的{@link String}类型的模板文件名称，以html扩展名为主的模板路径
	 * @throws ResolverException
	 */
	void resolveViewName(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull IHttpRequestMethod requestMethod, @NotNull String viewName) throws ResolverException;
}