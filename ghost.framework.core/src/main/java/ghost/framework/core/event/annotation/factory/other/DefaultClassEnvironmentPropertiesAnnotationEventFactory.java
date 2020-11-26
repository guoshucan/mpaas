//package ghost.framework.core.event.annotation.factory.other;
//
//import ghost.framework.beans.annotation.injection.Autowired;
//import ghost.framework.beans.configuration.environment.EnvironmentCompatibilityMode;
//import ghost.framework.beans.configuration.environment.annotation.EnvironmentExclude;
//import ghost.framework.beans.configuration.environment.annotation.EnvironmentExcludeValue;
//import ghost.framework.beans.configuration.environment.annotation.EnvironmentProperties;
//import ghost.framework.beans.annotation.constraints.NotNull;
//import ghost.framework.beans.annotation.constraints.Nullable;
//import ghost.framework.context.application.IApplication;
//import ghost.framework.context.application.IApplicationEnvironment;
//import ghost.framework.context.base.ICoreInterface;
//import ghost.framework.context.environment.IEnvironmentPrefix;
//import ghost.framework.context.environment.IEnvironmentReader;
//import ghost.framework.core.event.ApplicationOwnerEventFactory;
//import ghost.framework.context.event.annotation.IClassAnnotationEventTargetHandle;
//import ghost.framework.context.exception.InjectorAnnotationEnvironmentException;
//import ghost.framework.context.exception.InjectorAnnotationEnvironmentNullException;
//import ghost.framework.context.module.environment.IModuleEnvironment;
//import ghost.framework.util.ReflectUtil;
//import ghost.framework.util.StringUtil;
//import ghost.framework.util.StringUtils;
//
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Field;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * package: ghost.framework.core.event.annotation.factory.built
// *
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:类型 {@link ghost.framework.beans.configuration.environment.annotation.EnvironmentProperties} 注释事件工厂类
// * @Date: 2020/1/10:20:30
// * @param <O> 发起方类型
// * @param <T> 目标类型
// * @param <E> 注入绑定事件目标处理类型
// * @param <V> 返回类型
// */
//public class DefaultClassEnvironmentPropertiesAnnotationEventFactory<
//        O extends ICoreInterface,
//        T extends Class<?>,
//        E extends IClassAnnotationEventTargetHandle<O, T, V, String, Object>,
//        V extends Object
//        >
//        extends ApplicationOwnerEventFactory<O, T, E>
//        implements IClassEnvironmentPropertiesAnnotationEventFactory<O, T, E, V> {
//    /**
//     * 初始化类型 {@link ghost.framework.beans.configuration.environment.annotation.EnvironmentProperties} 注释事件工厂类
//     * @param app 应用接口
//     */
//    public DefaultClassEnvironmentPropertiesAnnotationEventFactory(@Autowired IApplication app){
//        super(app);
//    }
//    /**
//     * 注释类型
//     */
//    private final Class<? extends Annotation> annotation = EnvironmentProperties.class;
//
//    /**
//     * 重新注释类型
//     *
//     * @return
//     */
//    @Override
//    public Class<? extends Annotation> getAnnotation() {
//        return annotation;
//    }
//    /**
//     * 绑定事件
//     *
//     * @param event 事件对象
//     */
//    @Override
//    public void loader(E event) {
//        //判断是否没有此注释或此注释已经在排除执行列表中
//        if (this.isLoader(event)) {
//            return;
//        }
//        this.getLog().debug("loader>class:" + event.getTarget().getName());
//        //获取注入注释对象
//        EnvironmentProperties properties = this.getApp().getProxyAnnotationObject(event.getTarget().getAnnotation(EnvironmentProperties.class));
//        //获取核心接口
//        this.positionOwner(event);
//        //绑定依赖类型
//        for (Class<?> c : properties.type()) {
//            event.getExecuteOwner().addBean(c);
//        }
//        //构建类型实例
//        this.newInstance(event);
//        //获取env
//        IEnvironmentPrefix environment = null;
//        //判断发起方是否为应用env
//        if (event.getOwner() instanceof IApplicationEnvironment) {
//            environment = event.getExecuteOwner().getBean(IApplicationEnvironment.class).getPrefix(properties.prefix());
//        }
//        //判断发起方是否为模块env
//        if (event.getOwner() instanceof IModuleEnvironment) {
//            environment = event.getExecuteOwner().getBean(IModuleEnvironment.class).getPrefix(properties.prefix());
//        }
//        //判断env是否有效获取
//        if (environment == null) {
//            throw new InjectorAnnotationEnvironmentException(event.toString());
//        }
//        //注入参数
//        //获取读取env
//        //获取应用注入器注释env配置解析器接口
//        this.injector(environment.getReader(properties.prefix()), event.getValue());
//        //判断服务注释是否指定名称
//        if (!properties.name().equals("")) {
//            //指定绑定的名称
//            event.setName(properties.name());
//        }
//        //绑定入容器中
//        if (StringUtils.isEmpty(event.getName())) {
//            event.getExecuteOwner().addBean(event.getValue());
//        } else {
//            event.getExecuteOwner().addBean(event.getName(), event.getValue());
//        }
//        //设置构建对象完成
//        event.setHandle(true);
//    }
//    /**
//     * 注入env参数
//     *
//     * @param environmentReader env读取
//     * @param obj               要注入的对象
//     */
//    private void injector(IEnvironmentReader environmentReader, V obj) {
//        this.getLog().info("injector:" + environmentReader.toString() + "->" + obj.toString());
//        //获取env配置注释
//        EnvironmentProperties properties = this.getApp().getProxyAnnotationObject(obj.getClass().getAnnotation(EnvironmentProperties.class));
//        //获取是否有排除注释
//        EnvironmentExclude exclude = obj.getClass().getAnnotation(EnvironmentExclude.class);
//        //对象声明地图
//        Map<String, Field> map = new ConcurrentHashMap<>();
//        //遍历对象例列表
//        for (Field field : obj.getClass().getDeclaredFields()) {
//            //判断是否为排除值注释
//            if (field.isAnnotationPresent(EnvironmentExcludeValue.class)) {
//                continue;
//            }
//            //排除静态声明
//            if (ReflectUtil.isStaticField(field)) {
//                continue;
//            }
//            map.put(field.getName(), field);
//        }
//        //排除例
//        if (exclude != null) {
//            for (String name : exclude.value()) {
//                map.remove(name);
//            }
//        }
//        //遍历例
//        Object o = null;
//        for (Map.Entry<String, Field> entry : map.entrySet()) {
//            try {
//                //
//                o = environmentReader.getNullable(entry.getKey());
//                //判断声明是否注释可空
//                if (entry.getValue().isAnnotationPresent(Nullable.class)) {
//                    if (o != null) {
//                        ReflectUtil.setFieldValue(obj, entry.getValue(), o);
//                    }
//                    continue;
//                }
//                //判断是否注释不可空
//                if (entry.getValue().isAnnotationPresent(NotNull.class)) {
//                    if (o == null) {
//                        throw new InjectorAnnotationEnvironmentNullException(entry.getKey());
//                    }
//                    ReflectUtil.setFieldValue(obj, entry.getValue(), environmentReader.get(entry.getKey()));
//                    continue;
//                }
//                //判断兼容模式处理
//                if (properties.compatibilityMode() == EnvironmentCompatibilityMode.IgnoreInvalid) {
//                    if (o != null) {
//                        ReflectUtil.setFieldValue(obj, entry.getValue(), o);
//                    }
//                    continue;
//                }
//                if (properties.compatibilityMode() == EnvironmentCompatibilityMode.AllMatch && o == null) {
//                    throw new InjectorAnnotationEnvironmentNullException(entry.getKey());
//                }
//                ReflectUtil.setFieldValue(obj, entry.getValue(), o);
//            } catch (Exception e) {
//                throw new InjectorAnnotationEnvironmentException(entry.getKey(), e);
//            }
//        }
//    }
//    /**
//     * 删除绑定事件
//     *
//     * @param event 事件对象
//     */
//    @Override
//    public void unloader(E event) {
//        //判断是否没有此注释或此注释已经在排除执行列表中
//        if (this.isLoader(event)) {
//            return;
//        }
//        this.getLog().info("unloader:" + event.toString());
//        //获取注入注释对象
//        EnvironmentProperties properties = this.getApp().getProxyAnnotationObject(event.getTarget().getAnnotation(EnvironmentProperties.class));
//        //获取核心接口
//        this.positionOwner(event);
//        //判断服务注释是否指定名称
//        if (!properties.name().equals("")) {
//            //指定绑定的名称
//            event.setName(properties.name());
//        }
//        //删除本身
//        if (event.getName().equals("")) {
//            event.getExecuteOwner().removeBean(event.getTarget());
//        } else {
//            event.getExecuteOwner().removeBean(event.getName());
//        }
//        //设置构建对象完成
//        event.setHandle(true);
//    }
//}