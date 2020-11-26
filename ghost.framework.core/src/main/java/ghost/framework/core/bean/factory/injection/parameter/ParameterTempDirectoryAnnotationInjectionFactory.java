package ghost.framework.core.bean.factory.injection.parameter;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.injection.TempDirectory;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.injection.parameter.IParameterInjectionFactory;
import ghost.framework.context.bean.factory.injection.parameter.IParameterInjectionTargetHandle;
import ghost.framework.context.exception.InjectionParameterException;
import ghost.framework.core.bean.factory.injection.InjectionTempDirectoryUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * package: ghost.framework.core.bean.factory.injection.factory
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/9:23:56
 */
public class ParameterTempDirectoryAnnotationInjectionFactory<
        O extends ICoreInterface,
        T extends Object,
        E extends IParameterInjectionTargetHandle<O, T, Constructor, Method, Parameter, Object>
        >
        implements IParameterInjectionFactory<O, T, E> {
    public ParameterTempDirectoryAnnotationInjectionFactory(@Autowired IApplication app) {
        this.app = app;
    }

    @Override
    public String toString() {
        return "ParameterTempDirectoryAnnotationInjectionFactory{" +
                "annotation=" + annotation.toString() +
                '}';
    }

    private IApplication app;
     private Log log = LogFactory.getLog(ParameterTempDirectoryAnnotationInjectionFactory.class);

    @Override
    public Log getLog() {
        return log;
    }

    @Override
    public IApplication getApp() {
        return app;
    }

    private Class<? extends Annotation> annotation = TempDirectory.class;

    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return annotation;
    }

    /**
     * 初始化注入临时目录工具类
     */
    private InjectionTempDirectoryUtils tempDirectoryUtils = new InjectionTempDirectoryUtils();

    @Override
    public void injector(E event) {
        this.getLog().info("parameter:" + event.toString());
        //获取注入值注释对象
        TempDirectory directory = this.getAnnotation(event);
        //
        for (Class<?> c : directory.depend()) {
            event.getExecuteOwner().addBean(c);
        }
        //获取拥有者临时目录
        Object v = this.tempDirectoryUtils.getTempDirectory(event);
        //判断是否空
        if (event.getParameter().isAnnotationPresent(Nullable.class)) {
            if (v != null) {
                //判断是否注入字符类型
                if (event.getParameter().getType().equals(String.class)) {
                    //返回目录路径
                    v = this.tempDirectoryUtils.getFilePath(((File) v).getPath(), this.tempDirectoryUtils.getSuffixMethodName(event, directory), directory);
                } else {
                    //返回目录
                    v = this.tempDirectoryUtils.getFile((File) v, this.tempDirectoryUtils.getSuffixMethodName(event, directory), directory);
                }
            }
        } else {
            //判断是否不可空
            if (event.getParameter().isAnnotationPresent(NotNull.class)) {
                if (v == null) {
                    throw new InjectionParameterException(event.getParameter());
                }
            }
            //判断是否注入字符类型
            if (event.getParameter().getType().equals(String.class)) {
                //返回目录路径
                v = this.tempDirectoryUtils.getFilePath(((File) v).getPath(), this.tempDirectoryUtils.getSuffixMethodName(event, directory), directory);
            } else {
                //返回目录
                v = this.tempDirectoryUtils.getFile((File) v, this.tempDirectoryUtils.getSuffixMethodName(event, directory), directory);
            }
        }
        //设置临时目录
        event.setValue(v);
    }
}