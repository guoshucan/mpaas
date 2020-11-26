package ghost.framework.module.reflect;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:获取对象Id接口。
 * @Date: 10:18 2018-07-01
 */
public interface IGetObjectId {
    /**
     * 获取对象Id。
     *
     * @return
     */
    default String getId() {
        return null;
    }
}
