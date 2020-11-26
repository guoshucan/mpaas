package ghost.framework.data.core;

import ghost.framework.localization.annotations.LocalMetadata;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:数据库状态枚举。
 * @Date: 6:21 2018/5/28
 */
@LocalMetadata()
public enum DataBaseStatus {
    /**
     * 禁用。
     */
    disabled,
    /**
     * 启用。
     */
    enable,
    /**
     * 同步。
     */
    synchronize,
    /**
     * 镜像。
     */
    image,
    /**
     * 热备。
     */
    hotStandby,
    /**
     * 读取。
     */
    read,
    /**
     * 写入。
     */
    write;
    /**
     * 转换数据库状态枚举名称。
     * @param value
     * @return
     * @throws ArithmeticException
     */
    public static String valueOfName(int value) throws ArithmeticException {
        return valueOf(value).name();
    }
    /**
     * 转换数据库状态枚举值。
     * @param value 枚举整数值。
     * @return 返回枚举状态。
     * @throws ArithmeticException
     */
    public static DataBaseStatus valueOf(int value) throws ArithmeticException {
        switch (value) {
            case 0:
                return disabled;
            case 1:
                return enable;
            case 2:
                return synchronize;
            case 3:
                return image;
            case 4:
                return hotStandby;
            case 5:
                return read;
            case 6:
                return write;
            default:
                throw new ArithmeticException("转换数据库状态式枚举值[" + value + "]无效错误！");
        }
    }
}
