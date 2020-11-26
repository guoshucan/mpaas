package ghost.framework.context.valid;

import ghost.framework.context.parameter.NameParameter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * package: ghost.framework.context.valid
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:注释验证工厂容器接口
 * @Date: 2020/2/29:13:59
 */
public interface IAnnotationValidFactoryContainer<T extends AnnotationValidBeanFactory> extends Collection<T> {
    /**
     * 验证函数参数解析
     * @param target 函数目标对象
     * @param method 函数
     * @param parameter 函数参数
     * @param parameterValue 函数参数值
     * @throws ValidException
     */
    void validResolver(Object target, Method method, NameParameter parameter, Object parameterValue) throws ValidException;
    /**
     * 验证声明参数解析
     * @param target 声明目标对象
     * @param field 声明
     * @param fieldValue 声明值
     * @throws ValidException
     */
    void validResolver(Object target, Field field, Object fieldValue) throws ValidException;
    /**
     * 验证对象参数解析
     * @param target 目标对象
     * @throws ValidException
     */
    void validResolver(Object target) throws ValidException;
}