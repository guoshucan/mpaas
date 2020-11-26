package ghost.framework.software.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * package: ghost.framework.software.entity
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:软件版本表
 * @Date: 2020/5/31:18:15
 */
@Entity
@Table(name="e76d5a2a844644afb33c9a9951743c82",
        indexes = {
                @Index(name = "uk", columnList = "id", unique = true),
                @Index(name = "pk", columnList = "id,version,create_time,status,description,software_id")
        })
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SoftwareVersionEntity implements Serializable {
        private static final long serialVersionUID = 339133439085204072L;
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
         * 软件id
         * {@link SoftwareEntity#getId()}
         */
        @Column(name = "software_id", updatable = false, nullable = false, length = 36, columnDefinition = "char(36)")
        @NotNull(message = "null error")
        @Length(max = 36, min = 36, message = "length error")
        private String softwareId;

        public void setSoftwareId(String softwareId) {
                this.softwareId = softwareId;
        }

        public String getSoftwareId() {
                return softwareId;
        }

        /**
         * 软件版本
         */
        @NotNull(message = "null error")
        @Length(max = 50, min = 1, message = "length error")
        @Column(name = "version", nullable = false, unique = true, length = 50, columnDefinition = "char(50)")
        private String version;

        public String getVersion() {
                return version;
        }

        public void setVersion(String version) {
                this.version = version;
        }

        /**
         * 创建时间
         */
        @NotNull(message = "null error")
        @Column(name = "create_time", nullable = false/*, columnDefinition = "comment '创建时间'"*/)
        private Date createTime;

        public Date getCreateTime() {
                return createTime;
        }

        public void setCreateTime(Date createTime) {
                this.createTime = createTime;
        }

        /**
         * 状态
         */
        @Min(value = 0, message = "min error")
        @Max(value = 1, message = "max error")
        @Column(name = "status", nullable = false/*, columnDefinition = "comment '状态'"*/)
        private short status;

        public short getStatus() {
                return status;
        }

        public void setStatus(short status) {
                this.status = status;
        }
        /**
         * 描述
         */
        @Length(max = 255, message = "length error")
        @Column(name = "description", length = 255, columnDefinition="varchar(255) null comment '描述'")
        private String description;

        public void setDescription(String description) {
                this.description = description;
        }

        public String getDescription() {
                return description;
        }
}
