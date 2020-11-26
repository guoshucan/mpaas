package ghost.framework.core.converter.serialization;

import ghost.framework.context.converter.serialization.SerializationException;
import ghost.framework.util.Assert;

import java.io.*;

/**
 * package: ghost.framework.util
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:序列化工具类
 * @Date: 2020/6/13:10:21
 */
public final class SerializeUtils {
    /**
     * 序列化
     * @param value
     * @return
     * @throws SerializationException
     */
    public static byte[] serialize(Object value) throws SerializationException {
        if (value == null) {
            Assert.notNull(value, "null value error");
        }
        byte[] result = null;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream os = null;
        try {
            bos = new ByteArrayOutputStream();
            os = new ObjectOutputStream(bos);
            os.writeObject(value);
            os.close();
            bos.close();
            result = bos.toByteArray();
        } catch (IOException e) {
            throw new SerializationException(e.getMessage(), e);
        } finally {
            close(os);
            close(bos);
        }
        return result;
    }

    /**
     * 反序列化
     * @param in
     * @return
     * @throws SerializationException
     */
    public static Object deserialize(byte[] in) throws SerializationException {
        Object result = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream is = null;
        try {
            if (in != null) {
                bis = new ByteArrayInputStream(in);
                is = new ObjectInputStream(bis);
                result = is.readObject();
                is.close();
                bis.close();
            }
        } catch (IOException e) {
            throw new SerializationException(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            throw new SerializationException(e.getMessage(), e);
        } finally {
            close(is);
            close(bis);
        }
        return result;
    }

    static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }
}
