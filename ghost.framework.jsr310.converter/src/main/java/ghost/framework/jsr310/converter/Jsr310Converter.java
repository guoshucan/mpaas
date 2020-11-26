package ghost.framework.jsr310.converter;

import ghost.framework.context.converter.TypeConverter;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * package: ghost.framework.jsr310.converter
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/26:1:33
 */
public interface Jsr310Converter<S, T> extends TypeConverter<S, T> {
    class StringBasedConverter {
        public static byte[] fromString(String source) {
            return source.getBytes(StandardCharsets.UTF_8);
        }

        public static String toString(byte[] source) {
            return new String(source, StandardCharsets.UTF_8);
        }

        public static byte[] fromString(String source, String characterEncoding) throws UnsupportedEncodingException {
            return source.getBytes(characterEncoding);
        }

        public static String toString(byte[] source, String characterEncoding) throws UnsupportedEncodingException {
            return new String(source, characterEncoding);
        }
    }
}