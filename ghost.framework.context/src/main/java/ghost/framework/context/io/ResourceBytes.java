package ghost.framework.context.io;

import java.io.IOException;

/**
 * package: ghost.framework.context.io
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:资源文件二进制接口
 * @Date: 2020/3/15:1:27
 */
public interface ResourceBytes {
    /**
     * 获取二进制
     * @return
     */
    default byte[] getBytes(){
        throw new UnsupportedOperationException(ResourceBytes.class.getName() + "#getBytes");
    }
    /**
     * 获取文件修改时间
     * @throws IOException
     * @return
     */
    long lastModified() throws IOException;
}
