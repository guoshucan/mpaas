package ghost.framework.web.module.servlet.resolver;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.beans.annotation.stereotype.Single;
import ghost.framework.web.context.servlet.locale.LocaleResolver;
import ghost.framework.web.context.utils.WebUtils;
import ghost.framework.web.module.servlet.LocaleContextResolver;
import ghost.framework.web.module.servlet.locale.AbstractLocaleResolver;
import ghost.framework.web.module.servlet.locale.AbstractLocaleTimeZoneResolver;
import ghost.framework.web.module.servlet.locale.LocaleContext;
import ghost.framework.web.module.servlet.locale.LocaleTimeZoneContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.util.TimeZone;
/**
 * 会话区域解析器
 * {@link LocaleResolver}
 * {@link LocaleResolver#setLocale(HttpServletRequest, HttpServletResponse, Locale)}
 * {@link LocaleResolver#resolveLocale(HttpServletRequest)}
 * {@link AbstractLocaleResolver}
 * {@link AbstractLocaleResolver#getDefaultLocale()}
 * {@link AbstractLocaleResolver#setDefaultLocale(Locale)}
 * {@link LocaleContextResolver}
 * {@link LocaleContextResolver#resolveLocaleContext(HttpServletRequest)}
 * {@link LocaleContextResolver#setLocaleContext(HttpServletRequest, HttpServletResponse, LocaleContext)}
 * {@link AbstractLocaleTimeZoneResolver}
 * {@link AbstractLocaleTimeZoneResolver#getDefaultLocale()}
 * {@link AbstractLocaleTimeZoneResolver#getDefaultTimeZone()}
 * {@link AbstractLocaleTimeZoneResolver#resolveLocale(HttpServletRequest)}
 * {@link AbstractLocaleTimeZoneResolver#setLocale(HttpServletRequest, HttpServletResponse, Locale)}
 */
@Single
@Component
public class SessionLocaleResolver extends AbstractLocaleTimeZoneResolver {
	/**
	 * 会话区域键
	 */
	public static final String LOCALE_SESSION_ATTRIBUTE_NAME = SessionLocaleResolver.class.getName() + ".LOCALE";
	/**
	 * 会话时区键
	 * 会话区域为时区区域
	 */
	public static final String TIME_ZONE_SESSION_ATTRIBUTE_NAME = SessionLocaleResolver.class.getName() + ".TIME_ZONE";
	/**
	 * 会话区域键
	 */
	private String localeAttributeName = LOCALE_SESSION_ATTRIBUTE_NAME;
	/**
	 * 会话时区键
	 * 会话区域为时区区域
	 */
	private String timeZoneAttributeName = TIME_ZONE_SESSION_ATTRIBUTE_NAME;

	/**
	 * 设置{@link HttpSession}区域扩展名称
	 */
	public void setLocaleAttributeName(String localeAttributeName) {
		this.localeAttributeName = localeAttributeName;
	}

	/**
	 * 设置{@link HttpSession}时区扩展名称
	 */
	public void setTimeZoneAttributeName(String timeZoneAttributeName) {
		this.timeZoneAttributeName = timeZoneAttributeName;
	}

	/**
	 * 获取解析区域
	 *
	 * @param request 请求对象
	 * @return 返回解析区域对象
	 */
	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		Locale locale = (Locale) WebUtils.getSessionAttribute(request, this.localeAttributeName);
		if (locale == null) {
			locale = determineDefaultLocale(request);
		}
		return locale;
	}

	/**
	 * 获取解析区域内容
	 *
	 * @param request 请求对象
	 * @return 返回区域内容
	 */
	@Override
	public LocaleContext resolveLocaleContext(final HttpServletRequest request) {
		return new LocaleTimeZoneContext() {
			@Override
			public Locale getLocale() {
				Locale locale = (Locale) WebUtils.getSessionAttribute(request, localeAttributeName);
				if (locale == null) {
					locale = determineDefaultLocale(request);
				}
				return locale;
			}

			@Override
			@Nullable
			public TimeZone getTimeZone() {
				TimeZone timeZone = (TimeZone) WebUtils.getSessionAttribute(request, timeZoneAttributeName);
				if (timeZone == null) {
					timeZone = determineDefaultTimeZone(request);
				}
				return timeZone;
			}
		};
	}

	/**
	 * 设置区域内容
	 *
	 * @param request       请求对象
	 * @param response      响应对象
	 * @param localeContext 区域内容
	 */
	@Override
	public void setLocaleContext(HttpServletRequest request, @Nullable HttpServletResponse response,
								 @Nullable LocaleContext localeContext) {
		Locale locale = null;
		TimeZone timeZone = null;
		if (localeContext != null) {
			locale = localeContext.getLocale();
			if (localeContext instanceof LocaleTimeZoneContext) {
				timeZone = ((LocaleTimeZoneContext) localeContext).getTimeZone();
			}
		}
		WebUtils.setSessionAttribute(request, this.localeAttributeName, locale);
		WebUtils.setSessionAttribute(request, this.timeZoneAttributeName, timeZone);
	}

	/**
	 * 确定默认区域
	 *
	 * @param request 请求对象
	 * @return 返回确定区域，默认区域优先
	 * @see #setDefaultLocale
	 * @see javax.servlet.http.HttpServletRequest#getLocale()
	 */
	protected Locale determineDefaultLocale(HttpServletRequest request) {
		Locale defaultLocale = getDefaultLocale();
		if (defaultLocale == null) {
			defaultLocale = request.getLocale();
		}
		return defaultLocale;
	}

	/**
	 * 获取确认默认时区
	 *
	 * @param request 请求对象
	 * @return 返回时区
	 * @see #setDefaultTimeZone
	 */
	@Nullable
	protected TimeZone determineDefaultTimeZone(HttpServletRequest request) {
		return getDefaultTimeZone();
	}
}