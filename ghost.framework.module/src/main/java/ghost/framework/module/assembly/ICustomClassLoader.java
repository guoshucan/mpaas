package ghost.framework.module.assembly;//package ghost.framework.module.assembly;
//
//import ghost.framework.util.FileUtil;
//
//import java.io.File;
//import java.net.MalformedURLException;
//import java.net.URL;
//
///**
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:自定义类加载器接口
// * @Date: 13:36 2019/6/12
// */
//public interface ICustomClassLoader extends AutoCloseable{
//    /**
//     * 添加包
//     *
//     * @param url 包url地址
//     */
//    void put(URL url);
//
//    /**
//     * 添加包
//     *
//     * @param file 包文件
//     */
//    default void put(File file) throws MalformedURLException {
//        this.put(FileUtil.toURL(file));
//    }
//
//    /**
//     * 添加包
//     *
//     * @param path 包文件
//     */
//    default void put(String path) throws MalformedURLException {
//        this.put(FileUtil.toURL(path));
//    }
//
//    Class<?> loadClass(String value) throws ClassNotFoundException;
//}
