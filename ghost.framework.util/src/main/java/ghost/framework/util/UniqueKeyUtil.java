package ghost.framework.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:唯一主键工具类
 * @Date: 15:47 2018-11-16
 */
public final class UniqueKeyUtil {
    /**
     * 获取随机生成UUID小写格式。
     *
     * @return
     */
    public static String createUuid() {
        return UUID.randomUUID().toString().toLowerCase();
    }

    /**
     * 创建long类型唯一键。
     * @return
     */
    public static long createLong() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyyMMddHH");
        String result = format.format(date);
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {//有可能是负数
            hashCodeV = -hashCodeV;
        }
        result += String.valueOf(hashCodeV);
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            result += random.nextInt(10);
        }
        result = result.substring(0, 18);
        return Long.parseLong(result);
    }
}
