package ghost.framework.web.context.servlet.locale;
import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.constraints.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * 区域解析器接口
 */
public interface LocaleResolver {

	/**
	 * 获取请求对象区域
	 *
	 * @param request
	 * @return
	 */
	Locale resolveLocale(@NotNull HttpServletRequest request);

	/**
	 * 设置请求对象区域
	 */
	void setLocale(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @Nullable Locale locale);
}