package ghost.framework.module.io.support;

import java.io.IOException;
import java.io.InputStream;

/**
 * package: ghost.framework.module.io.support
 *
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2019-11-15:1:34
 */
public interface InputStreamSource {
    InputStream getInputStream() throws IOException;
}
