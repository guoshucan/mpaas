//package ghost.framework.app.core;
//
//import ghost.framework.app.context.IApplicationResource;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:应用资源内容类
// * @Date: 11:23 2019/6/26
// */
//abstract class ApplicationResourceContent extends ApplicationDirectoryContent implements IApplicationResource {
//    /**
//     * 初始化应用资源内容类
//     * @param rootClass
//     * @throws Exception
//     */
//    protected ApplicationResourceContent(final Class<?> rootClass) throws Exception {
//        super(rootClass);
//        this.getLog().info("~ApplicationResourceContent");
//    }
//
//    /**
//     * 初始化
//     * @throws Exception
//     */
//    @Override
//    protected void init() throws Exception{
//        this.getLog().info("~ApplicationResourceContent.init->Before");
//        super.init();
//        this.getLog().info("~ApplicationResourceContent.init->After");
//    }
////    /**
////     * 从应用获取资源url
////     * 该方法主要给ApplicationClassLoader.getResource函数提供从基础读取不到资源url是调用该方法获取应用的资源url
////     *
////     * @param value 资源名称
////     * @return
////     */
////    @Override
////    public URL getResource(String value) {
////        URL url = null;
////        synchronized (this.getModuleMap()) {
////            for (IModuleContent c : this.getModuleMap().values()) {
//////                for (Resource r : c.getModuleAnnotation().resource()) {
//////                    if (r.value().equals(value)) {
//////                url = c.getClassLoader().getUrlReference().getResource(value);
//////                if (url != null) {
//////                    return url;
//////                }
//////                    }
//////                }
////            }
////        }
////        return null;
////    }
//}
