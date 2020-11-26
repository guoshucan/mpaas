package ghost.framework.core.bean.factory.injection.field;

import ghost.framework.beans.annotation.constraints.NotNull;
import ghost.framework.beans.annotation.constraints.Nullable;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.injection.TempDirectory;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.factory.injection.field.FieldInjectionFactory;
import ghost.framework.context.bean.factory.injection.field.IFieldInjectionTargetHandle;
import ghost.framework.context.exception.InjectionFieldException;
import ghost.framework.core.bean.factory.injection.InjectionTempDirectoryUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * package: ghost.framework.core.bean.factory.injection.field
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/2/9:22:46
 */
public class FieldTempDirectoryAnnotationInjectionFactory
        <
                O extends ICoreInterface,
                T extends Object,
                IF extends IFieldInjectionTargetHandle<O, T, Field, Object>
                >
        implements FieldInjectionFactory<O, T, IF> {
    public FieldTempDirectoryAnnotationInjectionFactory(@Autowired IApplication app) {
        this.app = app;
    }

    @Override
    public String toString() {
        return "FieldTempDirectoryAnnotationInjectionFactory{" +
                "annotation=" + annotation.toString() +
                '}';
    }

    private IApplication app;
    private Log log = LogFactory.getLog(FieldTempDirectoryAnnotationInjectionFactory.class);

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
    public void injector(IF event) {
        this.getLog().info("field:" + event.toString());
        //获取注入值注释对象
        TempDirectory directory = this.getAnnotation(event);
        //
        for (Class<?> c : directory.depend()) {
            event.getExecuteOwner().addBean(c);
        }
        //获取拥有者临时目录
        Object v = this.tempDirectoryUtils.getTempDirectory(event);
        //
        if (event.getField().isAnnotationPresent(Nullable.class)) {
            if (v != null) {
                //判断是否注入字符类型
                if (event.getField().getType().equals(String.class)) {
                    //返回目录路径
                    v = this.tempDirectoryUtils.getFilePath(((File) v).getPath(), this.tempDirectoryUtils.getSuffixMethodName(event, directory), directory);
                } else {
                    //返回目录
                    v = this.tempDirectoryUtils.getFile((File) v, this.tempDirectoryUtils.getSuffixMethodName(event, directory), directory);
                }
            }
        } else {
            //
            if (event.getField().isAnnotationPresent(NotNull.class)) {
                if (v == null) {
                    throw new InjectionFieldException(event.getField());
                }
            }
            //判断是否注入字符类型
            if (event.getField().getType().equals(String.class)) {
                //返回目录路径
                v = this.tempDirectoryUtils.getFilePath(((File) v).getPath(), this.tempDirectoryUtils.getSuffixMethodName(event, directory), directory);
            } else {
                //返回目录
                v = this.tempDirectoryUtils.getFile((File) v, this.tempDirectoryUtils.getSuffixMethodName(event, directory), directory);
            }
        }
        event.setValue(v);
    }
}