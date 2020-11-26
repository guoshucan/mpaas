package ghost.framework.data.core;

/**
 * 数据库更新返回。
 */
public final class DataBaseUpdateResult extends DataBaseResult {
    public DataBaseUpdateResult(IDataBase dataBase, int update) {
        super(dataBase);
        this.update = update;
    }

    public DataBaseUpdateResult(IDataBase dataBase, Exception error) {
        super(dataBase, error);
    }

    public DataBaseUpdateResult(IDataBase dataBase, int update, Exception error) {
        super(dataBase, error);
        this.update = update;
    }

    private int update;

    public DataBaseUpdateResult(IDataBase dataBase) {
        super(dataBase);
    }

    public int getUpdate() {
        return update;
    }
}