package ghost.framework.beans.enums;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:搜索模式
 * @Date: 12:15 2019/12/21
 */
public enum SearchStrategy {
    /**
     * 搜索应用Bean容器
     */
    App,
    /**
     * 搜索模块Bean容器
     * 模块是指当前模块，不跨模块容器搜索
     */
    Module,
    /**
     * 搜索当前位置容器
     */
    Current,
    /**
     * 搜索全部容器
     */
    All
}