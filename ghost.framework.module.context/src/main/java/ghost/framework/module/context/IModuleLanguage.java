package ghost.framework.module.context;

import java.util.List;

/**
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:模块语言包接口
 * @Date: 23:35 2019/4/24
 */
public interface IModuleLanguage {
    /**
     * 获取语言名称
     *
     * @return
     */
    List<String> getLanguageNames();

    /**
     * 设置语言名称
     *
     * @param names
     */
    void setLanguageNames(List<String> names);
}