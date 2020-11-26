package ghost.framework.data.core;

public final class DataBaseInsertResult extends DataBaseResult {
    public DataBaseInsertResult(IDataBase dataBase, int insert) {
        super(dataBase);
        this.insert = insert;
    }

    public DataBaseInsertResult(IDataBase dataBase, Exception error) {
        super(dataBase, error);
    }

    public DataBaseInsertResult(IDataBase dataBase, int insert, Exception error) {
        super(dataBase, error);
        this.insert = insert;
    }

    /**
     * -1表示错误。
     */
    private int insert;

    public DataBaseInsertResult(IDataBase dataBase) {
        super(dataBase);
    }

    public int getInsert() {
        return insert;
    }

    public void setInsert(int insert) {
        this.insert = insert;
    }

    public void setInsert(int insert, Exception e) {
        this.insert = insert;
        this.setError(e);
    }
}