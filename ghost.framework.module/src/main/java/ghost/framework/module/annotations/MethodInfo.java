//package ghost.framework.module.annotations;
//
//import java.io.Serializable;
//import java.util.Date;
//
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @Description:模块函数信息。
// * @Date: 12:16 2018/4/30
// */
//public class MethodInfo implements Serializable {
//    private static final long serialVersionUID = -1940856325064459358L;
//    /**
//     * 是否为调试包。
//     */
//    private boolean packageDebug;
//
//    public boolean isPackageDebug() {
//        return packageDebug;
//    }
//
//    public void setPackageDebug(boolean packageDebug) {
//        this.packageDebug = packageDebug;
//    }
//
//    /**
//     * 包描述。
//     */
//    private String packageDescription;
//
//    public String getPackageDescription() {
//        return packageDescription;
//    }
//
//    public void setPackageDescription(String packageDescription) {
//        this.packageDescription = packageDescription;
//    }
//
//    /**
//     * 包版本。
//     */
//    private String  packageVersion;
//
//    public String getPackageVersion() {
//        return packageVersion;
//    }
//
//    public void setPackageVersion(String packageVersion) {
//        this.packageVersion = packageVersion;
//    }
//    /**
//     * 模块排序位置。
//     */
//    private int packageOrder;
//
//    public int getPackageOrder() {
//        return packageOrder;
//    }
//
//    public void setPackageOrder(int packageOrder) {
//        this.packageOrder = packageOrder;
//    }
//    /**
//     * 包名称。
//     */
//    private String packageName;
//
//    public String getPackageName() {
//        return packageName;
//    }
//
//    public void setPackageName(String packageName) {
//        this.packageName = packageName;
//    }
//
//    /**
//     * 类Id。
//     */
//    private String classId;
//
//    public String getClassId() {
//        return classId;
//    }
//
//    public void setClassId(String classId) {
//        this.classId = classId;
//    }
//
//    /**
//     * 类名称。
//     */
//    private String className;
//
//    public String getClassName() {
//        return className;
//    }
//
//    public void setClassName(String className) {
//        this.className = className;
//    }
//
//    /**
//     * 类版本。
//     */
//    private String  classVersion;
//
//    public String getClassVersion() {
//        return classVersion;
//    }
//
//    public void setClassVersion(String classVersion) {
//        this.classVersion = classVersion;
//    }
//
//    /**
//     * 类描述。
//     */
//    private String classDescription;
//
//    public String getClassDescription() {
//        return classDescription;
//    }
//
//    public void setClassDescription(String classDescription) {
//        this.classDescription = classDescription;
//    }
//    /**
//     * 是否为实例类。
//     */
//    private boolean classInstance;
//
//    public boolean isClassInstance() {
//        return classInstance;
//    }
//
//    public void setClassInstance(boolean classInstance) {
//        this.classInstance = classInstance;
//    }
//
//    /**
//     * 类排序。
//     */
//    private int classOrder;
//
//    public int getClassOrder() {
//        return classOrder;
//    }
//
//    public void setClassOrder(int classOrder) {
//        this.classOrder = classOrder;
//    }
//
//    /**
//     * 是否为静态函数。
//     */
//    private boolean isStatic;
//
//    public boolean isStatic() {
//        return isStatic;
//    }
//
//    public void setStatic(boolean aStatic) {
//        isStatic = aStatic;
//    }
//
//    /**
//     * 函数排序。
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
//    /**
//     * 描述。
//     */
//    private String description;
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    /**
//     * 名称。
//     */
//    private String value;
//
//    public void setName(String value) {
//        this.value = value;
//    }
//
//    public String getName() {
//        return value;
//    }
//
//    /**
//     * 指令标识。
//     */
//    private String commandSign;
//
//    public void setCommandSign(String commandSign) {
//        this.commandSign = commandSign;
//    }
//
//    public String getCommandSign() {
//        return commandSign;
//    }
//
//    /**
//     * 模块Id。
//     */
//    private String id;
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    /**
//     * 包Id。
//     */
//    private String packageId;
//
//    public String getPackageId() {
//        return packageId;
//    }
//
//    public void setPackageId(String packageId) {
//        this.packageId = packageId;
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
//}
