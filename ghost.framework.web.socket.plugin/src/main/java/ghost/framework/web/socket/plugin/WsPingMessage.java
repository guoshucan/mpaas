package ghost.framework.web.socket.plugin;

import java.nio.ByteBuffer;

/**
 * package: ghost.framework.web.socket.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/9/2:22:36
 */
public class WsPingMessage implements PingMessage {
    private final ByteBuffer applicationData;


    public WsPingMessage(ByteBuffer applicationData) {
        byte[] dst = new byte[applicationData.limit()];
        applicationData.get(dst);
        this.applicationData = ByteBuffer.wrap(dst);
    }
    @Override
    public ByteBuffer getApplicationData() {
        return applicationData;
    }
}
