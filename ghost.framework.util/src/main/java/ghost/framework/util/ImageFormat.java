package ghost.framework.util;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:图片类型枚举。
 * @Date: 21:05 2018-11-18
 */
public enum ImageFormat {
    png,
    jpg,
    jpeg,
    gif,
    bmp,
    ico;
    public boolean contains(String _name) {
        ImageFormat[] es = values();
        for (ImageFormat e : es) {
            if (e.name().equals(_name)) {
                return true;
            }
        }
        return false;
    }
}
