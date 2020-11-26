package ghost.framework.util.generic;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:列表工具
 * @Date: 14:25 2019-10-27
 */
public final class ListUtil {
    /**
     * 数组转列表
     *
     * @param array 数组
     * @param <T>
     * @return
     */
    public static <T> List<T> toList(T[] array) {
        List<T> list = new ArrayList<>();
        if (array == null) {
            return list;
        }
        for (T t : array) {
            list.add(t);
        }
        return list;
    }
}
