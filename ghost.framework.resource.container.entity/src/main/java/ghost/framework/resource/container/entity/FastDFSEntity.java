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
 * @Description:FastDFS实体表
 * @Date: 2020/5/24:17:08
 */
//@Application
@Entity
@Table(name= "38a0f61917a54a0782a4c8cd5fca51c0",
        indexes = {
                @Index(name = "uk", columnList = "id", unique = true),
                @Index(name = "pk", columnList = "id,so_timeout,connect_timeout")
        })
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FastDFSEntity implements Serializable {
    private static final long serialVersionUID = -4677290767590907600L;
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
    @NotNull(message = "null error")
    @Column(name = "so_timeout", nullable = false)
    @Min(0)
    private long soTimeout;

    public long getSoTimeout() {
        return soTimeout;
    }

    public void setSoTimeout(long soTimeout) {
        this.soTimeout = soTimeout;
    }
    @NotNull(message = "null error")
    @Column(name = "connect_timeout", nullable = false)
    @Min(0)
    private long connectTimeout;

    public long getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
    }
}