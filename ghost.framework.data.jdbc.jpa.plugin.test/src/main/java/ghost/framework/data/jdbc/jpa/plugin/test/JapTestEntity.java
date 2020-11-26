package ghost.framework.data.jdbc.jpa.plugin.test;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * package: ghost.framework.data.jdbc.jpa.plugin.test
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/7/8:22:22
 */
@Entity
@Table(name="JapTestEntity",
        indexes = {
                @Index(name = "uk", columnList = "id,name", unique = true),
                @Index(name = "pk", columnList = "id,name,create_time")
        })
public class JapTestEntity implements Serializable {
    private static final long serialVersionUID = 6557669989044284608L;
    @Id
    @Column(name = "id", updatable = false, nullable = false, length = 36, columnDefinition = "char(36)")
    @NotNull(message = "null error")
    @Length(max = 36, min = 36, message = "length error")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NotNull(message = "null error")
    @Column(name = "create_time", nullable = false)
    private Date createTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 选中语言
     */
    @NotNull(message = "null error")
    @Column(name = "selected", nullable = false)
    private boolean selected;

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    @Column(name = "name", nullable = false, length = 100)
    @NotNull(message = "null error")
    @Length(max = 100, min = 0, message = "length error")
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}