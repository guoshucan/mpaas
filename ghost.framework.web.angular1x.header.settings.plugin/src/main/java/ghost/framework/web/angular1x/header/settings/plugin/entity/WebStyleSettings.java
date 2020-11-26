package ghost.framework.web.angular1x.header.settings.plugin.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * package: ghost.framework.web.angular1x.header.settings.plugin.entity
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:web样式设置表
 * @Date: 2020/8/18:0:37
 */
@Entity
@Table(name= "491e8c19d4044cabaff7d63f95b93d92",
        indexes = {
                @Index(name = "uk", columnList = "id", unique = true),
                @Index(name = "pk", columnList = "id,name,create_time,status,group_id,host_name,user_name,port,password,region_id")
        })
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WebStyleSettings implements Serializable {
    private static final long serialVersionUID = -716524487397133472L;
}