package ghost.framework.context.environment;

import java.util.Properties;

/**
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 10:11 2019/11/21
 */
public interface IEnvironment extends IEnvironmentReader, IEnvironmentWriter {
    /**
     * 获取指定前缀的env
     *
     * @param prefix 前缀
     * @return 返回当前前缀的操作env
     */
    IEnvironmentPrefix getPrefix(String prefix);
//    /**
//     * 替换注释string类型参数有指定前后缀位置中心内容
//     *
//     * @param source 注释参数源内容
//     * @return 返回替换好的参数
//     * @throws EnvironmentInvalidException
//     */
//    String replaceMiddle(String source) throws EnvironmentInvalidException;

    /**
     * 指定前缀的写入器
     *
     * @param prefix 前缀
     * @return
     */
    IEnvironmentWriter getWriter(String prefix);

    /**
     * 获取指定前缀读取器
     *
     * @param prefix 前缀
     * @return
     */
    IEnvironmentReader getReader(String prefix);

    /**
     * 删除配置
     * @param properties
     */
    void remove(Properties properties);
}