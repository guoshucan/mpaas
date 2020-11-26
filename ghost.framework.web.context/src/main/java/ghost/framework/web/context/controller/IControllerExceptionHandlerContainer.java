package ghost.framework.web.context.controller;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.web.context.bind.annotation.RestControllerAdvice;
import ghost.framework.web.context.http.request.IHttpRequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * package: ghost.framework.web.module
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description: {@link RestControllerAdvice} 控制器全局注释错误处理容器接口
 * Map<错误类型, 错误类型所注释函数所在的对象>
 * @Date: 2020/2/28:0:09
 */
public interface IControllerExceptionHandlerContainer extends Map<Class<? extends Throwable>, ExceptionHandlerMethod> {
    /**
     * 获取同步对象
     *
     * @return
     */
    Object getRoot();

    /**
     * 控制器错误处理
     * 在 {@see WebControllerCglibCallback::throwing} 错误处理时在此处理
     * @param requestMethod    请求函数
     * @param throwable 函数错误对象
     * @throws Throwable
     */
    void exception(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                   @NotNull IHttpRequestMethod requestMethod, @NotNull Throwable throwable) throws Throwable;
}