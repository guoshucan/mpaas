package ghost.framework.resource.container.entity;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * package: ghost.framework.web.angular1x.resource.container.manage.plugin
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/5/24:22:25
 */
public class TencentCloudCosBody extends ResourceContainerEntity {
    /**
     * 区域
     * 这个参数只有腾讯云使用
     * 阿里云无需此参数
     */
    @NotNull(message = "null error")
    @Length(max = 128, min = 1, message = "length error")
    private String region;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * 限流单位
     * 这个参数只有腾讯云使用
     * 阿里云无需此参数
     */
    @NotNull(message = "null error")
    @Min(0)
    private int trafficLimit;

    public void setTrafficLimit(int trafficLimit) {
        this.trafficLimit = trafficLimit;
    }

    public int getTrafficLimit() {
        return trafficLimit;
    }
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
