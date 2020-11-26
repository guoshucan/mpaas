package ghost.framework.module.language;//package ghost.framework.module.language;
//import ghost.framework.module.content.IModule;
//import ghost.framework.module.content.IModuleContent;
//import ghost.framework.module.content.ModuleTypes;
//import ghost.framework.module.events.module.IRegisteredModuleEventListener;
//import ghost.framework.module.events.obj.IRegistraObjectEventListener;
//import ghost.framework.module.events.module.IUninstallModuleEventListener;
//import ghost.framework.module.language.annotation.DynamicLanguageField;
//import ghost.framework.util.ReflectUtil;
//import ghost.framework.util.ExceptionUtil;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ConcurrentSkipListMap;
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:模块语言容器
// * @Date: 18:12 2019-05-11
// */
//public class ModuleLanguageContainer implements AutoCloseable, IRegisteredModuleEventListener, IUninstallModuleEventListener, IRegistraObjectEventListener {
//    /**
//     * 重写哈希值
//     * @return
//     */
//    @Override
//    public int hashCode() {
//        return this.syncRoot.hashCode() + this.map.hashCode() + this.content.hashCode() + this.log.hashCode() + super.hashCode();
//    }
//
//    /**
//     * 重写同等对象
//     * @param obj
//     * @return
//     */
//    @Override
//    public boolean equals(Object obj) {
//        if (obj instanceof ModuleLanguageContainer) {
//            ModuleLanguageContainer container = (ModuleLanguageContainer) obj;
//            return this.syncRoot.equals(container.syncRoot) &&
//                    this.log.equals(container.log) &&
//                    this.content.equals(container.content);
//        }
//        return super.equals(obj);
//    }
//
//    /**
//     * 日志
//     */
//    private Log log = LogFactory.getLog(this.getClass());
//
//    /**
//     * 获取日志
//     *
//     * @return
//     */
//    public Log getLog() {
//        return log;
//    }
//
//    /**
//     * 注册对象
//     *
//     * @param module 模块
//     * @param value  对象
//     */
//    @Override
//    public void registrarObjectEvent(IModule module, Object value) {
//        //判断是否语言动态注释
//        if (value.getClass().isAnnotationPresent(DynamicLanguageField.class)) {
//            DynamicLanguageField field = value.getClass().getAnnotation(DynamicLanguageField.class);
//            //国际化动态注释
//            if (!StringUtils.isEmpty(field.global())) {
//                try {
//                    Field f = ReflectUtil.findField(value.getClass(), field.global());
//                    f.setAccessible(true);
//                    f.set(value, this.content.getGlobalContainer().getContent(value.getClass()));
//                } catch (IllegalArgumentException e) {
//                    ExceptionUtil.debugOrError(this.log, e);
//                } catch (IllegalAccessException e) {
//                    ExceptionUtil.debugOrError(this.log, e);
//                }
//            }
//            //本地化动态注释
//            if (!StringUtils.isEmpty(field.local())) {
//                try {
//                    Field f = ReflectUtil.findField(value.getClass(), field.local());
//                    f.setAccessible(true);
//                    f.set(value, this.content.getLocalContainer().getContent(value.getClass()));
//                } catch (IllegalArgumentException e) {
//                    ExceptionUtil.debugOrError(this.log, e);
//                } catch (IllegalAccessException e) {
//                    ExceptionUtil.debugOrError(this.log, e);
//                }
//            }
//        }
//    }
//
//    /**
//     * 添加模块后事件
//     *
//     * @param module 模块对象
//     */
//    @Override
//    public void addAfter(IModule module) {
//        if (module.getType() == ModuleTypes.language) {
//            //判断语言包模块是否所属模块
//            if (this.content.getId().equals(module.getModuleOwnerId())) {
//                this.put((ModuleLanguage) module);
//                //重构语言
//                refactoringLanguage(this);
//                return;
//            }
//        }
//    }
//
//    /**
//     * 删除模块后事件
//     *
//     * @param module 模块对象
//     */
//    @Override
//    public void removeAfter(IModule module) {
//        if (module.getType() == ModuleTypes.language) {
//            //判断语言包模块是否所属模块
//            if (this.content.getId().equals(module.getModuleOwnerId())) {
//                this.remove(module.getId());
//                //重构语言
//                refactoringLanguage(this);
//                return;
//            }
//        }
//    }
//
//    /**
//     * 重构语言
//     */
//    private static void refactoringLanguage(ModuleLanguageContainer container) {
////        synchronized (container.syncRoot) {
////            for (ModuleLanguage module : container.map.values()) {
////                try {
////                    List<File> fileList = new ArrayList<>();
////                    //国际化
////                    if (module.getInfo().getClass().isAnnotationPresent(Global.class)) {
////                        Global globalAnnotation = module.getInfo().getClass().getAnnotation(Global.class);
////
////                    }
////                    //本地化
////                    if (module.getInfo().getClass().isAnnotationPresent(Local.class)) {
////                        Local localAnnotation = module.getInfo().getClass().getAnnotation(Local.class);
////                        fileList.clear();
////                        if (!StringUtils.isEmpty(localAnnotation.resource())) {
////                            FileUtil.getFiles(module.getPath() + File.separator + localAnnotation.resource(), fileList);
////                            for (File file1 : fileList) {
////                                String resourceName = file1.getPath().substring(module.getPath().length() + 1);
////                                if (container.log.isDebugEnabled()) {
////                                    container.log.debug(resourceName);
////                                }
////                                //本地化文件
////                                if (resourceName.startsWith(localAnnotation.resource()) && resourceName.endsWith(WebModuleConstant.JSON)) {
////                                    if (container.log.isDebugEnabled()) {
////                                        container.log.debug(file1.getPath());
////                                    }
////                                    Map m = container.owner.positionOwner().getGson().fromJson(new String(FileUtils.readFileToByteArray(file1), "UTF-8"), Map.class);
////                                    module.getLocalContainer().put(resourceName.substring(localAnnotation.resource().length() + 1).replace("-", "_").replace(WebModuleConstant.JSON, ""), m);
////                                    if (container.log.isDebugEnabled()) {
////                                        container.log.debug("ModuleId:" + module.getId() + "->local:" + resourceName.replace("-", "_") + "->" + container.owner.positionOwner().getGson().toJson(m));
////                                    } else {
////                                        container.log.info("ModuleId:" + module.getId() + "->local:" + resourceName.replace("-", "_") + "->" + container.owner.positionOwner().getGson().toJson(m));
////                                    }
////                                }
////                            }
////                        }
////                    }
////                } catch (Exception e) {
////                    if (container.log.isDebugEnabled()) {
////                        container.log.debug(e.getMessage());
////                    } else {
////                        container.log.error(e.getMessage());
////                    }
////                }
////            }
////        }
//    }
//
//    /**
//     * 释放资源
//     *
//     * @throws Exception
//     */
//    @Override
//    public void close() throws Exception {
//        if (this.content != null && this.content.getContent() != null && this.content.getContent().getEventListenerContainer() != null) {
//            //this.owner.positionOwner().remove(this);
//        }
//        this.content = null;
//        this.map.clear();
//        this.syncRoot = null;
//    }
//
//    /**
//     * 模块语言列表
//     */
//    private ConcurrentSkipListMap<String, ModuleLanguage> map = new ConcurrentSkipListMap<>();
//
//    /**
//     * 同步对象
//     */
//    private Object syncRoot = new Object();
//
//    /**
//     * 获取同步对象
//     *
//     * @return
//     */
//    public Object getSyncRoot() {
//        return syncRoot;
//    }
//
//    /**
//     * 拥有者
//     */
//    private IModuleContent content;
//
//    /**
//     * 初始化模块语言容器
//     *
//     * @param content 模块拥有者
//     */
//    public ModuleLanguageContainer(IModuleContent content) {
//        this.content = content;
//        //注册模块事件接口
//        //this.owner.positionOwner().put(this);
//    }
//
//    /**
//     * 添加模块语言
//     *
//     * @param module 语言模块
//     */
//    public void put(ModuleLanguage module) {
//        synchronized (this.syncRoot) {
//            this.map.put(module.getId(), module);
//        }
//    }
//
//    /**
//     * 删除模块语言
//     *
//     * @param value 语言模块Id
//     */
//    public void remove(String value) {
//        synchronized (this.syncRoot) {
//            this.map.remove(value);
//        }
//    }
//
//    /**
//     * 删除模块语言
//     *
//     * @param module 语言模块
//     */
//    public void remove(ModuleLanguage module) {
//        synchronized (this.syncRoot) {
//            this.map.remove(module.getId());
//        }
//    }
//
//    /**
//     * 获取模块语言列表
//     *
//     * @return
//     */
//    public List<String> getNames() {
//        List<String> list = new ArrayList<>();
//        for (ModuleLanguage module : this.map.values()) {
//            list.addAll(module.getLanguageNames());
//        }
//        return list;
//    }
//
//    /**
//     * 获取指定语言的语言模块
//     *
//     * @param language 语言名称，格式比如zh-CN
//     * @return
//     */
//    public ModuleLanguage get(String language) {
//        for (ModuleLanguage module : this.map.values()) {
//            if (module.getLanguageNames().contains(language)) {
//                return module;
//            }
//        }
//        return null;
//    }
//}
