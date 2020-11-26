package ghost.framework.core.bean;

import ghost.framework.context.bean.IBeanDefinition;
import ghost.framework.util.StringUtil;
import ghost.framework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.Objects;
/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:绑定定义
 * @Date: 20:17 2019/5/17
 */
public class BeanDefinition implements IBeanDefinition {
    /**
     * 初始化绑定定义
     *
     * @param name   绑定key
     * @param object 绑定类型
     * @throws IllegalArgumentException
     */
    public BeanDefinition(String name, Object object) throws IllegalArgumentException {
        this.name = StringUtil.inEmptyToNull(name);
        this.object = object;
        if (this.name == null && object == null) {
            throw new IllegalArgumentException("value and object is null error");
        }
        if (this.name == null) {
            this.name = this.object.getClass().getName();
        }
    }

    /**
     * 获取绑定定义对象数组注释对象
     *
     * @return
     */
    @Override
    public Annotation[] getAnnotations() {
        return this.object.getClass().getAnnotations();
    }

    /**
     * 重写哈希
     *
     * @return
     */
    @Override
    public int hashCode() {
        int i = super.hashCode();
        if (this.name != null) {
            i += this.name.hashCode();
        }
        if (this.object != null) {
            i += this.object.hashCode();
        }
        ;
        return i;
    }

    /**
     * 重写是否同等
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof BeanDefinition) {
            BeanDefinition d = (BeanDefinition) obj;
            if (this.name.equals(d.name) && Objects.equals(this.object, d.object)) {
                return true;
            }
        }
        return super.equals(obj);
    }

    /**
     * 绑定对象
     */
    protected Object object;

    /**
     * 获取绑定对象
     *
     * @return
     */
    @Override
    public Object getObject() {
        return object;
    }

    /**
     * 初始化绑定定义
     *
     * @param object 绑定对象
     */
    public BeanDefinition(Object object) {
        this(object.getClass().getName(), object);
    }

    /**
     * 绑定name
     */
    protected String name;

    /**
     * 获取绑定key
     *
     * @return
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * 重写字符串
     *
     * @return
     */
    @Override
    public String toString() {
        if (StringUtils.isEmpty(this.name)) {
            return "depend " + this.object.getClass().getName();
        }
        return this.name + " of depend " + this.object.getClass().getName();
    }
}