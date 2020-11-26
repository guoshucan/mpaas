package ghost.framework.core.converter.primitive;

import ghost.framework.beans.annotation.constructor.Constructor;
import ghost.framework.beans.annotation.converter.ConverterFactory;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.converter.AbstractConverter;
import ghost.framework.context.converter.ConverterException;
import ghost.framework.context.converter.EncodingTypeConverter;
import ghost.framework.context.converter.primitive.ConverterDate;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

/**
 * package: ghost.framework.core.converter.primitive
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/27:10:19
 */
@ConverterFactory
public class DefaultConverterDate<S extends Object, T extends Date>
        extends AbstractConverter<S, T>
        implements ConverterDate<S, T>, EncodingTypeConverter<S, T> {
    @Constructor
    public DefaultConverterDate(@Autowired(required = false) ICoreInterface domain) {
        super(domain);
    }

    public DefaultConverterDate() {
        super();
    }

    @Override
    public String toString() {
        return "DefaultConverterDate{" +
                "targetType=" + Arrays.toString(targetType) +
                ", dateFormat=" + dateFormat +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DefaultConverterDate<?, ?> that = (DefaultConverterDate<?, ?>) o;
        return Arrays.equals(targetType, that.targetType) &&
                dateFormat.equals(that.dateFormat);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(super.hashCode(), dateFormat);
        result = 31 * result + Arrays.hashCode(targetType);
        return result;
    }

    private final Class<?>[] targetType = {Date.class};

    @Override
    public Class<?>[] getTargetType() {
        return targetType;
    }

    @Override
    public boolean canConvert(S source) {
        try {
            dateFormat.parse(source.toString());
            return true;
        } catch (IllegalArgumentException | ParseException e) {
        }
        return false;
    }

    private DateFormat dateFormat = DateFormat.getDateInstance();

    @Override
    public T convert(S source) throws ConverterException {
        try {
            return (T) dateFormat.parse(source.toString());
        } catch (ParseException e) {
            throw new ConverterException(e.getMessage(), e);
        }
    }

    @Override
    public T convert(S source, String characterEncoding) throws ConverterException {
        try {
            return this.convert((S) new String(source.toString().getBytes(characterEncoding)));
        } catch (UnsupportedEncodingException e) {
            throw new ConverterException(e.getMessage(), e);
        }
    }

    @Override
    public T convert(S source, DateFormat format, String characterEncoding) throws ConverterException {
        try {
            return (T) format.parse(new String(source.toString().getBytes(characterEncoding)));
        } catch (ParseException | UnsupportedEncodingException e) {
            throw new ConverterException(e.getMessage(), e);
        }
    }

    @Override
    public T convert(S source, DateFormat format) throws ConverterException {
        try {
            return (T) format.parse(source.toString());
        } catch (ParseException e) {
            throw new ConverterException(e.getMessage(), e);
        }
    }
}