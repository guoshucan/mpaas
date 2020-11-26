package ghost.framework.context.module.exception;

import java.lang.reflect.Field;

/**
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块无效错误
 * @Date: 22:24 2019/11/5
 */
public class ModuleInvalidException extends ModuleException {
    private static final long serialVersionUID = 1423050744055160377L;

    /**
     * 初始化模块无效错误
     *
     * @param message 错误消息
     */
    public ModuleInvalidException(String message) {
        super(message);
    }

    private Field field;

    public ModuleInvalidException(Field field) {
        this.field = field;
    }
}