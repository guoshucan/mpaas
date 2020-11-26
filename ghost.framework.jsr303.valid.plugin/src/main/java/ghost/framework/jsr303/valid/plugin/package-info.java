/**
 * package: ghost.framework.jsr303.valid.plugin
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description: {@link javax.validation.Valid}（标准JSR-303规范）
 * @Date: 2020/2/29:13:23
 */
@PluginPackage(
        //插件名称
//        name = "java-jsr-303-valid-plugin",
        loadClass = {
                JSR303ValidAnnotationValidFactory.class,
                JSR303AssertFalseAnnotationValidFactory.class,
                JSR303AssertTrueAnnotationValidFactory.class,
                JSR303DecimalMaxAnnotationValidFactory.class,
                JSR303DecimalMinAnnotationValidFactory.class,
                JSR303DigitsAnnotationValidFactory.class,
                JSR303EmailAnnotationValidFactory.class,
                JSR303FutureAnnotationValidFactory.class,
                JSR303FutureOrPresentAnnotationValidFactory.class,
                JSR303MaxAnnotationValidFactory.class,
                JSR303MinAnnotationValidFactory.class,
                JSR303NegativeAnnotationValidFactory.class,
                JSR303NegativeOrZeroAnnotationValidFactory.class,
                JSR303NotBlankAnnotationValidFactory.class,
                JSR303NotEmptyAnnotationValidFactory.class,
                JSR303NotNullAnnotationValidFactory.class,
                JSR303NullAnnotationValidFactory.class,
                JSR303PastAnnotationValidFactory.class,
                JSR303PastOrPresentAnnotationValidFactory.class,
                JSR303PatternAnnotationValidFactory.class,
                JSR303PositiveAnnotationValidFactory.class,
                JSR303PositiveOrZeroAnnotationValidFactory.class,
                JSR303SizeAnnotationValidFactory.class
        }
)
package ghost.framework.jsr303.valid.plugin;
import ghost.framework.beans.plugin.bean.annotation.PluginPackage;