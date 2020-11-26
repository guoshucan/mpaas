package ghost.framework.core.resolverFactory;

import ghost.framework.beans.annotation.factory.ResolverFactory;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.resolver.ResolverCompatibleMode;
import ghost.framework.context.converter.ConverterContainer;
import ghost.framework.context.environment.IEnvironment;
import ghost.framework.context.resolver.IEnvironmentResolverObjectFactory;
import ghost.framework.context.resolver.ResolverException;
import ghost.framework.util.ReflectUtil;

import java.lang.reflect.Field;
import java.util.List;

/**
 * package: ghost.framework.core.resolverFactory
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description: {@link IEnvironment}解析对象类
 * @Date: 2020/3/21:17:53
 */
@ResolverFactory
public class EnvironmentResolverObjectFactory<S extends IEnvironment, T extends Object>
        extends AbstractResolverObjectFactory<S, T>
        implements IEnvironmentResolverObjectFactory<S, T> {
    private final Class<?> sourceType = IEnvironment.class;

    @Override
    public Class<?> getSourceType() {
        return sourceType;
    }

    /**
     * 注入转换器工厂
     */
    @Autowired
    private ConverterContainer converterContainer;

    /**
     * 解析目标
     *
     * @param source 解析源
     * @param target 解析目标
     */
    @Override
    public void resolver(S source, T target) throws ResolverException {
        this.resolver(source, target, ResolverCompatibleMode.IgnoreNullAndEmpty);
    }

    /**
     * 解析目标
     *
     * @param source         解析源
     * @param target         解析目标
     * @param compatibleMode 解析兼容模式
     */
    @Override
    public void resolver(S source, T target, ResolverCompatibleMode compatibleMode) throws ResolverException {
        try {
            //获取声明列表
            List<Field> fields = ReflectUtil.getAllField(target.getClass());
            switch (compatibleMode) {
                case Default:
                    for (Field field : fields) {
                        field.setAccessible(true);
                        field.set(target, this.converterContainer.convert(field.getType(), (Object) source.get(field.getName())));
                    }
                    return;
                case IgnoreNull:
                    for (Field field : fields) {
                        String s = source.getNullable(field.getName());
                        if (s != null) {
                            field.setAccessible(true);
                            field.set(target, this.converterContainer.convert(field.getType(), s));
                        }
                    }
                    return;
                case IgnoreEmpty:
                    for (Field field : fields) {
                        String s = source.getNullable(field.getName());
                        if (s != null && s.equals("")) {
                            continue;
                        }
                        field.setAccessible(true);
                        field.set(target, this.converterContainer.convert(field.getType(), s));
                    }
                    return;
                case IgnoreNullAndEmpty:
                    for (Field field : fields) {
                        String s = source.getNullable(field.getName());
                        if (s == null || s.equals("")) {
                            continue;
                        }
                        field.setAccessible(true);
                        field.set(target, this.converterContainer.convert(field.getType(), s));
                    }
                    return;
            }
        } catch (Exception e) {
            throw new ResolverException(e.getMessage(), e);
        }
    }
}