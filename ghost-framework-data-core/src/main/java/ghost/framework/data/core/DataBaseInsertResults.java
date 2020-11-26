package ghost.framework.data.core;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据插入返回。
 */
public class DataBaseInsertResults extends DataBaseResults {
    private List<DataBaseInsertResult> results = new ArrayList<>();

    /**
     * 获取返回列表。
     *
     * @return
     */
    public List<DataBaseInsertResult> getResults() {
        return results;
    }

    /**
     * 获取完成返回列表。
     *
     * @return
     */
    public List<DataBaseInsertResult> getCompleteResult() {
        List<DataBaseInsertResult> list = new ArrayList<>();
        for (DataBaseInsertResult result : this.results) {
            if (result.getInsert() != -1) {
                list.add(result);
            }
        }
        return list;
    }

    /**
     * 获取执行行数。
     * @return
     */
    public int getCount() {
        int count = 0;
        for (DataBaseInsertResult result : this.results) {
            if (result.getInsert() != -1) {
                count += result.getInsert();
            }
        }
        return count;
    }
}
