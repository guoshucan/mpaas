package ghost.framework.ssh;

/**
 * package: ghost.framework.ssh
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:SSH工具类型
 * @Date: 2020/5/25:20:39
 */
public final class SshUtil {
    /**
     * 设置ganymed测试连接
     *
     * @param host     主键地址
     * @param port     主机端口
     * @param user     登录账号
     * @param password 登录密码
     * @return 返回是否连接完成
     */
    public static boolean ganymedTestConnection(String host, int port, String user, String password) {
        return testConnection(SshMode.Ganymed, host, port, user, password);
    }

    /**
     * 使用jsch测试连接
     *
     * @param host     主键地址
     * @param port     主机端口
     * @param user     登录账号
     * @param password 登录密码
     * @return 返回是否连接完成
     */
    public static boolean jschTestConnection(String host, int port, String user, String password) {
        return testConnection(SshMode.Jsch, host, port, user, password);
    }

    /**
     * 测试连接
     *
     * @param mode     连接模式
     * @param host     主键地址
     * @param port     主机端口
     * @param user     登录账号
     * @param password 登录密码
     * @return 返回是否连接完成
     * @throws SshException
     */
    public static boolean testConnection(SshMode mode, String host, int port, String user, String password) throws SshException {
        if (mode == SshMode.Ganymed) {
            return GanymedUtil.testConnection(host, port, user, password);
        }
        if (mode == SshMode.Jsch) {
            return JschUtil.testConnection(host, port, user, password);
        }
        return false;
    }
}
