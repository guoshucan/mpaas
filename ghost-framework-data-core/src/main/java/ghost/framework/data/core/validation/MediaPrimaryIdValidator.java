package ghost.framework.data.core.validation;

import ghost.framework.util.UUIDFormatValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.xml.bind.ValidationException;

/**
 * 验证主键Id。
 */
public class MediaPrimaryIdValidator implements ConstraintValidator<MediaPrimaryId, CharSequence> {
    public MediaPrimaryIdValidator() {
    }

    private String message;

    public void initialize(MediaPrimaryId parameters) {
        this.message = parameters.message();
    }


    public boolean isValid(CharSequence str, ConstraintValidatorContext constraintValidatorContext) {
        if (str == null) return true;
        if (str.length() == 0) return true;
        if (!UUIDFormatValidator.isValidMediaId(str.toString())) {
            try {
                throw new ValidationException(this.message == null ? "主键字符格式错误！" : this.message);
            } catch (ValidationException ex) {
                ex.printStackTrace();
            }
            return false;
        }
        return true;
    }
}
