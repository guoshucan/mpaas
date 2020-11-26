package ghost.framework.web.context.http.request.method.returnValue;
import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.context.resolver.ResolverException;
import ghost.framework.context.resolver.ReturnValueResolverException;
import ghost.framework.util.CollectionUtils;
import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.bind.annotation.RestController;
import ghost.framework.web.context.http.request.IHttpRequestMethod;
import ghost.framework.web.context.http.request.IHttpRequestMethodContainer;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;

/**
 * package: ghost.framework.web.context.http.request.method
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:处理请求函数返回值解析接口
 * @Date: 2020/2/29:0:07
 */
public interface IRequestMethodReturnValueResolver {
	/**
	 * 获取指定返回控制器注释类型
	 * 比如只能使用{@link RestController}注释类型控制器返回就声明该控制器注释类型
	 * 指定请求函数返回值{@link IHttpRequestMethodContainer#execute(HttpServletRequest, HttpServletResponse, FilterChain)}执行函数所属控制器注释类型
	 *
	 * @return
	 */
	default Class<? extends Annotation>[] getAnnotationControllers() {
		return null;
	}

	/**
	 * 判断是否有注释控制器类型
	 * @param controllerClass
	 * @return
	 */
	default boolean isAnnotationController(Class<? extends Annotation> controllerClass) {
		//判断是否没有注释控制器类型
		if (CollectionUtils.isEmpty(this.getAnnotationControllers())) {
			return false;
		}
		//遍历比对注释控制器类型
		for (Class<? extends Annotation> a : this.getAnnotationControllers()) {
			if (a.equals(controllerClass)) {
				//找到注释控制器类型
				return true;
			}
		}
		return false;
	}
	/**
	 * 获取响应处理类型
	 * 对应 {@link RequestMapping#produces()} 指定类型
	 *
	 * @return
	 */
	default String[] getProduces() {
		return null;
	}

	/**
	 * 获取是否为默认解析器
	 *
	 * @return
	 */
	default boolean isDefault() {
		return false;
	}

	/**
	 * 设置是否为默认解析器
	 *
	 * @param def
	 */
	default void setDefault(boolean def) {
	}

	/**
	 * 判断返回值是否需要解析
	 *
	 * @param requestMethod 请求函数
	 * @param returnValue   返回值对象
	 * @return 返回是否需要解析
	 */
	default boolean isResolver(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
							   @NotNull IHttpRequestMethod requestMethod, @Nullable Object returnValue) {
		return false;
	}

	/**
	 * @param requestMethod
	 * @throws ResolverException
	 */
	@Nullable
	void handleReturnValue(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
						   @NotNull IHttpRequestMethod requestMethod, @Nullable Object returnValue) throws ReturnValueResolverException;
}