package ghost.framework.util;

import org.apache.commons.logging.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @Description:
 * @Date: 23:12 2018/3/28
 */
public final class ExceptionUtil {
    public final static void debugOrError(Log log, Exception e) {
        e.printStackTrace();
        if (log.isDebugEnabled()) {
            log.debug(ExceptionUtil.outStackTrace(e));
        } else {
            log.error(ExceptionUtil.outStackTrace(e));
        }
    }

    public final static String outStackTrace(Exception e) {
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            //将出错的栈信息输出到printWriter中
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
        } finally {
            if (sw != null) {
                try {
                    sw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (pw != null) {
                pw.close();
            }
        }
        return sw.toString();
    }
}
