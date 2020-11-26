package ghost.framework.web.module.servlet.locale;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.web.context.servlet.locale.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
/**
 * 基础区域解析器类
 * {@link LocaleResolver}
 * {@link LocaleResolver#setLocale(HttpServletRequest, HttpServletResponse, Locale)}
 * {@link LocaleResolver#resolveLocale(HttpServletRequest)}
 */
public abstract class AbstractLocaleResolver implements LocaleResolver {
	/**
	 * 默认区域解析器
	 */
	@Nullable
	private Locale defaultLocale = Locale.SIMPLIFIED_CHINESE;
	/**
	 * 设置默认区域解析器
	 */
	public void setDefaultLocale(@Nullable Locale defaultLocale) {
		this.defaultLocale = defaultLocale;
	}
	/**
	 * 获取默认区域解析器
	 */
	@Nullable
	protected Locale getDefaultLocale() {
		return this.defaultLocale;
	}
}