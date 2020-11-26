package ghost.framework.context.bean.factory.locale;

import ghost.framework.context.application.ApplicationConstant;
import ghost.framework.context.application.IApplication;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.beans.BeanException;
import ghost.framework.context.bean.factory.AbstractClassAnnotationBeanFactory;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.context.io.ResourceBytes;
import ghost.framework.context.converter.json.JsonConverterContainer;
import ghost.framework.context.converter.json.JsonToMapConverter;
import ghost.framework.context.locale.ILocaleDomain;
import ghost.framework.context.locale.ILocaleDomainContainer;
import ghost.framework.context.locale.LocaleDomain;
import ghost.framework.context.locale.LocaleKey;
import ghost.framework.context.module.ModuleConstant;
import ghost.framework.context.utils.AssemblyUtil;
import ghost.framework.util.MapMergeUtil;
import org.apache.commons.io.IOUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * package: ghost.framework.context.bean.factory.locale
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/14:18:05
 */
public abstract class AbstractClassLocaleAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V extends Object
                >
        extends AbstractClassAnnotationBeanFactory<O, T, E, V>
        implements IClassLocaleAnnotationBeanFactory<O, T, E, V> {
    /**
     * 加载
     *
     * @param event        事件对象
     * @param resourcePath 区域资源路径
     * @param localeNames  数组区域名称，格式比如[zh-CN]
     * @param localeTitles 数组区域标题，格式比如[简体中文]
     * @param container    区域容器接口
     */
    protected void loaderLocale(E event, String resourcePath, final String[] localeNames, final String[] localeTitles, ILocaleDomainContainer<ILocaleDomain> container) {
        if (localeNames.length != localeTitles.length) {
            throw new BeanException("localeNames.length!=localeTitles.length");
        }
        //获取json转换接口
        JsonToMapConverter jsonToMapConverter = this.getApp().getBean(JsonConverterContainer.class).getConverter(JsonToMapConverter.class);
        //锁定同步禁止删除添加修改
        synchronized (container.getSyncRoot()) {
            //遍历语言后缀
            int i = 0;
            for (String s : localeNames) {
                //加载国际化区域文件资源
                try {
                    //获取国际化json文件资源
                    ResourceBytes resourceBytes = AssemblyUtil.getResourceBytes(event.getTarget(), resourcePath + "/" + s + ".json");
                    if (resourceBytes == null) {
                        if (this.getLog().isDebugEnabled()) {
                            this.getLog().debug(event.getTarget().getName() + " not " + resourcePath + "/" + s + ".json");
                        } else {
                            this.getLog().error(event.getTarget().getName() + " not " + resourcePath + "/" + s + ".json");
                        }
                    } else {
                        //将json资源文件转换为map
                        Map<Object, Object> map = jsonToMapConverter.toMap(IOUtils.toString(resourceBytes.getBytes(),
                                event.getExecuteOwner().getEnv().getNullOrEmptyDefaultValue((event.getExecuteOwner() instanceof IApplication ? ApplicationConstant.Text.ENCODING : ModuleConstant.Text.ENCODING), "UTF-8")),
                                HashMap.class);
                        //创建键
                        LocaleKey key = new LocaleKey(s, localeTitles[i]);
                        //获取区域域
                        ILocaleDomain localeDomain = container.get(event.getValue());
                        //创建域对象
                        if (localeDomain == null) {
                            localeDomain = new LocaleDomain(event.getValue(), new HashMap<>());
                            localeDomain.getLocaleMap().put(key, map);
                            container.add(localeDomain);
                        } else {
                            if (localeDomain.getLocaleMap().containsKey(key)) {
                                MapMergeUtil.merge(localeDomain.getLocaleMap().get(key), map);
                            } else {
                                localeDomain.getLocaleMap().put(key, map);
                            }
                            //添加新的后从新合并
                            container.merge();
                        }
                    }
                } catch (Exception e) {
                    this.exception(e);
                }
                i++;
            }
        }
    }
}
