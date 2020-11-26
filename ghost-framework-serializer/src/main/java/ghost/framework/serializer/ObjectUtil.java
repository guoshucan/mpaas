package ghost.framework.serializer;
import java.io.*;

/**
 * 对象转换攻击。
 */
public class ObjectUtil {
    /**
     * 对象转数组
     *
     * @param obj
     * @return
     * @throws IOException
     */
    public static byte[] toBytes(Object obj) throws IOException {
        ByteArrayOutputStream arrayOutputStream = null;
        ObjectOutputStream outputStream = null;
        try {
            arrayOutputStream = new ByteArrayOutputStream();
            outputStream = new ObjectOutputStream(arrayOutputStream);
            outputStream.writeObject(obj);
            outputStream.flush();
            return arrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (arrayOutputStream != null) arrayOutputStream.close();
            if (outputStream != null) outputStream.close();
        }
    }

    /**
     * 数组转对象返回泛型对象。
     * @param bytes
     * @param c
     * @param <T>
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static  <T> T toObject(byte[] bytes, Class<T> c) throws IOException, ClassNotFoundException
    {
        return c.cast(toObject(bytes));
    }
    /**
     * 数组转对象
     *
     * @param bytes
     * @return
     */
    public static Object toObject(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream arrayInputStream = null;
        ObjectInputStream inputStream = null;
        try {
            arrayInputStream = new ByteArrayInputStream(bytes);
            inputStream = new ObjectInputStream(arrayInputStream);
            return inputStream.readObject();
        } catch (IOException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            if (arrayInputStream != null) arrayInputStream.close();
            if (inputStream != null) inputStream.close();
        }
    }
}