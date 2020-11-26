//package ghost.framework.context.io;
//
//import java.io.IOException;
//
///**
// * package: ghost.framework.context.io
// *
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:i18n资源加载接口
// * @Date: 2020/6/13:23:08
// */
//public interface II18NResourceLoader extends IResourceClassLoader{
//    /**
//     * 获取资源
//     *
//     * @param path 资源路径
//     * @return 返回资源接口
//     * @throws IOException
//     */
//    default IResource getI18NResource(String path) throws IOException {
//        throw new UnsupportedOperationException(II18NResourceLoader.class.getName() + "#getI18NResource(" + path + ")");
//    }
//}