package ghost.framework.util.generic;
import java.util.Comparator;
import java.util.Map;
/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 13:37 2018-09-22
 */
public class IntegerValueComparator<V> implements Comparator<Map.Entry<Integer, V>> {
    private OrderMode order;
    public IntegerValueComparator(OrderMode order) {
        this.order = order;
    }
    @Override
    public int compare(Map.Entry<Integer, V> o1, Map.Entry<Integer, V> o2) {
        switch (this.order) {
            case Asc:
                return o1.getKey().compareTo(o2.getKey());
            case Desc:
                return o2.getKey().compareTo(o1.getKey());
            default:
                throw new RuntimeException("顺序参数错误");
        }
    }
}