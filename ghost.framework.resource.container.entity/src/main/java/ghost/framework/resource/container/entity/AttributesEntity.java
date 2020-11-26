//package ghost.framework.resource.container.entity;
//
//import org.hibernate.annotations.CacheConcurrencyStrategy;
//
//import javax.persistence.Cacheable;
//import javax.persistence.Entity;
//import javax.persistence.Index;
//import javax.persistence.Table;
//import java.io.Serializable;
//
///**
// * package: ghost.framework.resource.container.entity
// *
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:资源扩展属性
// * @Date: 2020/5/24:17:18
// */
////@Application
//@Entity
//@Table(name= "0fcfd78116cb4e5a8ba7704e479c2e7e",
//        indexes = {
//                @Index(name = "uk", columnList = "id", unique = true),
//                @Index(name = "pk", columnList = "id,access_key_id,access_key_secret")
//        })
//@Cacheable
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//public class AttributesEntity implements Serializable {
//    private static final long serialVersionUID = 6954515932875608710L;
//}