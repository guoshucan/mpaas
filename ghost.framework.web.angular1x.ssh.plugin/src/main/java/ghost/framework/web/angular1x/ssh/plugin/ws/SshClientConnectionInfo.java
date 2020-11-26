package ghost.framework.web.angular1x.ssh.plugin.ws;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;

import javax.websocket.Session;

/**
 * package: ghost.framework.web.angular1x.ssh.plugin.ws
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:ssh客户端连接信息
 * @Date: 2020/8/31:21:00
 */
final class SshClientConnectionInfo {
    public SshClientConnectionInfo(SshSocket socket, Session session) {
        this.socket = socket;
        this.session = session;
    }
    private Session session;

    public Session getSession() {
        return session;
    }

    private JSch jSch;
    private Channel channel;

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public JSch getjSch() {
        return jSch;
    }

    public void setjSch(JSch jSch) {
        this.jSch = jSch;
    }

    public Channel getChannel() {
        return channel;
    }

    private SshSocket socket;

    public SshSocket getSocket() {
        return socket;
    }
}
