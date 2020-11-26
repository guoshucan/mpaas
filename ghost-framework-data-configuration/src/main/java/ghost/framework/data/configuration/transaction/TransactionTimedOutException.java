package ghost.framework.data.configuration.transaction;

/**
 * package: ghost.framework.data.configuration.transaction
 *
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2019-11-17:1:32
 */
public class TransactionTimedOutException extends TransactionException{
    private static final long serialVersionUID = -7994886400325675758L;
    /**
     * Constructor for TransactionTimedOutException.
     * @param msg the detail message
     */
    public TransactionTimedOutException(String msg) {
        super(msg);
    }

    /**
     * Constructor for TransactionTimedOutException.
     * @param msg the detail message
     * @param cause the root cause from the transaction API in use
     */
    public TransactionTimedOutException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
