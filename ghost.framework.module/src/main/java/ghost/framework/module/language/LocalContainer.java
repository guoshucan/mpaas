//package ghost.framework.module.language;
//
//import ghost.framework.core.locale.LocaleContainer;
//import ghost.framework.context.module.IModule;
//
//import java.io.IOException;
//import java.lang.reflect.InvocationTargetException;
//
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:本地化容器
// * @Date: 22:10 2018-11-22
// */
//public class LocalContainer extends LocaleContainer {
//    /**
//     * 资源拥有者
//     */
//    private IModule module;
//
//    /**
//     * 初始化本地化容器
//     *
//     * @param module 资源拥有者
//     * @throws IOException
//     */
//    public LocalContainer(IModule module) {
//        this.module = module;
//    }
//
//    /**
//     * @param container
//     * @throws IOException
//     */
//    public static void loadResources(LocalContainer container) throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
////        if (container.content.getInfo().getClass().isAnnotationPresent(Local.class)) {
////            Local annotation = container.content.getInfo().getClass().getAnnotation(Local.class);
////            if (StringUtils.isEmpty(annotation.resource())) {
////                throw new IOException("ModuleId:" + container.content.getId() + "->Local->StringUtils.isEmpty(annotation.resource())");
////            }
////            List<File> fileList = new ArrayList<>();
////            FileUtil.getFiles(container.content.getPath() + File.separator + annotation.resource(), fileList);
////            for (File file1 : fileList) {
////                String resourceName = file1.getPath().substring(container.content.getPath().length() + annotation.resource().length());
////                if (container.log.isDebugEnabled()) {
////                    container.log.debug(resourceName);
////                }
////                //本地化文件
////                /*
////                if (resourceName.endsWith(WebModuleConstant.JSON)) {
////                    if (container.log.isDebugEnabled()) {
////                        container.log.debug("ModuleId:" + container.content.getId() + "->Local->" + file1.getPath());
////                    }
////                    Gson gson = container.content.getApplicationContent().getBean(Gson.class);
////                    Map m = gson.fromJson(new String(FileUtils.readFileToByteArray(file1), "UTF-8"), Map.class);
////                    container.put(resourceName.replace("-", "_").replace(WebModuleConstant.JSON, ""), m);
////                    if (container.log.isDebugEnabled()) {
////                        container.log.debug("ModuleId:" + container.content.getId() + "->Local:" + resourceName.replace("-", "_") + "->" + gson.toJson(m));
////                    } else {
////                        container.log.info("ModuleId:" + container.content.getId() + "->Local:" + resourceName.replace("-", "_") + "->" + gson.toJson(m));
////                    }
////                }*/
////            }
////        }
//    }
//}
