//package ghost.framework.core.event.properties;
//import ghost.framework.context.environment.Environment;
//import ghost.framework.context.environment.IEnvironment;
//import java.util.Properties;
///**
// * package: ghost.framework.core.event.properties
// *
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description: {@link Properties} 合并 {@link Environment} 事件目标处理类
// * @Date: 2020/2/4:15:21
// */
//public class PropertiesMergeEnvironmentEventTargetHandle<O, T, P extends Properties, IE extends IEnvironment> extends PropertiesEventTargetHandle<O, T, P> implements IPropertiesMergeEnvironmentEventTargetHandle<O, T, P, IE> {
//    /**
//     * 初始化事件不表处理头
//     *
//     * @param owner       设置事件目标对象拥有者
//     * @param target      设置目标对象
//     * @param properties  配置
//     * @param environment 配置文件关联的env对象
//     */
//    public PropertiesMergeEnvironmentEventTargetHandle(O owner, T target, P properties, IE environment) {
//        super(owner, target, properties);
//        this.environment = environment;
//    }
//
//    /**
//     * 配置文件关联的env对象
//     */
//    private IE environment;
//
//    /**
//     * 获取配置文件关联的env对象
//     *
//     * @return
//     */
//    @Override
//    public IE getEnvironment() {
//        return environment;
//    }
//}