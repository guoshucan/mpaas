package ghost.framework.util;
import java.util.UUID;

/**
 * 格式化验证。
 */
public final class UUIDFormatValidator {
//    /**
//     * 格式横杠。
//     *
//     * @param uuid
//     * @return
//     */
//    private static String in(String uuid) {
//        if (uuid.contains("-")) return uuid;
//        StringBuilder sb = new StringBuilder(uuid);
//        sb.insert(8, "-");
//        sb.insert(13, "-");
//        sb.insert(18, "-");
//        sb.insert(23, "-");
//        return sb.toString();
//    }

    /**
     * 验证资源主键Id。
     *
     * @param uuid
     * @return
     */
    public static boolean isValidMediaId(String uuid) {
        try {
            StringBuilder sb = new StringBuilder(uuid);
            sb.insert(8, "-");
            sb.insert(13, "-");
            sb.insert(18, "-");
            sb.insert(23, "-");
            //
            sb.insert(8, "-");
            sb.insert(13, "-");
            sb.insert(18, "-");
            sb.insert(23, "-");

            String mediaId = sb.toString();
//            UUID.fromString(in(parameter, 0));
//            UUID.fromString(in(parameter, 0));
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    /**
     * 验证主键Id。
     *
     * @param uuid
     * @return
     */
    public static boolean isValidUUID(String uuid) {
        try {
            UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}
