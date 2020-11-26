package ghost.framework.core.security;

import ghost.framework.context.security.IPasswordDrive;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * package: ghost.framework.context.security
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:密码驱动器类
 * @Date: 2020/4/6:10:56
 */
public class PasswordDrive implements IPasswordDrive {
    /**
     * 密码验证
     * 默认使用sha模式比对
     * @param password    当前密码
     * @param oldPassword 旧的密码
     * @return 返回两个密码是否相同
     * @throws NoSuchAlgorithmException
     */
    @Override
    public boolean valid(String password, String oldPassword) throws NoSuchAlgorithmException {
        return sha(password).equals(sha(oldPassword));
    }
    /**
     * 密码验证
     *
     * @param password    当前密码
     * @param oldPassword 旧的密码
     * @param mode 加密比对模式
     * @return 返回两个密码是否相同
     * @throws NoSuchAlgorithmException
     */
    @Override
    public boolean valid(String password, String oldPassword, Mode mode) throws NoSuchAlgorithmException {
        if (mode == Mode.md5) {
            return md5(password).equals(sha(oldPassword));
        }
        return sha(password).equals(sha(oldPassword));
    }

    /**
     * sha加密密码
     * @param password 要加密的密码
     * @return 返回密码
     * @throws NoSuchAlgorithmException
     */
    @Override
    public String sha(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(password.getBytes());
        return new BigInteger(md.digest()).toString(32);
    }

    /**
     * md5加密密码
     * @param password 要加密的密码
     * @return 返回密码
     * @throws NoSuchAlgorithmException
     */
    @Override
    public String md5(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        return new BigInteger(1, md.digest()).toString(16);
    }
}