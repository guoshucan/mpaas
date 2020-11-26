package ghost.framework.web.module.servlet.locale;
import ghost.framework.beans.annotation.constraints.Nullable;
import java.util.Locale;
/**
 * 区域内容接口
 * @see LocaleTimeZoneContext
 */
public interface LocaleContext {
	/**
	 * 获取区域
	 *
	 * @return
	 */
	@Nullable
	Locale getLocale();
}