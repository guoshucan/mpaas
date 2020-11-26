package ghost.framework.context;

/**
 * package: ghost.framework.context
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:获取域接口
 * @Date: 2020/2/20:21:31
 */
public interface IGetDomain {
    /**
     * 获取域对象
     *
     * @return
     */
    default Object getDomain() {
        throw new UnsupportedOperationException(IGetDomain.class.getName() + "#getDomain()");
    }
}