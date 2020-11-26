package ghost.framework.jsr303.valid.plugin;

/**
 * package: ghost.framework.jsr303.valid.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/26:10:11
 */
public final class ValidationUtils {
//    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
//
//    public static <T> ValidationResult validateEntity(T obj) {
//        ValidationResult result = new ValidationResult();
//        Set<ConstraintViolation<T>> set = validator.validate(obj, Default.class);
//        // if( CollectionUtils.isNotEmpty(set) ){
//        if (set != null && set.size() != 0) {
//            result.setHasErrors(true);
//            Map<String, String> errorMsg = new HashMap<String, String>();
//            for (ConstraintViolation<T> cv : set) {
//                errorMsg.put(cv.getPropertyPath().toString(), cv.getMessage());
//            }
//            result.setErrorMsg(errorMsg);
//        }
//        return result;
//    }
//
//    public static <T> ValidationResult validateProperty(T obj, String propertyName) {
//        ValidationResult result = new ValidationResult();
//        Set<ConstraintViolation<T>> set = validator.validateProperty(obj, propertyName, Default.class);
//        if (set != null && set.size() != 0) {
//            result.setHasErrors(true);
//            Map<String, String> errorMsg = new HashMap<String, String>();
//            for (ConstraintViolation<T> cv : set) {
//                errorMsg.put(propertyName, cv.getMessage());
//            }
//            result.setErrorMsg(errorMsg);
//        }
//        return result;
//    }
}
