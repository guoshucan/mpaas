package ghost.framework.context.bean;

import ghost.framework.beans.annotation.order.Order;

import java.lang.reflect.Method;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:绑定注释函数
 * @Date: 21:48 2019/12/1
 */
public class BeanMethod {
    /**
     * 函数执行排序位置
     */
    private int order;

    public int getOrder() {
        return order;
    }

    /**
     * 绑定函数
     */
    private Method method;

    public Method getMethod() {
        return method;
    }

    /**
     * 初始化绑定注释函数
     * @param method
     */
    public BeanMethod(Method method) {
        this.method = method;
        //获取注释位置
        if (this.method.isAnnotationPresent(Order.class)) {
            this.order = this.method.getAnnotation(Order.class).value();
        }
    }
}