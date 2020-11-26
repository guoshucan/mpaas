package ghost.framework.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @Description:二进制转换。
 * @Date: 18:52 2018/5/24
 */
public final class ByteUtil {
    /**
     * short 转 byte[]
     * 大端
     *
     * @param data
     * @return
     */
    public static byte[] shortToBytes(short data) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putShort(data);
        byte[] bytes = buffer.array();
        return bytes;
    }

    /**
     * chart 转 byte[]
     * 大端
     *
     * @param data
     * @return
     */
    public static byte[] charToBytes(char data) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putChar(data);
        byte[] bytes = buffer.array();
        return bytes;
    }

    /**
     * int 转 byte[]
     * 大端
     *
     * @param data
     * @return
     */
    public static byte[] intToBytes(int data) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putInt(data);
        byte[] bytes = buffer.array();
        return bytes;
    }

    /**
     * long 转 byte[]
     * 大端
     *
     * @param data
     * @return
     */
    public static byte[] longToBytes(long data) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putLong(data);
        byte[] bytes = buffer.array();
        return bytes;
    }

    /**
     * float 转 byte[]
     * 大端
     *
     * @param data
     * @return
     */
    public static byte[] floatToBytes(float data) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putFloat(data);
        byte[] bytes = buffer.array();
        return bytes;
    }

    /**
     * double 转 byte[]
     * 大端
     *
     * @param data
     * @return
     */
    public static byte[] doubleToBytes(double data) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putDouble(data);
        byte[] bytes = buffer.array();
        return bytes;
    }

    /**
     * byte[] 转short
     * 大端
     *
     * @param bytes
     * @return
     */
    public static short bytesToShort(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.put(bytes);
        short result = buffer.getShort(0);
        return result;
    }

    /**
     * byte[] 转 char
     * 大端
     *
     * @param bytes
     * @return
     */
    public static char bytesToChar(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.put(bytes);
        char result = buffer.getChar(0);
        return result;
    }

    /**
     * byte[] 转 int
     * 大端
     *
     * @param bytes
     * @return
     */
    public static int bytesToInt(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.put(bytes);
        int result = buffer.getInt(0);
        return result;
    }

    /**
     * byte[] 转 long
     *
     * @param bytes
     * @return
     */
    public static long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.put(bytes);
        long result = buffer.getLong(0);
        return result;
    }

    /**
     * byte[] 转 float
     *
     * @param bytes
     * @return
     */
    public static float bytesToFloat(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.put(bytes);
        float result = buffer.getFloat(0);
        return result;
    }

    /**
     * byte[] 转 double
     *
     * @param bytes
     * @return
     */
    public static double bytesToDouble(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.put(bytes);
        double result = buffer.getDouble(0);
        return result;
    }
}