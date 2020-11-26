//package ghost.framework.module.module.bak;
//
//import ghost.framework.context.module.IModule;
//import ghost.framework.module.context.IModuleResourceContainer;
//import ghost.framework.util.FileUtil;
//import ghost.framework.module.io.ReadMemoryFile;
//import ghost.framework.util.ExceptionUtil;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ConcurrentSkipListMap;
//
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:模块资源容器。
// * @Date: 17:04 2018-10-01
// */
//public final class ModuleResourceContainer extends ConcurrentSkipListMap<String, ReadMemoryFile> implements AutoCloseable, IModuleResourceContainer {
//    /**
//     * 日记。
//     */
//    private Log log = LogFactory.getLog(this.getClass());
//
//    /**
//     * 资源容器所属模块
//     */
//    private IModule module;
//
//    /**
//     * 获取资源容器所属模块
//     *
//     * @return
//     */
//    public IModule getModule() {
//        return module;
//    }
//
//    /**
//     * 初始化模块资源容器
//     *
//     * @param module 设置资源容器所属模块
//     */
//    public ModuleResourceContainer(IModule module) throws IOException {
//        this.module = module;
//        loadResources(this);
//    }
//
//    /**
//     * 加载资源
//     *
//     * @param c
//     * @throws IOException
//     */
//    private static void loadResources(ModuleResourceContainer c) throws IOException {
//        //处理注释资源目录
////        if (!StringUtils.isEmpty(c.module.getInfo().resource())) {
////            c.paths.put(c.module.getInfo().resource());
////        }
////        if (c.module.getInfo().getClass().isAnnotationPresent(ResourceAnnotation.class)) {
////            ResourceAnnotation r = c.module.getInfo().getClass().getAnnotation(ResourceAnnotation.class);
////            for (String v : r.value()) {
////                c.paths.put(v);
////            }
////        }
//        for (String r : c.paths) {
//            loadResources(c, r);
//        }
//    }
//
//    /**
//     * 加载包资源
//     *
//     * @param c 模块资源容器
//     * @param r 资源路径
//     * @throws IOException
//     */
//    private static void loadResources(ModuleResourceContainer c, String r) throws IOException {
//        String rootPath = null;//c.module.isDev() ? c.module.getPath() : c.module.getTempDirectory().getPath();
//        List<File> fileList = new ArrayList<>();
//        FileUtil.getFiles(rootPath + File.separator + r, fileList);
//        for (File file1 : fileList) {
//            String resourceName = file1.getPath().substring(rootPath.length() + r.length());
//            if (!File.separator.equals("/")) {
//                resourceName = resourceName.replaceAll("\\\\", "/");
//            }
//            if (c.log.isDebugEnabled()) {
//                c.log.debug("/" + resourceName);
//            }
//            //web内容文件
//            //将文件添加入模块资源
//            c.put("/" + resourceName, file1.getPath());
//        }
//    }
//
//    /**
//     * 释放资源。
//     *
//     * @throws Exception
//     */
//    @Override
//    public void close() {
//        internalClose(this);
//    }
//
//    /**
//     * 内置资源释放器
//     */
//    private static void internalClose(ModuleResourceContainer container) {
//        synchronized (container.root) {
//            for (ReadMemoryFile file : container.values()) {
//                if (file != null) {
//                    try {
//                        file.close();
//                    } catch (Exception e) {
//                        if (container.log.isDebugEnabled()) {
//                            container.log.debug(ExceptionUtil.outStackTrace(e));
//                        } else {
//                            container.log.error(ExceptionUtil.outStackTrace(e));
//                        }
//                    }
//                }
//            }
//            container.clear();
//        }
//    }
//
//    /**
//     * 同步锁
//     */
//    private Object root = new Object();
//
//    /**
//     * 资源路径
//     */
//    private List<String> paths = new ArrayList<>();
//
//    /**
//     * 获取资源路径
//     *
//     * @return
//     */
//    public List<String> getPaths() {
//        return paths;
//    }
//
//    /**
//     * 添加页面文件
//     *
//     * @param value 文件名称
//     * @param path 文件
//     * @throws IOException
//     */
//    public void put(String value, String path) throws IOException {
//        this.put(value, new ReadMemoryFile(path));
//    }
//}
