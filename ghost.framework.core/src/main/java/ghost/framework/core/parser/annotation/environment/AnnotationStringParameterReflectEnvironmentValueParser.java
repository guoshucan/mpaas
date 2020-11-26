package ghost.framework.core.parser.annotation.environment;

import java.lang.annotation.Annotation;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:注释替换env值解析器接口
 * @Date: 20:06 2020/1/5
 */
public interface AnnotationStringParameterReflectEnvironmentValueParser extends IAnnotationEnvironmentParser {
    /**
     * 注释替换env值解析
     * @param a 注释类型
     */
    void annotationReflectValue(Annotation a);
}