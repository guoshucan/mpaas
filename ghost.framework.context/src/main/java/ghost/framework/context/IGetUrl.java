package ghost.framework.context;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * package: ghost.framework.core
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:获取url接口
 * @Date: 2019/12/12:22:31
 */
public interface IGetUrl {
    /**
     * 获取url
     * @return
     * @throws MalformedURLException
     */
    URL getUrl() throws MalformedURLException;
}
