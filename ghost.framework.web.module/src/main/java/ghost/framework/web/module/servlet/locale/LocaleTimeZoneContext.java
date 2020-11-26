package ghost.framework.web.module.servlet.locale;
import ghost.framework.beans.annotation.constraints.Nullable;

import java.util.TimeZone;
/**
 * 区域时区内容接口
 */
public interface LocaleTimeZoneContext extends LocaleContext {
	/**
	 * 获取区域时区
	 * @return
	 */
	@Nullable
	TimeZone getTimeZone();
}