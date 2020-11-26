package ghost.framework.core.converter.primitive;

import ghost.framework.beans.annotation.constructor.Constructor;
import ghost.framework.beans.annotation.converter.ConverterFactory;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.converter.AbstractConverter;
import ghost.framework.context.converter.ConverterException;
import ghost.framework.context.converter.primitive.ConverterCharacter;
import ghost.framework.util.StringUtils;

import java.util.Arrays;

/**
 * package: ghost.framework.core.converter.primitive
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/27:10:19
 */
@ConverterFactory
public class DefaultConverterCharacter<S extends Object, T extends Character>
        extends AbstractConverter<S, T>
        implements ConverterCharacter<S, T> {
    @Constructor
    public DefaultConverterCharacter(@Autowired(required = false) ICoreInterface domain) {
        super(domain);
    }

    public DefaultConverterCharacter() {
        super();
    }

    @Override
    public String toString() {
        return "DefaultConverterCharacter{" +
                "targetType=" + Arrays.toString(targetType) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DefaultConverterCharacter<?, ?> that = (DefaultConverterCharacter<?, ?>) o;
        return Arrays.equals(targetType, that.targetType);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(targetType);
        return result;
    }

    private final Class<?>[] targetType = {Character.class, char.class};

    @Override
    public Class<?>[] getTargetType() {
        return targetType;
    }

    @Override
    public boolean canConvert(S source) {
        try {
            Character.valueOf(source.toString().charAt(0));
            return true;
        } catch (IllegalArgumentException e) {
        }
        return false;
    }

    @Override
    public T convert(S source) {
        try {
            //判断是否可以转换
            if (StringUtils.isEmpty(source)) {
                return (T) Character.valueOf('\u0000');
            }
            return (T) Character.valueOf(source.toString().charAt(0));
        } catch (Exception e) {
            throw new ConverterException(e.getMessage(), e);
        }
    }
}