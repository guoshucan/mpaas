package ghost.framework.web.context.http.request.method.argument;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.module.Module;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.application.IGetApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.exception.InjectionClassAnnotationNotModuleException;
import ghost.framework.context.module.IModuleContainer;
import ghost.framework.context.module.thread.ModuleThreadLocal;
import ghost.framework.context.parameter.NameParameter;
import ghost.framework.context.resolver.ResolverException;
import ghost.framework.web.context.bind.annotation.RequestMethod;
import ghost.framework.web.context.bind.annotation.RestController;
import ghost.framework.web.context.http.MediaType;
import ghost.framework.web.context.http.request.IHttpRequestMethod;
import ghost.framework.web.context.http.request.IHttpRequestMethodContainer;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;

/**
 * package: ghost.framework.web.context.http.request.method
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:处理程序方法参数解析器接口
 * {@link IGetApplication}
 * @Date: 2020/2/29:8:57
 */
public interface IRequestMethodArgumentResolver extends IGetApplication {
	/**
	 * 获取指定函数参数控制器注释类型
	 * 比如只能使用{@link RestController}注释类型控制器函数参数就声明该控制器注释类型
	 * 指定请求函数参数{@link IHttpRequestMethodContainer#execute(HttpServletRequest, HttpServletResponse, FilterChain)}执行函数所属控制器注释类型
	 * @return
	 */
	default Class<? extends Annotation>[] getAnnotationControllers() {
		return null;
	}
	/**
	 * 获取拥有者核心接口
	 * 在函数参数上有注释 {@link Module} 或注释 {@link Application} 时才能获取到拥有者核心接口对象
	 * @param parameter
	 * @return
	 */
	default ICoreInterface positionOwner(@NotNull NameParameter parameter) {
		ICoreInterface coreInterface = null;
		//获取模块注释
		Module module = this.getModuleAnnotation(parameter);
		//获取应用注释
		Application application = this.getApplicationAnnotation(parameter);
		//判断是否有模块注释
		if (module != null) {
			//判断是否指定模块名称
			if (module.value().equals("")) {
				//没有模块名称时重模块线程上下文获取模块对象
				coreInterface = ModuleThreadLocal.get().getModule();
			} else {
				//有指定模块名称，从应用中获取指定名称的模块对象
				coreInterface = this.getApp().getModule(module.value());
			}
			//判断模块对象是否有效获取
			if (coreInterface == null) {
				throw new InjectionClassAnnotationNotModuleException(module.value());
			}
			return coreInterface;
		}
		//判断是否有注释应用
		if (application != null) {
			//从应用获取核心接口
			return this.getApp();
		}
		//使用参数类型获取核心接口
		return this.getApplicationHomeModule(parameter.getDeclaringExecutable().getDeclaringClass());
	}
	/**
	 * 获取代码位置所属模块核心接口
	 * @param c 类型
	 * @return
	 */
	default ICoreInterface getApplicationHomeModule(Class<?> c) {
		return this.getApp().getApplicationHomeModule(c);
	}
	/**
	 * 获取类型模块注释
	 * 1、如果类型没有 {@link Module} 注释将对 {@code @param <O>} 发起方进行Bean。
	 * 2、如果类型有 {@link Module} 注释但是没有指定模块名称将对当前线程上线文模块进行Bean，
	 * 如果当前模块上线文没有模块对象将引发错误。
	 * 3、如果类型有 {@link Module} 注释并指定模块名称时，
	 * 将从 {@link IApplication} 应用的Bean容器中获取 {@link IModuleContainer} 模块容器接口中获取模块并使用此模块进行Bean，
	 * 如果没有找到模块对象将引发错误。
	 * @param parameter 参数对象
	 * @return
	 */
	default Module getModuleAnnotation(@NotNull NameParameter parameter) {
		if (parameter.isAnnotationPresent(Module.class)) {
			return parameter.getAnnotation(Module.class);
		}
		return null;
	}
	/**
	 * 获取类型应用注释
	 * 1、如果类型没有 {@link Application} 注释将对 {@code @param <O>} 发起方进行Bean。
	 * 2、如果类型有 {@link Application} 注释将对应用 {@link IApplication} 应用的Bean进行绑定，
	 * @param parameter 参数对象
	 * @return
	 */
	default Application getApplicationAnnotation(@NotNull NameParameter parameter) {
		if (parameter.isAnnotationPresent(Application.class)) {
			return parameter.getAnnotation(Application.class);
		}
		return null;
	}
	/**
	 * 解析控制器函数参数
	 *
	 * @param requestMethod    请求函数
	 * @param parameter 控制器函数参数
	 * @return 返回参数值
	 * @throws ResolverException
	 */
	@Nullable
	Object resolveArgument(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
						   @NotNull IHttpRequestMethod requestMethod, @NotNull NameParameter parameter) throws ResolverException;
	/**
	 * 是否可以解析
	 *
	 * @param requestMethod 请求函数
	 * @param parameter     判断是否可以解析的参数
	 * @throws ResolverException
	 * @return
	 */
	boolean isResolver(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
					   @NotNull IHttpRequestMethod requestMethod, @NotNull NameParameter parameter) throws ResolverException;
	/**
	 * 判断自定义请求解析类型
	 * @param requestMethod
	 * @return
	 */
	default boolean isConsume(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
							  @NotNull IHttpRequestMethod requestMethod) {
		if (requestMethod.getConsumes().length == 0) {
			//未指定解析类型
			return true;
		} else {
			if (StringUtils.isEmpty(request.getContentType())) {
				return false;
			}
			MediaType type = MediaType.valueOf(request.getContentType());
			for (String consume : requestMethod.getConsumes()) {
				if (consume.equals(type.getType())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断请求模式
	 * @param requestMethod
	 * @return
	 */
	default boolean isMethod(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
							 @NotNull IHttpRequestMethod requestMethod) {
		if (requestMethod.getRequestMethods().length == 0) {
			//未指定请求类型
			return true;
		} else {
			for (RequestMethod method : requestMethod.getRequestMethods()) {
				if (method.name().equals(request.getMethod())) {
					return true;
				}
			}
		}
		return false;
	}
}