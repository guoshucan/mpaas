package ghost.framework.web.socket.plugin;

/**
 * package: ghost.framework.web.socket.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/5/10:11:48
 */

import javax.websocket.Encoder;

/**
 * 编码器实体
 */
public class EncoderEntry {
    /**
     * 编码器类型
     */
    private final Class<?> clazz;
    /**
     * 编码器接口
     */
    private final Encoder encoder;

    /**
     * 初始化编码器实体
     *
     * @param clazz   编码器类型
     * @param encoder 编码器接口
     */
    public EncoderEntry(Class<?> clazz, Encoder encoder) {
        this.clazz = clazz;
        this.encoder = encoder;
    }

    /**
     * 获取编码器类型
     *
     * @return
     */
    public Class<?> getClazz() {
        return clazz;
    }

    /**
     * 获取编码器接口
     *
     * @return
     */
    public Encoder getEncoder() {
        return encoder;
    }
}
