//package ghost.framework.module.annotations;
//
//import java.io.Serializable;
//
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:类型注释参数。
// * @Date: 0:26 2018-06-21
// */
//public final class ClassAnnotationParameters implements Serializable{
//    private static final long serialVersionUID = -3837879651555414063L;
//    private String description;
//    private String id;
//    private String value;
//    private String version;
//    private boolean instance;
//
//    public boolean isInstance() {
//        return instance;
//    }
//
//    public void setInstance(boolean instance) {
//        this.instance = instance;
//    }
//
//    private int order;
//
//    public void setOrder(int order) {
//        this.order = order;
//    }
//
//    public int getOrder() {
//        return order;
//    }
//
//    public ClassAnnotationParameters(String id, String value, String version, int order, boolean instance, String description) {
//        this.id = id;
//        this.value = value;
//        this.version = version;
//        this.order = order;
//        this.instance = instance;
//        this.description = description;
//    }
//
//    public String getName() {
//        return value;
//    }
//
//    public void setName(String value) {
//        this.value = value;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getVersion() {
//        return version;
//    }
//
//    public void setVersion(String version) {
//        this.version = version;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//}