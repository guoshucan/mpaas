package ghost.framework.util;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:类型工具。
 * @Date: 8:00 2018-06-14
 */
public final class TypeUtil {
    /**
     * 数组字符整数列表转换整数列表。
     * @param str 整数字符内容。
     * @param delimiter 整数字符分割符。
     * @return 返回整数列表。
     */
    public static List<Integer> arrayStringToIntegerList(String str, String delimiter) {
        List<Integer> list = new ArrayList<>();
        String[] commandStrings = StringUtils.split(str, delimiter);
        for (String s : commandStrings) {
            list.add(Integer.parseInt(s));
        }
        return list;
    }
}
