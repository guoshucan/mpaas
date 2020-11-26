package ghost.framework.data.core;

import ghost.framework.localization.annotations.LocalMetadata;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @Description:数据事务模式枚举。
 * @Date: 22:45 2018/3/28
 */
@LocalMetadata
public enum DataTransactionMode {
    /**
     * 并行模式。
     * 等待全部数据库完成再返回。
     */
    parallel,
//    /**
//     * 异步模式。
//     * 异步将在当前数据库操作完成立即返回，其它数据库使用并行处理。
//     */
//    async,
    /**
     * 同步模式。
     * 一个数据库执行完成再执行下一个数据库。
     */
    synch;
}
