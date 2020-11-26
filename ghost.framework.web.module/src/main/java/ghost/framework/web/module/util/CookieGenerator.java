package ghost.framework.web.module.util;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.util.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Cookie构造器
 */
public class CookieGenerator {
	/**
	 * 默认cookies路径
	 */
	public static final String DEFAULT_COOKIE_PATH = "/";

	protected final Log logger = LogFactory.getLog(getClass());
	/**
	 * Cookie名称
	 */
	@Nullable
	private String cookieName;
	/**
	 * Cookie域
	 */
	@Nullable
	private String cookieDomain;
	/**
	 * Cookie路径
	 */
	private String cookiePath = DEFAULT_COOKIE_PATH;
	/**
	 * JAVA中Cookie MaxAge属性及其使用
	 * （maxAge 可以为正数，表示此cookie从创建到过期所能存在的时间，以秒为单位，此cookie会存储到客户端电脑，以cookie文件形式保存，不论关闭浏览器或关闭电脑，直到时间到才会过期。
	 * 可以为负数，表示此cookie只是存储在浏览器内存里，只要关闭浏览器，此cookie就会消失。maxAge默认值为-1。
	 * 还可以为0，表示从客户端电脑或浏览器内存中删除此cookie。）
	 * 如果maxAge属性为正数，则表示该Cookie会在maxAge秒之后自动失效。浏览器会将maxAge为正数的Cookie持久化，即写到对应的Cookie文件中。无论客户关闭了浏览器还是电脑，只要还在maxAge秒之前，登录网站时该Cookie仍然有效。
	 * 如果maxAge为负数，则表示该Cookie仅在本浏览器窗口以及本窗口打开的子窗口内有效，关闭窗口后该Cookie即失效。maxAge为负数的Cookie，为临时性Cookie，不会被持久化，不会被写到Cookie文件中。Cookie信息保存在浏览器内存中，因此关闭浏览器该Cookie就消失了。Cookie默认的maxAge值为-1。
	 * 如果maxAge为0，则表示删除该Cookie。Cookie机制没有提供删除Cookie的方法，因此通过设置该Cookie即时失效实现删除Cookie的效果。失效的Cookie会被浏览器从Cookie文件或者内存中删除。
	 * response对象提供的Cookie操作方法只有一个添加操作add(Cookie cookie)。要想修改Cookie只能使用一个同名的Cookie来覆盖原来的Cookie，达到修改的目的。删除时只需要把maxAge修改为0即可。
	 * 在所遇到的项目中，Action里创建了一个cookie，maxAge为-1，紧接着在另一个方法中要删除cookie，就可以通过创建一个同名同域的cookie，然后将maxAge设置为0，再通过response的addCookie方法对客户端的cookie文件或浏览器内存中的cookie进行删除。
	 * 注意一、修改、删除Cookie时，新建的Cookie除value、maxAge之外的所有属性，例如name、path、domain等，都要与原Cookie完全一样。否则，浏览器将视为两个不同的Cookie不予覆盖，导致修改、删除失败。
	 * 注意二、从客户端读取Cookie时，包括maxAge在内的其他属性都是不可读的，也不会被提交。浏览器提交Cookie时只会提交name与value属性。maxAge属性只被浏览器用来判断Cookie是否过期。
	 */
	@Nullable
	private Integer cookieMaxAge;
	/**
	 * 是否为https
	 */
	private boolean cookieSecure = false;
	/**
	 * 客户都浏览器是否可读cookie
	 */
	private boolean cookieHttpOnly = false;


	/**
	 * 设置cookie名称
	 * @param cookieName cookie名称
	 * @see javax.servlet.http.Cookie#getName()
	 */
	public void setCookieName(@Nullable String cookieName) {
		this.cookieName = cookieName;
	}

	/**
	 * 获取cookie名称
	 * @return 返回cookie名称
	 */
	@Nullable
	public String getCookieName() {
		return this.cookieName;
	}

	/**
	 * 设置cookie域
	 * @param cookieDomain cookie域
	 * @see javax.servlet.http.Cookie#setDomain
	 */
	public void setCookieDomain(@Nullable String cookieDomain) {
		this.cookieDomain = cookieDomain;
	}

	/**
	 * 获取cookie域
	 * @return 返回cookie域
	 */
	@Nullable
	public String getCookieDomain() {
		return this.cookieDomain;
	}

	/**
	 * 设置cookie路径
	 * @param cookiePath  cookie路径
	 * @see javax.servlet.http.Cookie#setPath
	 */
	public void setCookiePath(String cookiePath) {
		this.cookiePath = cookiePath;
	}

	/**
	 * 获取cookie路径
	 * @return 返回cookie路径
	 */
	public String getCookiePath() {
		return this.cookiePath;
	}

	/**
	 * Use the given maximum age (in seconds) for cookies created by this generator.
	 * Useful special value: -1 ... not persistent, deleted when client shuts down.
	 * <p>Default is no specific maximum age at all, using the Servlet container's
	 * default.
	 * @see javax.servlet.http.Cookie#setMaxAge
	 */
	public void setCookieMaxAge(@Nullable Integer cookieMaxAge) {
		this.cookieMaxAge = cookieMaxAge;
	}

	/**
	 * Return the maximum age for cookies created by this generator.
	 */
	@Nullable
	public Integer getCookieMaxAge() {
		return this.cookieMaxAge;
	}

	/**
	 * 设置cookie是否为https
	 * @param cookieSecure 是否为https cookie
	 * @see javax.servlet.http.Cookie#setSecure
	 */
	public void setCookieSecure(boolean cookieSecure) {
		this.cookieSecure = cookieSecure;
	}

	/**
	 * 获取是否为https cookie
	 * @return
	 */
	public boolean isCookieSecure() {
		return this.cookieSecure;
	}

	/**
	 * 设置cookie客户浏览器是否可读
	 * @param cookieHttpOnly
	 * @see javax.servlet.http.Cookie#setHttpOnly
	 */
	public void setCookieHttpOnly(boolean cookieHttpOnly) {
		this.cookieHttpOnly = cookieHttpOnly;
	}

	/**
	 * 获取cookie客户浏览器是否可读
	 * @return
	 */
	public boolean isCookieHttpOnly() {
		return this.cookieHttpOnly;
	}

	/**
	 * 添加cookie
	 * @see #setCookieName
	 * @see #setCookieDomain
	 * @see #setCookiePath
	 * @see #setCookieMaxAge
	 * @param response 响应对象
	 * @param cookieValue cookie值
	 */
	public void addCookie(HttpServletResponse response, String cookieValue) {
		Assert.notNull(response, "response must not be null");
		Cookie cookie = createCookie(cookieValue);
		Integer maxAge = getCookieMaxAge();
		if (maxAge != null) {
			cookie.setMaxAge(maxAge);
		}
		if (isCookieSecure()) {
			cookie.setSecure(true);
		}
		if (isCookieHttpOnly()) {
			cookie.setHttpOnly(true);
		}
		response.addCookie(cookie);
		if (logger.isTraceEnabled()) {
			logger.trace("Added cookie [" + getCookieName() + "=" + cookieValue + "]");
		}
	}

	/**
	 * 删除cookie
	 * @see #setCookieName
	 * @see #setCookieDomain
	 * @see #setCookiePath
	 * @param response 响应对象
	 */
	public void removeCookie(HttpServletResponse response) {
		Assert.notNull(response, "response must not be null");
		Cookie cookie = createCookie("");
		cookie.setMaxAge(0);
		if (isCookieSecure()) {
			cookie.setSecure(true);
		}
		if (isCookieHttpOnly()) {
			cookie.setHttpOnly(true);
		}
		response.addCookie(cookie);
		if (logger.isTraceEnabled()) {
			logger.trace("Removed cookie '" + getCookieName() + "'");
		}
	}

	/**
	 * 创建cookie
	 * @param cookieValue cookie值
	 * @return 返回cookie
	 * @see #setCookieName
	 * @see #setCookieDomain
	 * @see #setCookiePath
	 */
	protected Cookie createCookie(String cookieValue) {
		Cookie cookie = new Cookie(getCookieName(), cookieValue);
		if (getCookieDomain() != null) {
			cookie.setDomain(getCookieDomain());
		}
		cookie.setPath(getCookiePath());
		return cookie;
	}
}