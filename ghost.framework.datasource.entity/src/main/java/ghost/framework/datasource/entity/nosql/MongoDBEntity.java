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
@Table(name="8bdb1c8d8fcf4ea1a2748362c74c273a",
        indexes = {
                @Index(name = "uk", columnList = "data_source_id", unique = true),
                @Index(name = "pk", columnList = "data_source_id")
        })
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MongoDBEntity implements Serializable {
    private static final long serialVersionUID = -4230958279160703994L;
}
