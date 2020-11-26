package ghost.framework.context.locale;

import java.io.Serializable;

/**
 * package: ghost.framework.context.locale
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:语言接口
 * @Date: 2020/3/12:12:18
 */
public interface ILanguage extends Serializable {
    /**
     * 获取语言名称
     * 格式比如:
     * en/zh-CN/zh-TW等三种语言格式
     * @return
     */
    String getName();

    /**
     * 设置语言名称
     * @param name
     */
    void setName(String name);

    /**
     * 获取语言标题
     * 比如 {@link ILanguage#getName()} 为en此标题为English，如果名称为zh-CN此标题为简体中文
     * @return
     */
    String getTitle();

    /**
     * 设置语言标题
     * @param title
     */
    void setTitle(String title);

    /**
     * 获取是否为当前指定语言
     * @return
     */
    boolean isCurrent();

    /**
     * 设置是否为当前指定语语言
     * @param current
     */
    void setCurrent(boolean current);
}