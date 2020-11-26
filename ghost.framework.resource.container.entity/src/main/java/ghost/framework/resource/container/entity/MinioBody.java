package ghost.framework.resource.container.entity;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * package: ghost.framework.resource.container.entity
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/8:8:21
 */
public class MinioBody extends ResourceContainerEntity {
    /**
     * 资源授权键id
     */
    @NotNull(message = "null error")
    @Length(max = 128, min = 5, message = "length error")
    private String accessKeyId;

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    /**
     * 资源授权键秘密
     */
    @NotNull(message = "null error")
    @Length(max = 128, min = 5, message = "length error")
    private String accessKeySecret;

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    /**
     * 资源存储通桶
     */
//    @NotNull(message = "null error")
    @Length(max = 128, message = "length error")
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }
}
