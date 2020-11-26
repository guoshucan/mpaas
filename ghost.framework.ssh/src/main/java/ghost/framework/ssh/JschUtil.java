package ghost.framework.ssh;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.IOException;
import java.io.InputStream;

/**
 * package: ghost.framework.ssh.jsch
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/5/25:20:22
 */
public class JschUtil {
    private static Log log = LogFactory.getLog(JschUtil.class);
    public static void main(String[] args) throws IOException, JSchException {
        // TODO Auto-generated method stub
//        String host = "www.xxx.com";
//        int port = 12;
        //        String host = "www.xxx.com";
//        int port = 12;
        //内网
        String host = "192.168.1.51";
        int port = 22;
        String user = "root";
        String password = "123456";
        String command = "java -version";
        String res = exeCommand(host, port, user, password, command);
        System.out.println(res);
    }

    public static String exeCommand(String host, int port, String user, String password, String command) throws JSchException, IOException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(user, host, port);
        session.setConfig("StrictHostKeyChecking", "no");
        //    java.util.Properties config = new java.util.Properties();
        //   config.put("StrictHostKeyChecking", "no");
        session.setPassword(password);
        session.connect();

        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        InputStream in = channelExec.getInputStream();
        channelExec.setCommand(command);
        channelExec.setErrStream(System.err);
        channelExec.connect();
        String out = IOUtils.toString(in, "UTF-8");
        channelExec.disconnect();
        session.disconnect();
        return out;
    }

    /**
     * 测试连接
     *
     * @param host     主键地址
     * @param port     主机端口
     * @param user     登录账号
     * @param password 登录密码
     * @return 返回是否连接完成
     */
    public static boolean testConnection(String host, int port, String user, String password) {
        JSch jsch = new JSch();
        Session session = null;
        try {
            session = jsch.getSession(user, host, port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(password);
            session.connect();
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                e.printStackTrace();
                log.error(e.getMessage(), e);
            }
        } finally {
            try {
                session.disconnect();
            } catch (Exception ex) {
                if (log.isDebugEnabled()) {
                    ex.printStackTrace();
                    log.error(ex.getMessage(), ex);
                }
            }
        }
        return false;
    }
}
