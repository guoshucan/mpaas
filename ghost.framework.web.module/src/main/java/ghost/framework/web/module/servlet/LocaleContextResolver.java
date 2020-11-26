package ghost.framework.web.module.servlet;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.web.context.servlet.locale.LocaleResolver;
import ghost.framework.web.module.servlet.locale.LocaleContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 区域内容解析接口
 * {@link LocaleResolver}
 */
public interface LocaleContextResolver extends LocaleResolver {
	/**
	 * 解析区域内容
	 * @param request 请求对象
	 * @return 返回区域内容
	 */
	LocaleContext resolveLocaleContext(HttpServletRequest request);

	/**
	 * 设置区域内容
	 * @param request 请求对象
	 * @param response 响应对象
	 * @param localeContext 区域内容
	 */
	void setLocaleContext(HttpServletRequest request, @Nullable HttpServletResponse response,
                          @Nullable LocaleContext localeContext);
}