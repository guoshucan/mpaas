package ghost.framework.core.bean.factory.container;
import ghost.framework.beans.annotation.bean.factory.ClassAnnotationBeanFactory;
import ghost.framework.beans.annotation.tags.AnnotationTag;
import ghost.framework.context.base.ICoreInterface;
import ghost.framework.context.bean.annotation.json.JsonConverterFactory;
import ghost.framework.context.bean.factory.AbstractClassAnnotationBeanFactory;
import ghost.framework.context.bean.factory.IClassAnnotationBeanTargetHandle;
import ghost.framework.context.bean.factory.container.IListContainerAnnotationBeanFactory;
import ghost.framework.context.converter.json.JsonConverter;
import ghost.framework.context.converter.json.JsonConverterContainer;

import java.lang.annotation.Annotation;
/**
 * package: ghost.framework.core.bean.factory.container
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/20:19:52
 */
@ClassAnnotationBeanFactory(tag = AnnotationTag.AnnotationTags.Container)
public class JsonConverterAnnotationBeanFactory
        <
                O extends ICoreInterface,
                T extends Class<?>,
                E extends IClassAnnotationBeanTargetHandle<O, T, V, String, Object>,
                V extends JsonConverter
                >
        extends AbstractClassAnnotationBeanFactory<O, T, E, V>
        implements IListContainerAnnotationBeanFactory<O, T, E, V> {

    /**
     * 事件工厂注释
     */
    private final Class<? extends Annotation> annotation = JsonConverterFactory.class;
    /**
     * 获取绑定注释类型
     *
     * @return
     */
    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return annotation;
    }

    @Override
    public String toString() {
        return "JsonConverterAnnotationBeanFactory{" +
                "annotation=" + annotation +
                '}';
    }

    /**
     * 添加绑定后事件
     *
     * @param event 事件对象
     */
    @Override
    public void loader(E event) {
        JsonConverterContainer container = this.getApp().getBean(JsonConverterContainer.class);
        container.add(event.getValue());
    }

    /**
     * 删除绑定后事件
     *
     * @param event 事件对象
     */
    @Override
    public void unloader(E event) {
        //获取容器注释
//        JsonConverterFactory container = this.getAnnotation(event);
        JsonConverterContainer jsonConverterContainer = this.getApp().getBean(JsonConverterContainer.class);
//        jsonConverterContainer.add((JsonConverter) event.getValue());
    }
}
