package ghost.framework.jsr310.converter.plugin;

import com.google.common.base.Objects;
import ghost.framework.beans.annotation.converter.ConverterFactory;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.converter.ConverterException;
import ghost.framework.jsr310.converter.BytesToLocalDateTimeConverter;
import ghost.framework.jsr310.converter.Jsr310Converter;
import ghost.framework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * package: ghost.framework.jsr310.convert.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description: {@link byte[]} to {@link LocalDateTime}
 * @Date: 2020/6/26:1:22
 */
@ConverterFactory
public class JSR310BytesToLocalDateTimeConverter implements BytesToLocalDateTimeConverter<byte[], LocalDateTime> {
    public JSR310BytesToLocalDateTimeConverter(@Autowired(required = false) ICoreInterface domain) {
        this.domain = domain;
    }

    private Object domain;

    @Override
    public Object getDomain() {
        return domain;
    }

    private final Class<?>[] sourceType = {byte[].class};

    @Override
    public Class<?>[] getSourceType() {
        return sourceType;
    }

    private final Class<?>[] targetType = {Duration.class};

    @Override
    public Class<?>[] getTargetType() {
        return targetType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JSR310BytesToLocalDateTimeConverter that = (JSR310BytesToLocalDateTimeConverter) o;
        return Objects.equal(domain, that.domain) &&
                Objects.equal(sourceType, that.sourceType) &&
                Objects.equal(targetType, that.targetType);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(domain, sourceType, targetType);
    }

    @Override
    public String toString() {
        return "JSR310BytesToLocalDateTimeConverter{" +
                "domain=" + (domain == null ? "" : domain.toString()) +
                ", sourceType=" + Arrays.toString(sourceType) +
                ", targetType=" + Arrays.toString(targetType) +
                '}';
    }

    @Override
    public boolean canConvert(byte[] source) {
        try {
            //判断是否可以转换
            if (CollectionUtils.isEmpty(source)) {
                return false;
            }
            return true;
        } catch (IllegalArgumentException e) {
        }
        return false;
    }

    @Override
    public LocalDateTime convert(byte[] source, String characterEncoding) throws ConverterException {
        try {
            return LocalDateTime.parse(Jsr310Converter.StringBasedConverter.toString(source, characterEncoding));
        } catch (UnsupportedEncodingException e) {
            throw new ConverterException(e.getMessage(), e);
        }
    }

    @Override
    public LocalDateTime convert(byte[] source) throws ConverterException {
        return LocalDateTime.parse(Jsr310Converter.StringBasedConverter.toString(source));
    }
}
