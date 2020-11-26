package ghost.framework.data;

/**
 * @Author: 郭树灿{guoshucan-pc}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * @Date: 11:02 2018-07-29
 */
abstract class DataSessionTask {
    protected DataSessionTask(Object item){
        this.item =item;
    }
    private Object item;
    public Object getItem() {
        return item;
    }
}
