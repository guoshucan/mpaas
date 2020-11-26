package ghost.framework.util;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 23:55 2018-08-05
 */
public final class ConverterUtil {
    public static byte[] intToBytes(int v) {
        byte[] byteNum = new byte[4];
        for (int ix = 0; ix < 4; ++ix) {
            int offset = 32 - (ix + 1) * 8;
            byteNum[ix] = (byte) ((v >> offset) & 0xff);
        }
        return byteNum;
    }

    public static int bytesToInt(byte[] bytes) {
        int num = 0;
        for (int ix = 0; ix < 4; ++ix) {
            num <<= 8;
            num |= (bytes[ix] & 0xff);
        }
        return num;
    }

    public static byte intToOneByte(int v) {
        return (byte) (v & 0x000000ff);
    }

    public static int oneByteToInt(byte b) {
        //针对正数的int
        return b > 0 ? b : (128 + (128 + b));
    }

    public static byte[] longToBytes(long v) {
        byte[] byteNum = new byte[8];
        for (int ix = 0; ix < 8; ++ix) {
            int offset = 64 - (ix + 1) * 8;
            byteNum[ix] = (byte) ((v >> offset) & 0xff);
        }
        return byteNum;
    }

    public static long bytesToLong(byte[] bytes) {
        long num = 0;
        for (int ix = 0; ix < 8; ++ix) {
            num <<= 8;
            num |= (bytes[ix] & 0xff);
        }
        return num;
    }
}
