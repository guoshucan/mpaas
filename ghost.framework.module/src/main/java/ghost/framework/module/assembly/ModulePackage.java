package ghost.framework.module.assembly;//package ghost.framework.module.assembly;
//
//import ghost.framework.module.content.IModuleContent;
//
//import java.net.URL;
//import java.util.Vector;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:模块包信息
// * @Date: 21:40 2019/6/19
// */
//public final class ModulePackage extends URLPackage {
//    /**
//     * 模块url依赖列表
//     */
//    private ModuleUrlReference urlReference;
//    private  ModuleClassLoader loader;
//    public ModulePackage(URL url, ModuleClassLoader loader) {
//        super(url);
//        this.loader = loader;
//    }
//    /**
//     * 该包引用的模块列表
//     */
//    private Vector<IModuleContent> contentList = new Vector<>();
//
//    /**
//     * 获取该包引用的模块列表
//     * @return
//     */
//    public Vector<IModuleContent> getContentList() {
//        return contentList;
//    }
//    /**
//     * 后去模块url依赖列表
//     *
//     * @return
//     */
//    public ModuleUrlReference getUrlReference() {
//        return urlReference;
//    }
//
//    /**
//     * 初始化模块包信息
//     *
//     * @param url         包
//     * @param urlReference 模块url依赖列表
//     */
//    public ModulePackage(URL url, ModuleUrlReference urlReference) {
//        super(url);
//        this.urlReference = urlReference;
//    }
//    /**
//     * 初始化模块包信息
//     *
//     * @param url         包对象
//     * @param urlReference 模块url依赖列表
//     */
//    public ModulePackage(URL url, IModuleContent content, ModuleUrlReference urlReference) {
//        super(url);
//        this.content = content;
//        this.urlReference = urlReference;
//    }
//    private IModuleContent content;
//
//    public IModuleContent getContent() {
//        return content;
//    }
//}
