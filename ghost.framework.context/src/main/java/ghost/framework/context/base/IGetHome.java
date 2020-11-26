package ghost.framework.context.base;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * package: ghost.framework.context.base
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/20:18:59
 */
public interface IGetHome {
    ApplicationHome getHome();
    /**
     * 获取应用包指定文件url路径
     *
     * @param filePath 文件路径
     * @return
     * @throws MalformedURLException
     */
    URL getUrlPath(String filePath) throws MalformedURLException;

    /**
     * 获取应用包url路径
     *
     * @return
     * @throws MalformedURLException
     */
    URL getUrlPath() throws MalformedURLException;
}
