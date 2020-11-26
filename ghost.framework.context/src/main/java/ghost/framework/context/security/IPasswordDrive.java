package ghost.framework.context.security;

import java.security.NoSuchAlgorithmException;

/**
 * package: ghost.framework.context.security
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:密码启动器接口
 * @Date: 2020/4/6:10:59
 */
public interface IPasswordDrive {
    /**
     * 加密模式
     */
    enum Mode {
        /**
         * sha加密模式
         */
        sha,
        /**
         * md5加密模式
         */
        md5
    }
    /**
     * md5加密密码
     *
     * @param password 要加密的密码
     * @return 返回密码
     * @throws NoSuchAlgorithmException
     */
    String md5(String password) throws NoSuchAlgorithmException;

    /**
     * sha加密密码
     *
     * @param password 要加密的密码
     * @return 返回密码
     * @throws NoSuchAlgorithmException
     */
    String sha(String password) throws NoSuchAlgorithmException;

    /**
     * 密码验证
     *
     * @param password    当前密码
     * @param oldPassword 旧的密码
     * @param mode 加密比对模式
     * @return 返回两个密码是否相同
     * @throws NoSuchAlgorithmException
     */
    boolean valid(String password, String oldPassword, Mode mode) throws NoSuchAlgorithmException;
    /**
     * 密码验证
     * 默认使用sha模式比对
     * @param password    当前密码
     * @param oldPassword 旧的密码
     * @return 返回两个密码是否相同
     * @throws NoSuchAlgorithmException
     */
    boolean valid(String password, String oldPassword) throws NoSuchAlgorithmException;
}