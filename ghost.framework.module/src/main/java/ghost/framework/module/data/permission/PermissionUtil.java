//package ghost.framework.module.data.permission;
//
//import javax.persistence.Column;
//import java.lang.reflect.Field;
//import java.util.HashMap;
//
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:权限工具类
// * @Date: 21:44 2018-09-16
// */
//public final class PermissionUtil {
//    /**
//     * 转换权限列表。
//     *
//     * @param permissionEntity 权限实体对象
//     */
//    public static void toMap(Object permissionEntity, HashMap<String, Boolean> map) throws IllegalArgumentException, IllegalAccessException {
//        for (Field field : permissionEntity.getClass().getDeclaredFields()) {
//            try {
//                //验证例对象。
//                if (field.isAnnotationPresent(Column.class) && field.getGenericType() == Boolean.TYPE) {
//                    field.setAccessible(true);
//                    map.put(field.getName(), field.getBoolean(permissionEntity));
//                    field.setAccessible(false);
//                }
//            } catch (NullPointerException e) {
//            }
//        }
//    }
//}