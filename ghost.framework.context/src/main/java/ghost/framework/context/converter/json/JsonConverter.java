package ghost.framework.context.converter.json;

import ghost.framework.context.IGetDomain;

/**
 * package: ghost.framework.context.converter.json
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:json转换接口
 * 作为各种json转换的标准
 * @Date: 2020/6/9:20:52
 */
public interface JsonConverter extends IGetDomain {
    /**
     * 转化目标类型
     *
     * @return
     */
    default Class<?> getTargetType() {
        return null;
    }
}
