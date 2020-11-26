package ghost.framework.context.pack;
import ghost.framework.context.proxy.ProxyUtil;
import org.apache.commons.logging.Log;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
/**
 * package: ghost.framework.context.pack
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:类型解析接口
 * @Date: 2020/8/2:12:26
 */
public interface IClassResolve {
    /**
     * 获取日志
     *
     * @return
     */
    Log getLog();

    /**
     * 获取目标类型注释列表
     *
     * @param target     目标类型
     * @param annotation 注释类型
     * @param <R>        返回类型
     * @return 返回注释类型列表
     */
    default <R extends Annotation> List<R> getDeclaredAnnotationList(Class<?> target, Class<? extends Annotation> annotation) {
        return this.getDeclaredAnnotationList(target, null, annotation);
    }

    /**
     * 获取目标类型注释列表
     *
     * @param target     目标类型
     * @param annotation 注释类型
     * @param <R>        返回类型
     * @return 返回注释类型列表
     */
    default <R extends Annotation> List<R> getAnnotationList(Class<?> target, Class<? extends Annotation> annotation) {
        return this.getAnnotationList(target, null, annotation);
    }

    /**
     * 获取目标类型注释列表
     *
     * @param target           目标类型
     * @param parentAnnotation 注释父级类型
     * @param annotation       注释类型
     * @param <R>              返回类型
     * @return 返回注释类型列表
     */
    default <R extends Annotation> List<R> getDeclaredAnnotationList(Class<?> target, Class<? extends Annotation> parentAnnotation, Class<? extends Annotation> annotation) {
        List<R> rs = new ArrayList<>();
        if (parentAnnotation != null && target.isAnnotationPresent(parentAnnotation)) {
            for (Annotation aa : target.getDeclaredAnnotationsByType(parentAnnotation)) {
                rs.addAll(ProxyUtil.getProxyAnnotationObjectList(aa, annotation));
            }
        }
        if (target.isAnnotationPresent(annotation)) {
            for (Annotation aa : target.getDeclaredAnnotationsByType(annotation)) {
                rs.add((R) aa);
            }
        }
        return rs;
    }

    /**
     * 获取目标类型注释列表
     *
     * @param target           目标类型
     * @param parentAnnotation 注释父级类型
     * @param annotation       注释类型
     * @param <R>              返回类型
     * @return 返回注释类型列表
     */
    default <R extends Annotation> List<R> getAnnotationList(Class<?> target, Class<? extends Annotation> parentAnnotation, Class<? extends Annotation> annotation) {
        List<R> rs = new ArrayList<>();
        if (parentAnnotation != null && target.isAnnotationPresent(parentAnnotation)) {
            for (Annotation aa : target.getAnnotationsByType(parentAnnotation)) {
                rs.addAll(ProxyUtil.getProxyAnnotationObjectList(aa, annotation));
            }
        }
        if (target.isAnnotationPresent(annotation)) {
            for (Annotation aa : target.getAnnotationsByType(annotation)) {
                rs.add((R) aa);
            }
        }
        return rs;
    }
}