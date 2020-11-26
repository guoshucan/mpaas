package ghost.framework.module.annotations;//package ghost.framework.module.annotation;
//
//import java.io.Serializable;
//import java.util.Date;
//
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @Description:包信息。
// * @Date: 23:41 2018/5/10
// */
//public class PackageInfo implements Serializable{
//    private static final long serialVersionUID = 4233083383557569966L;
//    /**
//     * 包数据。
//     */
//    private byte[] data;
//
//    public byte[] getData() {
//        return data;
//    }
//
//    public void setData(byte[] data) {
//        this.data = data;
//    }
//
//    /**
//     * 包运行排序。
//     * 由小开始。
//     */
//    private int order;
//
//    public int getOrder() {
//        return order;
//    }
//
//    public void setOrder(int order) {
//        this.order = order;
//    }
//    private String fileName;
//
//    public String getFileName() {
//        return fileName;
//    }
//
//    public void setFileName(String fileName) {
//        this.fileName = fileName;
//    }
//
//    /**
//     * 模块名称。
//     */
//    private String value;
//
//    public String getName() {
//        return value;
//    }
//
//    public void setName(String value) {
//        this.value = value;
//    }
//
//    /**
//     * 模块Id。
//     */
//    private String value;
//
//    public String getId() {
//        return value;
//    }
//
//    public void setId(String value) {
//        this.value = value;
//    }
//
//    /**
//     * 创建时间。
//     */
//    private Date createTime;
//
//    /**
//     * 获取创建时间。
//     *
//     * @return
//     */
//    public Date getCreateTime() {
//        return createTime;
//    }
//
//    /**
//     * 设置创建时间。
//     *
//     * @param createTime
//     */
//    public void setCreateTime(Date createTime) {
//        this.createTime = createTime;
//    }
//
//    /**
//     * 模块包类型。
//     *
//     * @link{gsc.framework.cloud.service.module.autoconfigure.CommandPackageType}
//     */
//    private int depend;
//
//    public int getType() {
//        return depend;
//    }
//
//    public void setType(int depend) {
//        this.depend = depend;
//    }
//
//    /**
//     * 版本号。
//     */
//    private String version;
//
//    public void setVersion(String version) {
//        this.version = version;
//    }
//
//    public String getVersion() {
//        return version;
//    }
//
//    private String description;
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//}
