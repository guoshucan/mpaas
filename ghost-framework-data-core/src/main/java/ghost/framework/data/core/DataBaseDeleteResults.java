package ghost.framework.data.core;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库控制器删除返回。
 */
public class DataBaseDeleteResults extends DataBaseResults {
    private List<DataBaseDeleteResult> results = new ArrayList<>();

    public List<DataBaseDeleteResult> getResults() {
        return results;
    }

    public int getCount() {
        int count = 0;
        for (DataBaseDeleteResult result : this.results) {
            if (result.getDelete() != -1) {
                count += result.getDelete();
            }
        }
        return count;
    }
}