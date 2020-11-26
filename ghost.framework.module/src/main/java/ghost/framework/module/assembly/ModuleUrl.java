package ghost.framework.module.assembly;//package ghost.framework.module.assembly;
//
//import ghost.framework.module.content.IModuleContent;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:模块包地址
// * @Date: 20:50 2019/6/19
// */
//public final class ModuleUrl {
//    public ModuleUrl(IModuleContent content) {
//        this.contentList = new ArrayList<>();
//        this.put(content);
//    }
//
//    /**
//     * 包引用模块列表
//     */
//    private List<IModuleContent> contentList = new ArrayList<>();
//
//    /**
//     * 获取引用模块列表
//     * @return
//     */
//    public List<IModuleContent> getContentList() {
//        return contentList;
//    }
//
//    /**
//     * 添加包引用模块
//     * @param content 模块内容
//     */
//    public void put(IModuleContent content) {
//        synchronized (this.contentList) {
//            this.contentList.put(content);
//        }
//    }
//
//    /**
//     * 删除包引用模块
//     * @param content 模块内容
//     */
//    public void remove(IModuleContent content) {
//        synchronized (this.contentList) {
//            this.contentList.remove(content);
//        }
//    }
//
//    /**
//     * 验证包模块是否引用
//     * @param content 模块内容
//     * @return
//     */
//    public boolean contains(IModuleContent content) {
//        synchronized (this.contentList) {
//            return this.contentList.contains(content);
//        }
//    }
//}
