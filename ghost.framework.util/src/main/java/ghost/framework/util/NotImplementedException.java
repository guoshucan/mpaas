package ghost.framework.util;

/**
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:函数未定义工作内容错误
 * @Date: 10:32 2019/11/21
 */
public class NotImplementedException extends Exception {
    private static final long serialVersionUID = -4188389465432825198L;

    /**
     * 函数未定义工作内容错误
     */
    public NotImplementedException() {
        super(Thread.currentThread().getStackTrace()[2].getClassName() + "." + Thread.currentThread().getStackTrace()[2].getMethodName());
    }
}