package ghost.framework.web.session.core;

/**
 * 会话保存模式枚举
 */
public enum SaveMode {
	/**
	 * 不立即保存会话键刷新
	 */
	ON_SET_ATTRIBUTE,
	/**
	 * Same as {@link #ON_SET_ATTRIBUTE} with addition of saving attributes that have been
	 * read using {@link Session#getAttribute(String)}.
	 */
	ON_GET_ATTRIBUTE,
	/**
	 * 全部动作保存
	 */
	ALWAYS
}
