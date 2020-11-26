package ghost.framework.context;

import java.lang.annotation.Annotation;

/**
 * package: ghost.framework.context
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/13:23:25
 */
public interface IGetAnnotationDomain {
    /**
     * 获取注释域
     * @return 返回注释域注释类型
     */
    default Class<? extends Annotation> getAnnotationDomain() {
        throw new UnsupportedOperationException(IGetAnnotationDomain.class.getName() + "#getAnnotationDomain()");
    }
}
