package ghost.framework.resource.container.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * package: ghost.framework.resource.container.entity
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:腾讯云实体
 * @Date: 2020/5/24:17:06
 */
//@Application
@Entity
@Table(name= "a90948135316486bafdd5752d5ee2fa8",
        indexes = {
                @Index(name = "uk", columnList = "id", unique = true),
                @Index(name = "pk", columnList = "id,region,traffic_limit,access_key_id,access_key_secret")
        })
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TencentCloudCosEntity implements Serializable {
    private static final long serialVersionUID = -4158858378971440059L;
    /**
     * 主键id
     */
    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true, length = 36, columnDefinition = "char(36)")
    @NotNull(message = "null error")
    @Length(max = 36, min = 36, message = "length error")
    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
    /**
     * 区域
     * 这个参数只有腾讯云使用
     * 阿里云无需此参数
     */
    @NotNull(message = "null error")
    @Length(max = 128, min = 1, message = "length error")
    @Column(name = "region", length = 128, columnDefinition = "char(128)"/*, columnDefinition = "char(128) comment '区域\r\n这个参数只有腾讯云使用\r\n阿里云无需此参数'"*/)
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
    @Column(name = "traffic_limit", nullable = false/*, columnDefinition = "comment '限流单位\r\n这个参数只有腾讯云使用\r\n阿里云无需此参数'"*/)
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
    @Column(name = "access_key_id", nullable = false, length = 128, columnDefinition = "char(128)")
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
    @Column(name = "access_key_secret", nullable = false, length = 128, columnDefinition = "char(128)")
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
    @Column(name = "bucket", length = 128, columnDefinition = "char(128)")
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