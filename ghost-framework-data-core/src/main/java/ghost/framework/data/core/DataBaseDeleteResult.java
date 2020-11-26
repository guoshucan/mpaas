package ghost.framework.data.core;

/**
 * 数据库删除返回。
 */
public final class DataBaseDeleteResult extends DataBaseResult {
    public DataBaseDeleteResult(IDataBase dataBase, int delete) {
        super(dataBase);
        this.delete = delete;
    }

    public DataBaseDeleteResult(IDataBase dataBase, Exception error) {
        super(dataBase, error);
    }

    public DataBaseDeleteResult(IDataBase dataBase, int delete, Exception error) {
        super(dataBase, error);
        this.delete = delete;
    }
    private int delete;

    public int getDelete() {
        return delete;
    }
}
