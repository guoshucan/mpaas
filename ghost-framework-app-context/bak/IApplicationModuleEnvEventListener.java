package ghost.framework.app.context.event.bak;//package ghost.framework.core.event.module.env;
//
//import ghost.framework.module.context.IModuleContent;
//
//import java.util.EventListener;
//import java.util.Properties;
//
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:应用模块env事件监听接口
// * @Date: 16:44 2019-10-07
// */
//public interface IApplicationModuleEnvEventListener extends EventListener {
//    /**
//     * 清除模块env内容
//     * @param content
//     */
//    void moduleEnvClear(IModuleContent content);
//    /**
//     * 添加模块env内容
//     * @param content 模块内容
//     * @param value 键
//     * @param v 值
//     */
//    void moduleEnvAdd(IModuleContent content, String value, Object v);
//    /**
//     * 删除模块env内容
//     * @param content 模块内容
//     * @param value 键
//     */
//    void moduleEnvRemove(IModuleContent content, String value);
//
//    /**
//     * 模块env更改后
//     * @param content 模块内容
//     * @param value 键
//     * @param v 值
//     */
//    void moduleEnvChangeAfter(IModuleContent content, String value, Object v);
//
//    /**
//     * 模块env更改前
//     * @param content 模块内容
//     * @param value 键
//     * @param v 值
//     */
//    void moduleEnvChangeBefore(IModuleContent content, String value, Object v);
//
//    /**
//     * 模块env合并后
//     * @param content 模块内容
//     * @param prefix 前缀
//     * @param properties 配置
//     */
//    void moduleEnvMergeAfter(IModuleContent content, String prefix, Properties properties);
//
//    /**
//     * 模块env合并前
//     * @param content 模块内容
//     * @param prefix 前缀
//     * @param properties 配置
//     */
//    void moduleEnvMergeBefore(IModuleContent content, String prefix, Properties properties);
//}
