package ghost.framework.resource.container.entity;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * package: ghost.framework.resource.container.entity
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:资源容器表
 * @Date: 2020/5/23:23:22
 */
//@Application
@Entity
@Table(name= "cbf74f5629474d48ab75de64df5b445e",
        indexes = {
                @Index(name = "uk", columnList = "id", unique = true),
                @Index(name = "pk", columnList = "id,name,create_time,status,endpoint,provider")
        })
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ResourceContainerEntity implements Serializable {
    private static final long serialVersionUID = 7534781751312327162L;
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
     * 资源名称
     */
    @NotNull(message = "null error")
    @Length(max = 100, min = 1, message = "length error")
    @Column(name = "name", nullable = false, length = 100/*, columnDefinition = "comment '资源名称'"*/)
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * 创建时间
     */
    @NotNull(message = "null error")
    @Column(name = "create_time", nullable = false/*, columnDefinition = "comment '资源创建时间'"*/)
    private Date createTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 资源状态
     */
    @Min(value = 0, message = "min error")
    @Max(value = 1, message = "max error")
    @Column(name = "status", nullable = false/*, columnDefinition = "comment '资源状态'"*/)
    private short status;

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    /**
     * 资源服务域名终结点url地址
     * endpoint=hdfs.path=hdfs://192.168.136.110:8020
     *
     */
    @Column(name = "endpoint", nullable = false, length = 255, columnDefinition = "char(255)")
    @NotNull(message = "null error")
    @Length(max = 255, min = 8, message = "length error")
    private String endpoint;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * 存储类型
     * 1为图片
     * 2为视频
     * 4为文档
     * 8为压缩文件
     * 16为数据库
     * 32为其它文件
     * 参数值可以做组合值
     */
    @NotNull(message = "null error")
    @Column(name = "type", nullable = false/*, columnDefinition = "comment '存储类型\r\n为图片\r\n为视频\r\n为文档\r\n为压缩文件\r\n为数据库\r\n为其它文件\r\n参数值可以做组合值'"*/)
    @Min(0)
    private int type;

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    /**
     * 提供者
     * 0为阿里云
     * 1为腾讯云
     * 2为FastDFS
     * 3为Hadoop HDFS
     * 4为Minio
     */
    @NotNull(message = "null error")
    @Column(name = "provider", nullable = false/*, columnDefinition = "comment '提供者\r\n为阿里云\r\n为腾讯云'"*/)
    @Min(0)
    @Max(4)
    private short provider;

    public short getProvider() {
        return provider;
    }

    public void setProvider(short provider) {
        this.provider = provider;
    }
    @OneToOne(targetEntity = AliyunOssEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false,
            //不创建外键约束
            foreignKey = @ForeignKey(name="none",value= ConstraintMode.NO_CONSTRAINT))
    private AliyunOssEntity ossEntity;

    public AliyunOssEntity getOssEntity() {
        return ossEntity;
    }
    @OneToOne(targetEntity = TencentCloudCosEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false,
            //不创建外键约束
            foreignKey = @ForeignKey(name="none",value= ConstraintMode.NO_CONSTRAINT))
    private TencentCloudCosEntity cloudCosEntity;

    public TencentCloudCosEntity getCloudCosEntity() {
        return cloudCosEntity;
    }
    @OneToOne(targetEntity = FastDFSEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false,
            //不创建外键约束
            foreignKey = @ForeignKey(name="none",value= ConstraintMode.NO_CONSTRAINT))
    private FastDFSEntity dfsEntity;

    public FastDFSEntity getDfsEntity() {
        return dfsEntity;
    }
    @OneToOne(targetEntity = HdfsEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false,
            //不创建外键约束
            foreignKey = @ForeignKey(name="none",value= ConstraintMode.NO_CONSTRAINT))
    private HdfsEntity hdfsEntity;

    public HdfsEntity getHdfsEntity() {
        return hdfsEntity;
    }
    @OneToOne(targetEntity = MinioEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false,
            //不创建外键约束
            foreignKey = @ForeignKey(name="none",value= ConstraintMode.NO_CONSTRAINT))
    private MinioEntity minioEntity;

    public MinioEntity getMinioEntity() {
        return minioEntity;
    }
}