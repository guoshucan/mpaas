//package ghost.framework.beans;
//
//import ghost.framework.beans.annotation.Key;
//import ghost.framework.util.StringUtil;
//import org.apache.commons.lang3.StringUtils;
//
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:绑定定义键，域的命名规则[域名:value]=等于map的key，如果没有域名侧[value]=map的key
// * @Date: 10:36 2019-06-09
// */
//public class BeanDefinitionKey {
//    /**
//     * 域分隔符
//     */
//    public static final String ScopeSeparator = ":";
//    /**
//     * 初始化绑定定义键
//     *
//     * @param value 绑定id
//     */
//    public BeanDefinitionKey(String value) {
//        this.value = value;
//        if (StringUtils.isEmpty(this.value)) {
//            throw new IllegalArgumentException("value is null error");
//        }
//    }
//    public BeanDefinitionKey(String value, Object o) {
//        this(value, o.getClass());
//    }
//    public BeanDefinitionKey(String value, Class<?> c) {
//        this.value = StringUtil.inEmptyToNull(value);
//        //绑定定义域名称
//        if (c.isAnnotationPresent(Key.class)) {
//            this.value = c.getAnnotation(Key.class).value();
//        }
//        if (this.value == null) {
//            this.value = c.getName();
//        }
//    }
//    @Override
//    public int hashCode() {
//        int i = super.hashCode();
//        if (this.value != null) {
//            i += this.value.hashCode();
//        }
//        return i;
//    }
//
//    @Override
//    public String toString() {
//        return this.getName();
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//        BeanDefinitionKey value = (BeanDefinitionKey) obj;
//        if (this.value.equals(value.value)) {
//            return true;
//        }
//        return super.equals(obj);
//    }
//
//    protected String value;
//    /**
//     * 获取域与键的组合
//     * 格式域:键
//     *
//     * @return
//     */
//    public String getName() {
//        return this.value;
//    }
//
//    /**
//     * 初始化绑定定义键
//     *
//     * @param c 绑定类型
//     */
//    public BeanDefinitionKey(Class<?> c) throws IllegalArgumentException {
//        //绑定定义id
//        if (c.isAnnotationPresent(Key.class)) {
//            this.value = c.getAnnotation(Key.class).value();
//        } else {
//            this.value = c.getName();
//        }
//    }
//
//    /**
//     * 初始化绑定定义键
//     *
//     * @param o 绑定对象
//     */
//    public BeanDefinitionKey(Object o) {
//        this(o.getClass());
//    }
//}