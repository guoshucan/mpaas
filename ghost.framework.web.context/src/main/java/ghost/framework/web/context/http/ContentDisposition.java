package ghost.framework.web.context.http;

/**
 * package: ghost.framework.web.module.http
 *
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2019-11-15:1:35
 */

import ghost.framework.util.Assert;
import ghost.framework.util.ObjectUtils;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME;

/**
 * Represent the Content-Disposition depend and parameters as defined in RFC 2183.
 *
 * @author Sebastien Deleuze
 * @author Juergen Hoeller
 * @since 5.0
 * @see <a href="https://tools.ietf.org/html/rfc2183">RFC 2183</a>
 */
public final class ContentDisposition {

    private static final String INVALID_HEADER_FIELD_PARAMETER_FORMAT =
            "Invalid header field parameter format (as defined in RFC 5987)";

    
    private final String type;

    
    private final String name;

    
    private final String filename;

    
    private final Charset charset;

    
    private final Long size;

    
    private final ZonedDateTime creationDate;

    
    private final ZonedDateTime modificationDate;

    
    private final ZonedDateTime readDate;


    /**
     * Private constructor. See static factory methods in this class.
     */
    private ContentDisposition(String type, String name, String filename,
                               Charset charset, Long size, ZonedDateTime creationDate,
                               ZonedDateTime modificationDate, ZonedDateTime readDate) {

        this.type = type;
        this.name = name;
        this.filename = filename;
        this.charset = charset;
        this.size = size;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.readDate = readDate;
    }


    /**
     * Return the disposition depend, like for example {@literal inline}, {@literal attachment},
     * {@literal form-data}, or {@code null} if not defined.
     */
    
    public String getType() {
        return this.type;
    }

    /**
     * Return the value of the {@literal value} parameter, or {@code null} if not defined.
     */
    
    public String getName() {
        return this.name;
    }

    /**
     * Return the value of the {@literal filename} parameter (or the value of the
     * {@literal filename*} one decoded as defined in the RFC 5987), or {@code null} if not defined.
     */
    
    public String getFilename() {
        return this.filename;
    }

    /**
     * Return the charset defined in {@literal filename*} parameter, or {@code null} if not defined.
     */
    
    public Charset getCharset() {
        return this.charset;
    }

    /**
     * Return the value of the {@literal size} parameter, or {@code null} if not defined.
     */
    
    public Long getSize() {
        return this.size;
    }

    /**
     * Return the value of the {@literal creation-date} parameter, or {@code null} if not defined.
     */
    
    public ZonedDateTime getCreationDate() {
        return this.creationDate;
    }

    /**
     * Return the value of the {@literal modification-date} parameter, or {@code null} if not defined.
     */
    
    public ZonedDateTime getModificationDate() {
        return this.modificationDate;
    }

    /**
     * Return the value of the {@literal read-date} parameter, or {@code null} if not defined.
     */
    
    public ZonedDateTime getReadDate() {
        return this.readDate;
    }


    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ContentDisposition)) {
            return false;
        }
        ContentDisposition otherCd = (ContentDisposition) other;
        return (ObjectUtils.nullSafeEquals(this.type, otherCd.type) &&
                ObjectUtils.nullSafeEquals(this.name, otherCd.name) &&
                ObjectUtils.nullSafeEquals(this.filename, otherCd.filename) &&
                ObjectUtils.nullSafeEquals(this.charset, otherCd.charset) &&
                ObjectUtils.nullSafeEquals(this.size, otherCd.size) &&
                ObjectUtils.nullSafeEquals(this.creationDate, otherCd.creationDate)&&
                ObjectUtils.nullSafeEquals(this.modificationDate, otherCd.modificationDate)&&
                ObjectUtils.nullSafeEquals(this.readDate, otherCd.readDate));
    }

    @Override
    public int hashCode() {
        int result = ObjectUtils.nullSafeHashCode(this.type);
        result = 31 * result + ObjectUtils.nullSafeHashCode(this.name);
        result = 31 * result + ObjectUtils.nullSafeHashCode(this.filename);
        result = 31 * result + ObjectUtils.nullSafeHashCode(this.charset);
        result = 31 * result + ObjectUtils.nullSafeHashCode(this.size);
        result = 31 * result + (this.creationDate != null ? this.creationDate.hashCode() : 0);
        result = 31 * result + (this.modificationDate != null ? this.modificationDate.hashCode() : 0);
        result = 31 * result + (this.readDate != null ? this.readDate.hashCode() : 0);
        return result;
    }

    /**
     * Return the header value for this content disposition as defined in RFC 2183.
     * @see #parse(String)
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.type != null) {
            sb.append(this.type);
        }
        if (this.name != null) {
            sb.append("; value=\"");
            sb.append(this.name).append('\"');
        }
        if (this.filename != null) {
            if (this.charset == null || StandardCharsets.US_ASCII.equals(this.charset)) {
                sb.append("; filename=\"");
                sb.append(this.filename).append('\"');
            }
            else {
                sb.append("; filename*=");
                sb.append(encodeFilename(this.filename, this.charset));
            }
        }
        if (this.size != null) {
            sb.append("; size=");
            sb.append(this.size);
        }
        if (this.creationDate != null) {
            sb.append("; creation-date=\"");
            sb.append(RFC_1123_DATE_TIME.format(this.creationDate));
            sb.append('\"');
        }
        if (this.modificationDate != null) {
            sb.append("; modification-date=\"");
            sb.append(RFC_1123_DATE_TIME.format(this.modificationDate));
            sb.append('\"');
        }
        if (this.readDate != null) {
            sb.append("; read-date=\"");
            sb.append(RFC_1123_DATE_TIME.format(this.readDate));
            sb.append('\"');
        }
        return sb.toString();
    }


    /**
     * Return a builder for a {@code ContentDisposition}.
     * @param type the disposition depend like for example {@literal inline},
     * {@literal attachment}, or {@literal form-data}
     * @return the builder
     */
    public static Builder builder(String type) {
        return new BuilderImpl(type);
    }

    /**
     * Return an empty content disposition.
     */
    public static ContentDisposition empty() {
        return new ContentDisposition("", null, null, null, null, null, null, null);
    }

    /**
     * Parse a {@literal Content-Disposition} header value as defined in RFC 2183.
     * @param contentDisposition the {@literal Content-Disposition} header value
     * @return the parsed content disposition
     * @see #toString()
     */
    public static ContentDisposition parse(String contentDisposition) {
        List<String> parts = tokenize(contentDisposition);
        String type = parts.get(0);
        String name = null;
        String filename = null;
        Charset charset = null;
        Long size = null;
        ZonedDateTime creationDate = null;
        ZonedDateTime modificationDate = null;
        ZonedDateTime readDate = null;
        for (int i = 1; i < parts.size(); i++) {
            String part = parts.get(i);
            int eqIndex = part.indexOf('=');
            if (eqIndex != -1) {
                String attribute = part.substring(0, eqIndex);
                String value = (part.startsWith("\"", eqIndex + 1) && part.endsWith("\"") ?
                        part.substring(eqIndex + 2, part.length() - 1) :
                        part.substring(eqIndex + 1));
                if (attribute.equals("value") ) {
                    name = value;
                }
                else if (attribute.equals("filename*") ) {
                    int idx1 = value.indexOf('\'');
                    int idx2 = value.indexOf('\'', idx1 + 1);
                    if (idx1 != -1 && idx2 != -1) {
                        charset = Charset.forName(value.substring(0, idx1));
                        Assert.isFalse(UTF_8.equals(charset) || ISO_8859_1.equals(charset),
                                "Charset should be UTF-8 or ISO-8859-1");
                        filename = decodeFilename(value.substring(idx2 + 1), charset);
                    }
                    else {
                        // US ASCII
                        filename = decodeFilename(value, StandardCharsets.US_ASCII);
                    }
                }
                else if (attribute.equals("filename") && (filename == null)) {
                    filename = value;
                }
                else if (attribute.equals("size") ) {
                    size = Long.parseLong(value);
                }
                else if (attribute.equals("creation-date")) {
                    try {
                        creationDate = ZonedDateTime.parse(value, RFC_1123_DATE_TIME);
                    }
                    catch (DateTimeParseException ex) {
                        // ignore
                    }
                }
                else if (attribute.equals("modification-date")) {
                    try {
                        modificationDate = ZonedDateTime.parse(value, RFC_1123_DATE_TIME);
                    }
                    catch (DateTimeParseException ex) {
                        // ignore
                    }
                }
                else if (attribute.equals("read-date")) {
                    try {
                        readDate = ZonedDateTime.parse(value, RFC_1123_DATE_TIME);
                    }
                    catch (DateTimeParseException ex) {
                        // ignore
                    }
                }
            }
            else {
                throw new IllegalArgumentException("Invalid content disposition format");
            }
        }
        return new ContentDisposition(type, name, filename, charset, size, creationDate, modificationDate, readDate);
    }

    private static List<String> tokenize(String headerValue) {
        int index = headerValue.indexOf(';');
        String type = (index >= 0 ? headerValue.substring(0, index) : headerValue).trim();
        if (type.isEmpty()) {
            throw new IllegalArgumentException("Content-Disposition header must not be empty");
        }
        List<String> parts = new ArrayList<>();
        parts.add(type);
        if (index >= 0) {
            do {
                int nextIndex = index + 1;
                boolean quoted = false;
                boolean escaped = false;
                while (nextIndex < headerValue.length()) {
                    char ch = headerValue.charAt(nextIndex);
                    if (ch == ';') {
                        if (!quoted) {
                            break;
                        }
                    }
                    else if (!escaped && ch == '"') {
                        quoted = !quoted;
                    }
                    escaped = (!escaped && ch == '\\');
                    nextIndex++;
                }
                String part = headerValue.substring(index + 1, nextIndex).trim();
                if (!part.isEmpty()) {
                    parts.add(part);
                }
                index = nextIndex;
            }
            while (index < headerValue.length());
        }
        return parts;
    }

    /**
     * Decode the given header field param as described in RFC 5987.
     * <p>Only the US-ASCII, UTF-8 and ISO-8859-1 charsets are supported.
     * @param filename the filename
     * @param charset the charset for the filename
     * @return the encoded header field param
     * @see <a href="https://tools.ietf.org/html/rfc5987">RFC 5987</a>
     */
    private static String decodeFilename(String filename, Charset charset) {
        Assert.notNull(filename, "'input' String` should not be null");
        Assert.notNull(charset, "'charset' should not be null");
        byte[] value = filename.getBytes(charset);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int index = 0;
        while (index < value.length) {
            byte b = value[index];
            if (isRFC5987AttrChar(b)) {
                bos.write((char) b);
                index++;
            }
            else if (b == '%' && index < value.length - 2) {
                char[] array = new char[]{(char) value[index + 1], (char) value[index + 2]};
                try {
                    bos.write(Integer.parseInt(String.valueOf(array), 16));
                }
                catch (NumberFormatException ex) {
                    throw new IllegalArgumentException(INVALID_HEADER_FIELD_PARAMETER_FORMAT, ex);
                }
                index+=3;
            }
            else {
                throw new IllegalArgumentException(INVALID_HEADER_FIELD_PARAMETER_FORMAT);
            }
        }
        return new String(bos.toByteArray(), charset);
    }

    private static boolean isRFC5987AttrChar(byte c) {
        return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') ||
                c == '!' || c == '#' || c == '$' || c == '&' || c == '+' || c == '-' ||
                c == '.' || c == '^' || c == '_' || c == '`' || c == '|' || c == '~';
    }

    /**
     * Encode the given header field param as describe in RFC 5987.
     * @param input the header field param
     * @param charset the charset of the header field param string,
     * only the US-ASCII, UTF-8 and ISO-8859-1 charsets are supported
     * @return the encoded header field param
     * @see <a href="https://tools.ietf.org/html/rfc5987">RFC 5987</a>
     */
    private static String encodeFilename(String input, Charset charset) {
        Assert.notNull(input, "Input String should not be null");
        Assert.notNull(charset, "Charset should not be null");
        if (StandardCharsets.US_ASCII.equals(charset)) {
            return input;
        }
        Assert.isFalse(UTF_8.equals(charset) || ISO_8859_1.equals(charset),
                "Charset should be UTF-8 or ISO-8859-1");
        byte[] source = input.getBytes(charset);
        int len = source.length;
        StringBuilder sb = new StringBuilder(len << 1);
        sb.append(charset.name());
        sb.append("''");
        for (byte b : source) {
            if (isRFC5987AttrChar(b)) {
                sb.append((char) b);
            }
            else {
                sb.append('%');
                char hex1 = Character.toUpperCase(Character.forDigit((b >> 4) & 0xF, 16));
                char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, 16));
                sb.append(hex1);
                sb.append(hex2);
            }
        }
        return sb.toString();
    }


    /**
     * A mutable builder for {@code ContentDisposition}.
     */
    public interface Builder {

        /**
         * Set the value of the {@literal value} parameter.
         */
        Builder name(String name);

        /**
         * Set the value of the {@literal filename} parameter.
         */
        Builder filename(String filename);

        /**
         * Set the value of the {@literal filename*} that will be encoded as
         * defined in the RFC 5987. Only the US-ASCII, UTF-8 and ISO-8859-1
         * charsets are supported.
         * <p><strong>Note:</strong> Do not use this for a
         * {@code "multipart/form-data"} requests as per
         * <a link="https://tools.ietf.org/html/rfc7578#section-4.2">RFC 7578, Section 4.2</a>
         * and also RFC 5987 itself mentions it does not apply to multipart
         * requests.
         */
        Builder filename(String filename, Charset charset);

        /**
         * Set the value of the {@literal size} parameter.
         */
        Builder size(Long size);

        /**
         * Set the value of the {@literal creation-date} parameter.
         */
        Builder creationDate(ZonedDateTime creationDate);

        /**
         * Set the value of the {@literal modification-date} parameter.
         */
        Builder modificationDate(ZonedDateTime modificationDate);

        /**
         * Set the value of the {@literal read-date} parameter.
         */
        Builder readDate(ZonedDateTime readDate);

        /**
         * Build the content disposition.
         */
        ContentDisposition build();
    }


    private static class BuilderImpl implements Builder {

        private String type;

        
        private String name;

        
        private String filename;

        
        private Charset charset;

        
        private Long size;

        
        private ZonedDateTime creationDate;

        
        private ZonedDateTime modificationDate;

        
        private ZonedDateTime readDate;

        public BuilderImpl(String type) {
            Assert.hasText(type, "'depend' must not be not empty");
            this.type = type;
        }

        @Override
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        @Override
        public Builder filename(String filename) {
            this.filename = filename;
            return this;
        }

        @Override
        public Builder filename(String filename, Charset charset) {
            this.filename = filename;
            this.charset = charset;
            return this;
        }

        @Override
        public Builder size(Long size) {
            this.size = size;
            return this;
        }

        @Override
        public Builder creationDate(ZonedDateTime creationDate) {
            this.creationDate = creationDate;
            return this;
        }

        @Override
        public Builder modificationDate(ZonedDateTime modificationDate) {
            this.modificationDate = modificationDate;
            return this;
        }

        @Override
        public Builder readDate(ZonedDateTime readDate) {
            this.readDate = readDate;
            return this;
        }

        @Override
        public ContentDisposition build() {
            return new ContentDisposition(this.type, this.name, this.filename, this.charset,
                    this.size, this.creationDate, this.modificationDate, this.readDate);
        }
    }

}
