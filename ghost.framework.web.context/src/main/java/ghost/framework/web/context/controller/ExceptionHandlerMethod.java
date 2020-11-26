package ghost.framework.web.context.controller;

import ghost.framework.context.parameter.NameParameter;
import ghost.framework.web.context.bind.annotation.RestControllerAdvice;
import ghost.framework.web.context.http.request.IHttpRequestMethod;
import ghost.framework.context.method.MethodTarget;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/**
 * package: ghost.framework.web.module.controller
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:错误处理函数
 * @Date: 2020/2/28:20:17
 */
public final class ExceptionHandlerMethod extends MethodTarget implements IHttpRequestMethod {
    /**
     * 初始化错误处理函数
     *
     * @param target    错误处理目标对象
     * @param method    错误处理函数
     * @param exception 错误对象
     */
    public ExceptionHandlerMethod(Object target, Method method, Class<? extends Throwable> exception) {
        super(target, method);
        this.advice = this.target.getClass().getAnnotation(RestControllerAdvice.class);
        this.exception = exception;
    }

    private RestControllerAdvice advice;

    /**
     * 指定处理的包
     * 通配符格式 ghost.framework.web.module.controller.*
     * *表示该包下全部包的通配符
     * 包格式 ghost.framework.web.module.controller
     * 表示该包下全部控制器类型
     *
     * @return
     */
    public String[] getBasePackages() {
        return advice.basePackages();
    }

    /**
     * 指定处理的类型
     *
     * @return
     */
    public Class<?>[] getBasePackageClasses() {
        return advice.basePackageClasses();
    }

    /**
     * 指定处理的注释
     *
     * @return
     */
    public Class<? extends Annotation>[] getAnnotations() {
        return advice.annotations();
    }

    @Override
    public Object getTarget() {
        return super.getTarget();
    }

    @Override
    public NameParameter[] getParameters() {
        return super.getParameters();
    }

    @Override
    public Method getMethod() {
        return super.getMethod();
    }

    /**
     * 错误类型
     */
    private Class<? extends Throwable> exception;

    @Override
    public String toString() {
        return "ExceptionHandlerMethod{" +
                "target=" + target.toString() +
                ", method=" + method.getName() +
                ", exception=" + exception.toString() +
                '}';
    }

    /**
     * 调用函数
     * @param parameters
     * @return
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Override
    public Object invoke(Object[] parameters) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return this.method.invoke(this.target, parameters);
    }
}