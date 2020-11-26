//package ghost.framework.core.parser.annotation.environment;
//
//import ghost.framework.beans.configuration.environment.EnvironmentCompatibilityMode;
//import ghost.framework.beans.configuration.environment.annotation.EnvironmentExclude;
//import ghost.framework.beans.configuration.environment.annotation.EnvironmentExcludeValue;
//import ghost.framework.beans.configuration.environment.annotation.EnvironmentProperties;
//import ghost.framework.beans.annotation.constraints.NotNull;
//import ghost.framework.beans.annotation.constraints.Nullable;
//import ghost.framework.context.env.IEnvironmentReader;
//import ghost.framework.context.exception.InjectorAnnotationEnvironmentException;
//import ghost.framework.context.exception.InjectorAnnotationEnvironmentNullException;
//import ghost.framework.util.ReflectUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.lang.reflect.Field;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * package: ghost.framework.core.parser.annotation.environment
// *
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:注入器注释env配置解析器类
// * @Date: 2020/1/10:21:06
// */
//public class InjectorAnnotationEnvironmentPropertiesParser implements IInjectorAnnotationEnvironmentPropertiesParser {
//    /**
//     * 日志
//     */
//    private Logger log = LoggerFactory.getLogger(this.getClass());
//
//    /**
//     * 注入env参数
//     *
//     * @param environmentReader env读取
//     * @param obj               要注入的对象
//     */
//    @Override
//    public void injector(IEnvironmentReader environmentReader, Object obj) {
//        this.log.info("injector:" + environmentReader.toString() + "->" + obj.toString());
//        //获取env配置注释
//        EnvironmentProperties properties = obj.getClass().getAnnotation(EnvironmentProperties.class);
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
//                if (entry.getOrderValue().isAnnotationPresent(Nullable.class)) {
//                    if (o != null) {
//                        ReflectUtil.setFieldValue(obj, entry.getOrderValue(), o);
//                    }
//                    continue;
//                }
//                //判断是否注释不可空
//                if (entry.getOrderValue().isAnnotationPresent(NotNull.class)) {
//                    if (o == null) {
//                        throw new InjectorAnnotationEnvironmentNullException(entry.getKey());
//                    }
//                    ReflectUtil.setFieldValue(obj, entry.getOrderValue(), environmentReader.get(entry.getKey()));
//                    continue;
//                }
//                //判断兼容模式处理
//                if (properties.compatibilityMode() == EnvironmentCompatibilityMode.IgnoreInvalid) {
//                    if (o != null) {
//                        ReflectUtil.setFieldValue(obj, entry.getOrderValue(), o);
//                    }
//                    continue;
//                }
//                if (properties.compatibilityMode() == EnvironmentCompatibilityMode.AllMatch && o == null) {
//                    throw new InjectorAnnotationEnvironmentNullException(entry.getKey());
//                }
//                ReflectUtil.setFieldValue(obj, entry.getOrderValue(), o);
//            } catch (Exception e) {
//                throw new InjectorAnnotationEnvironmentException(entry.getKey(), e);
//            }
//        }
//    }
//}