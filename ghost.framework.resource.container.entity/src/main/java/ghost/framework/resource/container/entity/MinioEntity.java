package ghost.framework.resource.container.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * package: ghost.framework.resource.container.entity
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:MinIO is a high performance object storage server compatible with Amazon S3 APIs https://min.io/download
 * @Date: 2020/5/24:17:06
 */
//@Application
@Entity
@Table(name= "36d74a31badb4d829636286e58b98ab6",
        indexes = {
                @Index(name = "uk", columnList = "id", unique = true),
                @Index(name = "pk", columnList = "id,access_key_id,access_key_secret")
        })
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MinioEntity implements Serializable {
    private static final long serialVersionUID = -2676796342074232742L;
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
    @NotNull(message = "null error")
    @Length(max = 128, message = "length error")
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }
}
