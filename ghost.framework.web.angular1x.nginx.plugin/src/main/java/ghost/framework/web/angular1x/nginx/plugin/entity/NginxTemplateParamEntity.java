package ghost.framework.web.angular1x.nginx.plugin.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * package: ghost.framework.web.angular1x.nginx.plugin.entity
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/8/21:17:58
 */
@Entity
@Table(name= "nginx_template_param_11ffe22d",
        indexes = {
                @Index(name = "uk", columnList = "id", unique = true)
//                @Index(name = "pk", columnList = "id,name,create_time,status,description")
        })
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class NginxTemplateParamEntity implements Serializable {
    private static final long serialVersionUID = 6395315624065802612L;
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
}
