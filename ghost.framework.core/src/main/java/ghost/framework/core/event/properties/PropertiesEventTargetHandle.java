//package ghost.framework.core.event.properties;
//
//import ghost.framework.context.event.EventTargetHandle;
//
//import java.util.Properties;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description: {@link Properties} 事件目标处理类
// * @Date: 18:14 2020/1/14
// */
//public class PropertiesEventTargetHandle<O, T, P extends Properties> extends EventTargetHandle<O, T> implements IPropertiesEventTargetHandle<O, T, P> {
//    /**
//     * 初始化事件不表处理头
//     *
//     * @param owner      设置事件目标对象拥有者
//     * @param target     设置目标对象
//     * @param properties 配置
//     */
//    public PropertiesEventTargetHandle(O owner, T target, P properties) {
//        super(owner, target);
//        this.properties = properties;
//    }
//
//    /**
//     * 配置文件关联的env对象
//     */
//    private P properties;
//
//    /**
//     * 获取配置
//     *
//     * @return
//     */
//    @Override
//    public P getProperties() {
//        return properties;
//    }
//    /**
//     * 设置配置
//     * @param properties
//     */
//    @Override
//    public void setProperties(P properties) {
//        this.properties = properties;
//    }
//}