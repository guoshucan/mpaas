package ghost.framework.ssh;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import com.jcraft.jsch.JSchException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;

/**
 * package: ghost.framework.ssh.ganymed
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/5/25:20:28
 */
public class GanymedUtil {
    private static Log log = LogFactory.getLog(GanymedUtil.class);

    public static void main(String[] args) throws IOException, JSchException {
        // TODO Auto-generated method stub

//        String host = "www.xxx.com";
//        int port = 12;
        //内网
        String host = "192.168.1.12";
        int port = 22;

        String user = "root";
        String password = "@@Gsc19800313@@";
        String command = "java -version";
        Connection connection = getConn(host, port, user, password);
        String res = execute(connection, command);
        System.out.println(res);
    }

    private static String DEFAULTCHART = "UTF-8";

    /**
     * @param url      主机Ip
     * @param port     主机端口
     * @param user     用户名
     * @param password 密码
     * @return ch.ethz.ssh2.Connection
     * @Author r
     * @Description 连接主机
     */
    public static Connection getConn(String url, int port, String user, String password) {
        Connection conn = new Connection(url, port);
        try {
            conn.connect();
            //判断是否含有用户名与密码
            if ((!StringUtils.isEmpty(user)) && (!StringUtils.isEmpty(password))) {
                boolean isAuthenticated = conn.authenticateWithPassword(user, password);
                if (!isAuthenticated) {
                    throw new IOException("Authentication failed");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 执行shll脚本或者命令
     *
     * @param shell 命令(多条命令以；隔开)
     * @return 结果
     */
    public static String execute(Connection conn, String shell) {
        String result = "";
        try {
            if (conn != null) {
                Session session = conn.openSession();//打开一个会话
                session.execCommand(shell);//执行命令
                result = processStdout(session.getStdout(), DEFAULTCHART);
                //如果为得到标准输出为空，说明脚本执行出错了
                if (StringUtils.isBlank(result)) {
                    result = processStdout(session.getStderr(), DEFAULTCHART);
                }
                conn.close();
                session.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 解析脚本执行返回的结果集
     *
     * @param in      输入流对象
     * @param charset 编码
     * @return 以纯文本的格式返回
     */
    private static String processStdout(InputStream in, String charset) {
        InputStream stdout = new StreamGobbler(in);
        StringBuffer buffer = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout, charset));
            String line;
            while ((line = br.readLine()) != null) {
                buffer.append(line + "\n");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
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
        Connection connection = null;
        Session session = null;
        try {
            connection = getConn(host, port, user, password);
            session = connection.openSession();//打开一个会话
            return true;
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                e.printStackTrace();
                log.error(e.getMessage(), e);
            }
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (Exception ex) {
                    if (log.isDebugEnabled()) {
                        ex.printStackTrace();
                        log.error(ex.getMessage(), ex);
                    }
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception ex) {
                    if (log.isDebugEnabled()) {
                        ex.printStackTrace();
                        log.error(ex.getMessage(), ex);
                    }
                }
            }
        }
        return false;
    }
}
