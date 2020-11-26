package ghost.framework.data.core;
import ghost.framework.localization.annotations.LocalMetadata;
/**
 * 数据库并发模式枚举。
 */
@LocalMetadata
public enum DataConcurrencyMode {
    /**
     * 异步写入数据库。
     */
    async,
    /**
     * 并行写入数据。
     */
    parallel,
    /**
     * 顺序写入数据。
     */
    order,
    /**
     * 空闲优先顺序写入数据。
     * 数据库最空闲最先写入数据。
     */
    idleOrder;
    /**
     * 转换枚举值。
     *
     * @param value
     * @return
     */
    public static DataConcurrencyMode valueOf(int value) throws ArithmeticException {
        if (value == 0) return async;
        if (value == 1) return parallel;
        if (value == 2) return order;
        if (value == 3) return idleOrder;
        throw new ArithmeticException("转换数据插入模式枚举值[" + value + "]无效错误！");
    }
}