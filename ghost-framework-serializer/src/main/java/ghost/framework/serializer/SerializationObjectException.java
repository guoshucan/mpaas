package ghost.framework.serializer;

/**
 * 序列化对象错误。
 */
public class SerializationObjectException extends Exception {
    /**
     * 初始化序列化对象错误。
     */
    public SerializationObjectException(){}

    /**
     * 初始化序列化对象错误。
     * @param message
     */
    public SerializationObjectException(String message){
        super(message);
    }

    /**
     * 初始化序列化对象错误。
     * @param message
     * @param cause
     */
    public SerializationObjectException(String message, Throwable cause) {
        super(message, cause);
    }
}