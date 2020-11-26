package ghost.framework.context.bean.exception;

import ghost.framework.beans.BeanException;

import java.lang.reflect.Field;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:绑定无效错误
 * @Date: 21:33 2019/12/1
 */
public class BeanNotException extends BeanException {
    private static final long serialVersionUID = 8042230553587736332L;
    private Field field;
    public BeanNotException(Field field){
        this.field = field;
    }

    @Override
    public String getMessage() {
        return field.toString();
    }
}
