package ghost.framework.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CodeExpiredUtil {
    /**
     * 验证码过期。
     *
     * @param codeTime
     * @return
     * @throws ParseException
     */
    public static boolean expired(Object codeTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fromDate = sdf.format(codeTime);
        String toDate = sdf.format(new Date());
        long from = sdf.parse(fromDate).getTime();
        long to = sdf.parse(toDate).getTime();
        int minutes = (int) ((to - from)/1000);
        System.out.println(minutes);
        return minutes > 120;
    }
}