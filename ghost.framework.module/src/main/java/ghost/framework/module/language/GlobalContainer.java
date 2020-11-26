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
// * @Description:国际化容器
// * @Date: 19:05 2019-01-31
// */
//public class GlobalContainer extends LocaleContainer {
//    /**
//     * 资源拥有者
//     */
//    private IModule module;
//
//    /**
//     * 初始化国际化容器
//     *
//     * @param module 资源拥有者
//     */
//    public GlobalContainer(IModule module) {
//        this.module = module;
//    }
//    /**
//     * 加载语音资源
//     *
//     * @param container
//     * @throws IOException
//     */
//    public static void loadResources(GlobalContainer container) throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
////        if (container.module.getInfo().getClass().isAnnotationPresent(Global.class)) {
////            Global annotation = container.module.getInfo().getClass().getAnnotation(Global.class);
////            if (StringUtils.isEmpty(annotation.resource())) {
////                throw new IOException("ModuleId:" + container.module.getId() + "->Global->StringUtils.isEmpty(annotation.resource())");
////            }
////            List<File> fileList = new ArrayList<>();
////            FileUtil.getFiles(container.module.getPath() + File.separator + annotation.resource(), fileList);
////            for (File file1 : fileList) {
////                String resourceName = file1.getPath().substring(container.module.getPath().length() + annotation.resource().length());
////                if (container.log.isDebugEnabled()) {
////                    container.log.debug(resourceName);
////                }
////                //国际化文件
//////                if (resourceName.endsWith(WebModuleConstant.JSON)) {
//////                    if (container.log.isDebugEnabled()) {
//////                        container.log.debug("ModuleId:" + container.module.getId() + "->Global->" + file1.getPath());
//////                    }
//////                    Gson gson = container.module.getApplicationContent().getBean(Gson.class);
//////                    Map m = gson.fromJson(new String(FileUtils.readFileToByteArray(file1), "UTF-8"), Map.class);
//////                    container.put(resourceName.replace("-", "_").replace(WebModuleConstant.JSON, ""), m);
//////                    if (container.log.isDebugEnabled()) {
//////                        container.log.debug("ModuleId:" + container.module.getId() + "->Global:" + resourceName.replace("-", "_") + "->" + gson.toJson(m));
//////                    } else {
//////                        container.log.info("ModuleId:" + container.module.getId() + "->Global:" + resourceName.replace("-", "_") + "->" + gson.toJson(m));
//////                    }
//////                }
////            }
////        }
//    }
//}
