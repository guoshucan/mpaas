package ghost.framework.web.module.servlet.locale;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.web.context.servlet.locale.LocaleResolver;
import ghost.framework.web.module.servlet.LocaleContextResolver;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.TimeZone;
/**
 * 区域时区解析基础类
 * {@link LocaleResolver}
 * {@link LocaleResolver#setLocale(HttpServletRequest, HttpServletResponse, Locale)}
 * {@link LocaleResolver#resolveLocale(HttpServletRequest)}
 * {@link AbstractLocaleResolver}
 * {@link AbstractLocaleResolver#getDefaultLocale()}
 * {@link AbstractLocaleResolver#setDefaultLocale(Locale)}
 * {@link LocaleContextResolver}
 * {@link LocaleContextResolver#resolveLocaleContext(HttpServletRequest)}
 * {@link LocaleContextResolver#setLocaleContext(HttpServletRequest, HttpServletResponse, LocaleContext)}
 */
public abstract class AbstractLocaleTimeZoneResolver
		extends AbstractLocaleResolver
		implements LocaleContextResolver {
	/**
	 * 默认时区
	 */
	@Nullable
	private TimeZone defaultTimeZone;

	/**
	 * 设置默认时区
	 */
	public void setDefaultTimeZone(@Nullable TimeZone defaultTimeZone) {
		this.defaultTimeZone = defaultTimeZone;
	}

	/**
	 * 获取默认时区
	 *
	 * @return 返回默认时区
	 */
	@Nullable
	public TimeZone getDefaultTimeZone() {
		return this.defaultTimeZone;
	}

	/**
	 * 解析默认区域
	 *
	 * @param request 请求对象
	 * @return 返回默认区域
	 */
	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		Locale locale = resolveLocaleContext(request).getLocale();
		return (locale != null ? locale : request.getLocale());
	}

	/**
	 * 设置默认区域
	 *
	 * @param request  请求对象
	 * @param response 响应对象
	 * @param locale   区域对象
	 */
	@Override
	public void setLocale(HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Locale locale) {
		setLocaleContext(request, response, (locale != null ? new SimpleLocaleContext(locale) : null));
	}
}