package ghost.framework.data.core;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:数据库错误处理接口
 * @Date: 20:24 2019-02-06
 */
public interface IDataBaseError {
    /**
     * 数据库操作错误
     * @param objects
     * @param e
     * @throws Exception
     */
    void error(Object[] objects, Exception e) throws Exception;
}
