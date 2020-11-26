//package ghost.framework.web.module.http.request.method.returnValue;
//
//import ghost.framework.context.resolver.ResolverException;
//import ghost.framework.web.context.bens.annotation.ResponseBody;
//import ghost.framework.web.context.http.request.method.returnValue.AbstractHandlerRequestMethodReturnValueAnnotationResolver;
//
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Method;
//
///**
// * package: ghost.framework.web.module.http.request.method
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 2020/2/29:16:41
// */
//public class HandlerRequestMethodReturnValueResponseBodyAnnotationResolver extends AbstractHandlerRequestMethodReturnValueAnnotationResolver {
//    private final Class<? extends Annotation> a = ResponseBody.class;
//
//    @Override
//    public Class<? extends Annotation> getAnnotation() {
//        return a;
//    }
//
//    @Override
//    public Object handleReturnValue(Object target, Method method, Object returnValue) throws ResolverException {
//        return null;
//    }
//}