package ghost.framework.core.valid;

import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.context.parameter.NameParameter;
import ghost.framework.context.valid.AnnotationValidBeanFactory;
import ghost.framework.context.valid.IAnnotationValidFactoryContainer;
import ghost.framework.context.valid.ValidException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * package: ghost.framework.core.valid
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:注释验证工厂容器
 * @Date: 2020/2/29:14:00
 */
@Component
public final class DefaultAnnotationValidFactoryContainer<T extends AnnotationValidBeanFactory>
        extends AbstractCollection<T>
        implements IAnnotationValidFactoryContainer<T> {
    private List<T> list = new ArrayList();

    @Override
    public boolean add(T t) {
        return list.add(t);
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

    @Override
    public int size() {
        return list.size();
    }
    /**
     * 验证函数参数解析
     * @param target 函数目标对象
     * @param method 函数
     * @param parameter 函数参数
     * @param parameterValue 函数参数值
     * @throws ValidException
     */
    @Override
    public void validResolver(Object target, Method method, NameParameter parameter, Object parameterValue) throws ValidException {

    }
    /**
     * 验证声明参数解析
     * @param target 声明目标对象
     * @param field 声明
     * @param fieldValue 声明值
     * @throws ValidException
     */
    @Override
    public void validResolver(Object target, Field field, Object fieldValue) throws ValidException {

    }
    /**
     * 验证对象参数解析
     * @param target 目标对象
     * @throws ValidException
     */
    @Override
    public void validResolver(Object target) throws ValidException {

    }
}