package ghost.framework.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:字符串常量
 * @Date: 23:44 2019-10-26
 */
public final class StringUtil {
    public static final String CharacterEncoding = "CharacterEncoding";
    public static final String UTF8 = "UTF-8";

    /**
     * 判断是否有中心替换内容
     * @param source 源内容
     * @param start 前位置内容
     * @param end 尾部内容
     * @return
     */
    public static boolean isMiddle(String source, String start, String end) {
        if (source == null) {
            return false;
        }
        //获取开始子串的索引
        int index1 = source.indexOf(start);
        if (index1 != -1) {
            //获取结束字符的索引
            int index2 = source.indexOf(end, index1);
            if (index2 != -1 && index2 > index1) {
                //前后有指定内容
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * 获取指定前后缀的中间内容
     * @param source 源内容
     * @param start 前位置内容
     * @param end 尾部内容
     * @return 返回中间内容
     */
    public static String getMiddle(String source, String start, String end) {
        if (source == null) {
            return null;
        }
        //获取开始子串的索引
        int index1 = source.indexOf(start);
        if (index1 != -1) {
            //获取结束字符的索引
            int index2 = source.indexOf(end, index1);
            if (index2 != -1 && index2 > index1) {
                //将才分的字符串进行拼接
                String str3 = source.substring(0, index1 + start.length());
                String str4 = source.substring(index2, source.length());
                return str3  + str4;
            } else {
                return source;
            }
        } else {
            return source;
        }
    }
    /**
     * 替换指定前后缀的中间内容
     * @param source 源内容
     * @param start 前位置内容
     * @param end 尾部内容
     * @param value 前后中间插入内容
     * @return 返回内容
     */
    public static String replaceMiddle(String source, String start, String end, String value) {
        if (source == null) {
            return null;
        }
        String returnString = "";
        //获取开始子串的索引
        int index1 = source.indexOf(start);
        if (index1 != -1) {
            //获取结束字符的索引
            int index2 = source.indexOf(end, index1);
            if (index2 != -1 && index2 > index1) {
                //将才分的字符串进行拼接
                String str3 = source.substring(0, index1 + start.length());
                String str4 = source.substring(index2, source.length());
                returnString = str3 + value + str4;
            } else {
                return source;
            }
        } else {
            return source;

        }
        return returnString;
    }

    /**
     * 获取指定字符在字符内容中出现次数。
     *
     * @param source 字符源。
     * @param e      比对出现的字符。
     * @return 返回出现字符总数。
     */
    public static int getCount(String source, String e) {
        int count = 0;
        for (int i = 0; i <= source.length() - 1; i++) {
            String g = source.substring(i, i + 1);
            if (g.equals(e)) {
                count++;
            }
        }
        return count;
    }

    /**
     * 判断是否为空字符串，如果为空字符串返回null
     *
     * @param s
     * @return
     */
    public static String inEmptyToNull(String s) {
        return StringUtils.isEmpty(s) ? null : s;
    }
}