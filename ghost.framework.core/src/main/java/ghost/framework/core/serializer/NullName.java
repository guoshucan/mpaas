package ghost.framework.core.serializer;

/**
 * 空名称。
 */
public class NullName {
    public NullName() {
    }

    public NullName(String name) {
        this.name = name;
        this.isNull = true;
    }

    private boolean isNull;
    private String name;

    public boolean isNull() {
        return isNull;
    }

    public void setNull(boolean aNull) {
        isNull = aNull;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取默认对象。
     */
    public static NullName empty = new NullName();
}
