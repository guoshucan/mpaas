package ghost.framework.web.angular1x.ssh.plugin.ws;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * package: ghost.framework.web.angular1x.ssh.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/8/30:19:03
 */
public class SshSocketEncoder implements Encoder.Text<String> {
    @Override
    public String encode(String object) throws EncodeException {
        return object;
    }

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }
}
