package ghost.framework.core.bean.factory.injection;

import ghost.framework.beans.annotation.injection.TempDirectory;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.exception.TempDirectoryException;
import ghost.framework.context.bean.factory.IExecuteOwnerBeanTargetHandle;
import ghost.framework.context.application.ApplicationConstant;
import ghost.framework.context.module.ModuleConstant;
import ghost.framework.util.ReflectUtil;

import java.io.File;
import java.lang.reflect.Method;

/**
 * package: ghost.framework.core.bean.factory.injection.factory
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/10:0:08
 */
public class InjectionTempDirectoryUtils<O extends ICoreInterface, T extends Object, E extends IExecuteOwnerBeanTargetHandle<O, T>> {

    /**
     * 获取拥有者临时目录
     * 此函数必须在调用positionOwner函数定位好拥有者之后
     *
     * @param event 事件对象
     * @return
     */
    public File getTempDirectory(E event) {
        //判断拥有者
        if (event.getExecuteOwner() instanceof IApplication) {
            //获取应用临时目录
            return event.getExecuteOwner().getBean(ApplicationConstant.TEMP_DIRECTORY);
        }
        //获取模块临时模块
        return event.getExecuteOwner().getBean(ModuleConstant.TEMP_DIRECTORY);
    }
    /**
     * 获取临时文件路径
     *
     * @param path             临时目录路径
     * @param suffixMethodName 数组后缀累加函数名称
     * @param directory        临时目录注释
     * @return
     */
    public String getFilePath(String path, String[] suffixMethodName, TempDirectory directory) {
        for (String suffix : directory.increasePath()) {
            if (path.endsWith(File.separator)) {
                path = path + suffix;
            } else {
                path = path + File.separator + suffix;
            }
        }
        for (String suffix : suffixMethodName) {
            if (path.endsWith(File.separator)) {
                path = path + suffix;
            } else {
                path = path + File.separator + suffix;
            }
        }
        return path;
    }

    /**
     * 获取临时文件
     *
     * @param path             临时目录
     * @param suffixMethodName 数组后缀累加函数名称
     * @param directory        临时目录注释
     * @return
     */
    public File getFile(File path, String[] suffixMethodName, TempDirectory directory) {
        //创建临时目录
        File file = new File(this.getFilePath(path.getPath(), suffixMethodName, directory));
        //判断注释是否先删除后创建目录
        if (directory.deleteAfterCreation() && file.exists()) {
            file.delete();
        }
        //创建目录
        file.mkdirs();
        //判断是否退出时删除目录
        if (directory.deleteOnExit()) {
            file.deleteOnExit();
        }
        return file;
    }

    /**
     * 获取后缀函数名称列表
     *
     * @param event     事件对象
     * @param directory 临时目录注释
     * @return
     */
    public String[] getSuffixMethodName(E event, TempDirectory directory) {
        Class<?> c = event.getTarget().getClass();
        String[] strings = new String[directory.suffixMethodName().length];
        //遍历后缀函数列表
        for (String s : directory.suffixMethodName()) {
            //获取后缀函数
            Method method = ReflectUtil.findMethod(c, s);
            //调用后缀函数并返回函数目录路径
            try {
                method.setAccessible(true);
                strings[strings.length - 1] = String.valueOf(method.invoke(event.getTarget(), event.getOwner().newInstanceParameters(event.getTarget(), method)));
            } catch (Exception e) {
                throw new TempDirectoryException(e, directory);
            }
        }
        return strings;
    }
}