package ghost.framework.web.socket.test.plugin;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * package: ghost.framework.web.socket.test.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/5/9:13:59
 */
public class EncoderTest implements Encoder.Text<String> {
    @Override
    public String encode(String object) throws EncodeException {
        return "==" + object + "==";
    }

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }
}
