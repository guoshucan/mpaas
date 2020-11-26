package ghost.framework.core.bean.factory.configuration;

import ghost.framework.beans.annotation.bean.factory.ClassAnnotationBeanFactory;
import ghost.framework.beans.annotation.configuration.ConfigurationClassProperties;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.tags.AnnotationTag;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.AbstractClassAnnotationBeanFactory;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.context.bean.factory.configuration.IClassConfigurationClassPropertiesAnnotationBeanFactory;
import ghost.framework.context.converter.ConverterContainer;
import ghost.framework.context.converter.properties.ResourceBytesConverterProperties;
import ghost.framework.context.environment.IEnvironment;
import ghost.framework.context.resolver.IResolverFactoryContainer;
import ghost.framework.context.utils.AssemblyUtil;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.util.Properties;

/**
 * package: ghost.framework.core.bean.factory.configuration
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:类型 {@link ConfigurationClassProperties} 注释事件工厂类
 * @Date: 12:33 2020/1/11
 * @param <O> 发起方类型
 * @param <T> 目标类型
 * @param <E> 注入绑定事件目标处理类型
 * @param <V> 返回类型
 */
@ClassAnnotationBeanFactory(tag = AnnotationTag.AnnotationTags.Configuration)
public class ClassConfigurationClassPropertiesAnnotationBeanFactory<
        O extends ICoreInterface,
        T extends Class<?>,
        E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
        V extends Object
        >
        extends AbstractClassAnnotationBeanFactory<O, T, E, V>
        implements IClassConfigurationClassPropertiesAnnotationBeanFactory<O, T, E, V> {
    /**
     * 注释类型
     */
    private final Class<? extends Annotation> annotation = ConfigurationClassProperties.class;
    /**
     * 注入解析器容器接口
     */
    @Autowired
//    @Application
    private IResolverFactoryContainer resolverFactoryContainer;
    /**
     * 注入转换器工厂容器接口
     */
//    @Module
    @Autowired
    private ConverterContainer converterFactoryContainer;
    /**
     * 重新注释类型
     *
     * @return
     */
    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return annotation;
    }

    @Override
    public String toString() {
        return "ClassConfigurationClassPropertiesAnnotationBeanFactory{" +
                "annotation=" + annotation +
                '}';
    }

    /**
     * @param event 事件对象
     */
    @Override
    public void loader(E event) {
        this.getLog().debug("loader>class:" + event.getTarget().getName());
        //获取注入注释对象
        ConfigurationClassProperties classProperties = this.getAnnotation(event);
        //定位目标拥有者
        this.positionOwner(event);
        //构建培训对象
        Object propertiesObject = event.getExecuteOwner().newInstance(classProperties.value());
        //判断配置文件
        if (classProperties.path().equals("")) {
            //注入拥有者env值
            IEnvironment env = this.getEnvironment(event);
            //判断前缀
            if (classProperties.prefix().equals("")) {
                //不指定前缀
                this.resolverFactoryContainer.resolver(env, propertiesObject, classProperties.compatibleMode());
            } else {
                //指定前缀
                this.resolverFactoryContainer.resolver(env.getPrefix(classProperties.prefix()), propertiesObject, classProperties.compatibleMode());
            }
        } else {
            //注入配置文件值
            //获取转换器
            ResourceBytesConverterProperties converterProperties = (ResourceBytesConverterProperties) this.converterFactoryContainer.getTypeConverter(ResourceBytesConverterProperties.class);
            //转换配置
            Properties properties =
                    (Properties) converterProperties.convert(
                            AssemblyUtil.getResourceBytes(event.getTarget().getProtectionDomain().getCodeSource().getLocation(), classProperties.path()), "UTF-8");
            //解析
            this.resolverFactoryContainer.resolver(properties, propertiesObject, classProperties.compatibleMode());
        }
        //判断绑定名称
        if (!classProperties.name().equals("")) {
            event.setName(classProperties.name());
        }
        if (StringUtils.isEmpty(event.getName())) {
            event.getExecuteOwner().addBean(propertiesObject);
        } else {
            event.getExecuteOwner().addBean(event.getName(), propertiesObject);
        }
    }

    /**
     * 删除绑定
     *
     * @param event 事件对象
     */
    @Override
    public void unloader(E event) {
        //注释类型不对与已经执行的注释时错误
        this.isLoader(event);
        this.getLog().info("unloader:" + event.toString());
    }
}