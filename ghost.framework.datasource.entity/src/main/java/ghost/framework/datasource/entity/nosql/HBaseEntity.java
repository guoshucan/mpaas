package ghost.framework.datasource.entity.nosql;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * package: ghost.framework.datasource.entity.nosql
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 2020/6/12:18:11
 */
@Entity
@Table(name="20b05ac8ac0246d2bc01140d50035f01",
        indexes = {
                @Index(name = "uk", columnList = "data_source_id", unique = true),
                @Index(name = "pk", columnList = "data_source_id")
        })
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class HBaseEntity implements Serializable {
    private static final long serialVersionUID = -6105848670041223645L;
}
