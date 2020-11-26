package ghost.framework.data.core;

/**
 * 数据操作返回基础类。
 */
public abstract class DataBaseResult {
    public DataBaseResult(IDataBase dataBase){
        this.dataBase = dataBase;
    }
    public DataBaseResult(IDataBase dataBase, Exception error){
        this.dataBase = dataBase;
        this.error = error;
    }
    private IDataBase dataBase;

    public IDataBase getDataBase() {
        return dataBase;
    }

    private Exception error;

    public Exception getError() {
        return error;
    }

    public void setError(Exception error) {
        this.error = error;
    }
}
