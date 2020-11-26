//package ghost.framework.web.mvc.context.http.request.method.returnValue;
//
//import ghost.framework.beans.annotation.constraints.NotNull;
//import ghost.framework.web.context.http.request.IHttpRequestMethod;
//import ghost.framework.web.context.http.request.method.returnValue.IRequestMethodReturnValueClassResolver;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.lang.annotation.Annotation;
//
///**
// * package: ghost.framework.web.mvc.context.http.request.method.returnValue
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:处理请求函数mvc返回值注释类型解析接口
// * @Date: 2020/2/29:16:38
// */
//public interface IRequestMethodViewReturnValueAnnotationResolver extends IRequestMethodReturnValueClassResolver {
//    /**
//     * @param requestMethod 请求函数
//     * @param returnValue 返回值对象
//     * @return
//     */
//    @Override
//    default boolean isResolver(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
//                               @NotNull IHttpRequestMethod requestMethod, @NotNull Object returnValue) {
//        return requestMethod.getMethod().isAnnotationPresent(this.getAnnotation());
//    }
//    /**
//     * 获取解析器的注释类型
//     *
//     * @return
//     */
//    @NotNull
//    Class<? extends Annotation> getAnnotation();
//}
