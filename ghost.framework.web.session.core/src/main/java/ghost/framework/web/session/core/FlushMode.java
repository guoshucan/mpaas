package ghost.framework.web.session.core;
/**
 * 刷新模块
 */
public enum FlushMode {
	/**
	 * 不及时保存
	 * 等到 {@link SessionRepository#save(Session)} 调用时才保存会话数据
	 */
	NotTimely,
	/**
	 * 及时保存
	 * {@link SessionRepository#createSession()}
	 * {@link Session#removeAttribute(String)}
	 * {@link Session#setAttribute(String, Object)}
	 */
	Immediate
}