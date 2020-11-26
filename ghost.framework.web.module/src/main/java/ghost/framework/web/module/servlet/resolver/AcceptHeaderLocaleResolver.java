package ghost.framework.web.module.servlet.resolver;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.beans.annotation.stereotype.Single;
import ghost.framework.util.StringUtils;
import ghost.framework.web.context.servlet.locale.LocaleResolver;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
/**
 * 实现获取头Accept-Language参数构建区域
 * {@link LocaleResolver}
 * @see javax.servlet.http.HttpServletRequest#getLocale()
 */
@Single
@Component
public class AcceptHeaderLocaleResolver implements LocaleResolver {
	/**
	 * 实现区域列表
	 */
	private final List<Locale> supportedLocales = new ArrayList<>(4);
	/**
	 * 默认区域
	 */
	@Nullable
	private Locale defaultLocale = Locale.SIMPLIFIED_CHINESE;

	/**
	 * 设置实现区域列表
	 *
	 * @param locales
	 */
	public void setSupportedLocales(List<Locale> locales) {
		this.supportedLocales.clear();
		this.supportedLocales.addAll(locales);
	}

	/**
	 * 获取实现区域列表
	 *
	 * @return
	 */
	public List<Locale> getSupportedLocales() {
		return this.supportedLocales;
	}

	/**
	 * 设置默认区域
	 * {@link HttpServletRequest#getLocale()}.
	 *
	 * @param defaultLocale the default locale to use
	 */
	public void setDefaultLocale(@Nullable Locale defaultLocale) {
		this.defaultLocale = defaultLocale;
	}

	/**
	 * The configured default locale, if any.
	 *
	 * @since 4.3
	 */
	@Nullable
	public Locale getDefaultLocale() {
		return this.defaultLocale;
	}

	/**
	 * 解析区域
	 *
	 * @param request
	 * @return
	 */
	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		Locale defaultLocale = getDefaultLocale();
		if (defaultLocale != null && request.getHeader("Accept-Language") == null) {
			return defaultLocale;
		}
		Locale requestLocale = request.getLocale();
		List<Locale> supportedLocales = getSupportedLocales();
		if (supportedLocales.isEmpty() || supportedLocales.contains(requestLocale)) {
			return requestLocale;
		}
		Locale supportedLocale = findSupportedLocale(request, supportedLocales);
		if (supportedLocale != null) {
			return supportedLocale;
		}
		return (defaultLocale != null ? defaultLocale : requestLocale);
	}

	/**
	 * 搜索实现区域
	 *
	 * @param request
	 * @param supportedLocales
	 * @return
	 */
	@Nullable
	private Locale findSupportedLocale(HttpServletRequest request, List<Locale> supportedLocales) {
		Enumeration<Locale> requestLocales = request.getLocales();
		Locale languageMatch = null;
		while (requestLocales.hasMoreElements()) {
			Locale locale = requestLocales.nextElement();
			if (supportedLocales.contains(locale)) {
				if (languageMatch == null || languageMatch.getLanguage().equals(locale.getLanguage())) {
					// Full match: language + country, possibly narrowed from earlier language-only match
					return locale;
				}
			} else if (languageMatch == null) {
				// Let's try to find a language-only match as a fallback
				for (Locale candidate : supportedLocales) {
					if (!StringUtils.hasLength(candidate.getCountry()) &&
							candidate.getLanguage().equals(locale.getLanguage())) {
						languageMatch = candidate;
						break;
					}
				}
			}
		}
		return languageMatch;
	}

	/**
	 * 设置区域
	 *
	 * @param request
	 * @param response
	 * @param locale
	 */
	@Override
	public void setLocale(HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Locale locale) {
		throw new UnsupportedOperationException(
				"Cannot change HTTP accept header - use a different locale resolution strategy");
	}
}