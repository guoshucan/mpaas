package ghost.framework.data.core;

/**
 * 数据库控制器操作返回基础类。
 */
public abstract class DataBaseResults {
    private boolean complete = true;

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
