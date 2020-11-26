package ghost.framework.context.base;

import ghost.framework.context.module.IModule;

import java.lang.reflect.Field;

/**
 * package: ghost.framework.core.base
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:对象注释注入
 * @Date: 2019/12/31:13:10
 */
public interface IObjectAnnotationInjection {
    void injectionAutowired(Object target, Field field);

    void injectionValue(Object target, Field field);

    void injectionModuleAutowired(IModule module, Object target, Field field);

    void injectionModuleValue(IModule module, Object target, Field field);

    void injectionModuleTempDirectory(IModule module, Object target, Field field);

    void injectionApplicationTempDirectory(Object target, Field field);
}