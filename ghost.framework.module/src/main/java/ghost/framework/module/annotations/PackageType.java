package ghost.framework.module.annotations;//package ghost.framework.module.annotation;
//import ghost.framework.language.annotation.LocalMetadata;
//
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @Description:模块包类型枚举。
// * @Date: 0:30 2018/4/23
// */
//@LocalMetadata
//public enum PackageType {
//    /**
//     * 配置包。
//     */
//    config,
//    /**
//     * 处理适配器包。
//     */
//    adapter,
//    /**
//     * 内容包。
//     */
//    content,
//    /**
//     * 服务包。
//     */
//    service,
//    /**
//     * 模块包。
//     */
//    module,
//    /**
//     * 资源包。
//     */
//    resource,
//    /**
//     * 内置模块。
//     */
//    builtInmodule,
//    /**
//     * ui模块。
//     */
//    ui,
//    /**
//     * 转换器包。
//     */
//    converter;
//    /**
//     * 整数类转枚举。
//     * @param v
//     * @return
//     * @throws IllegalArgumentException
//     */
//    public final static PackageType valueOf(int v) throws IllegalArgumentException {
//        switch (v) {
//            case 0:
//                return config;
//            case 1:
//                return adapter;
//            case 2:
//                return content;
//            case 3:
//                return service;
//            case 4:
//                return module;
//            case 5:
//                return resource;
//            case 6:
//                return builtInmodule;
//            case 7:
//                return ui;
//            case 8:
//                return converter;
//        }
//        throw new IllegalArgumentException("包类型转换错误，参数[v:" + v + "]无效。");
//    }
//}