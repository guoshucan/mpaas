package ghost.framework.data.configuration.dao;

/**
 * package: ghost.framework.data.configuration.dao
 *
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2019-11-17:1:43
 */
public class IncorrectResultSizeDataAccessException extends DataRetrievalFailureException{
    private static final long serialVersionUID = -6271902095770839962L;

    private final int expectedSize;

    private final int actualSize;


    /**
     * Constructor for IncorrectResultSizeDataAccessException.
     * @param expectedSize the expected result size
     */
    public IncorrectResultSizeDataAccessException(int expectedSize) {
        super("Incorrect result size: expected " + expectedSize);
        this.expectedSize = expectedSize;
        this.actualSize = -1;
    }

    /**
     * Constructor for IncorrectResultSizeDataAccessException.
     * @param expectedSize the expected result size
     * @param actualSize the actual result size (or -1 if unknown)
     */
    public IncorrectResultSizeDataAccessException(int expectedSize, int actualSize) {
        super("Incorrect result size: expected " + expectedSize + ", actual " + actualSize);
        this.expectedSize = expectedSize;
        this.actualSize = actualSize;
    }

    /**
     * Constructor for IncorrectResultSizeDataAccessException.
     * @param msg the detail message
     * @param expectedSize the expected result size
     */
    public IncorrectResultSizeDataAccessException(String msg, int expectedSize) {
        super(msg);
        this.expectedSize = expectedSize;
        this.actualSize = -1;
    }

    /**
     * Constructor for IncorrectResultSizeDataAccessException.
     * @param msg the detail message
     * @param expectedSize the expected result size
     * @param ex the wrapped exception
     */
    public IncorrectResultSizeDataAccessException(String msg, int expectedSize, Throwable ex) {
        super(msg, ex);
        this.expectedSize = expectedSize;
        this.actualSize = -1;
    }

    /**
     * Constructor for IncorrectResultSizeDataAccessException.
     * @param msg the detail message
     * @param expectedSize the expected result size
     * @param actualSize the actual result size (or -1 if unknown)
     */
    public IncorrectResultSizeDataAccessException(String msg, int expectedSize, int actualSize) {
        super(msg);
        this.expectedSize = expectedSize;
        this.actualSize = actualSize;
    }

    /**
     * Constructor for IncorrectResultSizeDataAccessException.
     * @param msg the detail message
     * @param expectedSize the expected result size
     * @param actualSize the actual result size (or -1 if unknown)
     * @param ex the wrapped exception
     */
    public IncorrectResultSizeDataAccessException(String msg, int expectedSize, int actualSize, Throwable ex) {
        super(msg, ex);
        this.expectedSize = expectedSize;
        this.actualSize = actualSize;
    }


    /**
     * Return the expected result size.
     */
    public int getExpectedSize() {
        return this.expectedSize;
    }

    /**
     * Return the actual result size (or -1 if unknown).
     */
    public int getActualSize() {
        return this.actualSize;
    }
}
