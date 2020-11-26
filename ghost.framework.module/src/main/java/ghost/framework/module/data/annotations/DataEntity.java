package ghost.framework.module.data.annotations;

import ghost.framework.module.data.IEntity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:数据实体注释
 * @Date: 22:46 2019/5/16
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DataEntity {
    /**
     * 实体列表
     * @return
     */
    Class<? extends IEntity>[] value();
}