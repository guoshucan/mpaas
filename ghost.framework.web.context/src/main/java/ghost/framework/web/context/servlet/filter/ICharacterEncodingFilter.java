package ghost.framework.web.context.servlet.filter;
import ghost.framework.beans.annotation.constraints.Nullable;

import javax.servlet.Filter;

/**
 * package: ghost.framework.web.module.servlet.filter
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:编码过滤器接口
 * @Date: 2020/2/15:13:26
 */
public interface ICharacterEncodingFilter extends Filter {
    /**
     * 设置编码字符
     * {@link javax.servlet.http.HttpServletRequest#setCharacterEncoding}
     * {@link javax.servlet.http.HttpServletResponse#setCharacterEncoding()}
     * {@see #setForceEncoding}
     * {@see #setForceEncoding}
     */
    void setEncoding(@Nullable String encoding);

    /**
     * 获取编码字符
     *
     * @return
     */
    @Nullable
    String getEncoding();

    /**
     * 设置是否强制执行唯一编码
     * {@link javax.servlet.http.HttpServletRequest#getCharacterEncoding()}
     * {@link javax.servlet.http.HttpServletResponse#getCharacterEncoding()}
     *
     * @param forceEncoding
     */
    void setForceEncoding(boolean forceEncoding);

    /**
     * 设置是否强制执行请求唯一编码
     *
     * @param forceRequestEncoding
     */
    void setForceRequestEncoding(boolean forceRequestEncoding);

    /**
     * 获取是否请求执行请求唯一编码
     */
    boolean isForceRequestEncoding();

    /**
     * 设置是否响应执行唯一编码
     */
    void setForceResponseEncoding(boolean forceResponseEncoding);

    /**
     * 获取是否响应执行唯一编码
     */
    boolean isForceResponseEncoding();
}