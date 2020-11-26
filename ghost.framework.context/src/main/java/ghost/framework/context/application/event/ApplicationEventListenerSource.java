package ghost.framework.context.application.event;
import ghost.framework.beans.annotation.event.BeanMethodEventListener;
import ghost.framework.beans.application.event.AbstractApplicationEvent;
import ghost.framework.beans.utils.OrderAnnotationUtil;
import ghost.framework.util.Assert;
import ghost.framework.util.ReflectUtil;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/**
 * package: ghost.framework.context.application.event
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:应用事件监听源
 * @Date: 2020/2/19:22:45
 */
public class ApplicationEventListenerSource implements Comparable<ApplicationEventListenerSource>, ApplicationEventListener {
    /**
     * 初始化应用函数事件监听
     *
     * @param source 指定监听源
     */
    public ApplicationEventListenerSource(Object source) {
        Assert.notNull(source, "ApplicationEventListenerSource is source null error");
        this.source = source;
        List<Method> methodList = ReflectUtil.getAllAnnotationMethods(source, BeanMethodEventListener.class);
        OrderAnnotationUtil.methodListSort(methodList);
        List<MethodEventListener> eventListenerList = new ArrayList<>();
        for (Method method : methodList) {
            method.setAccessible(true);
            eventListenerList.add(new MethodEventListener(this.source, method, method.getAnnotation(BeanMethodEventListener.class).topic()));
        }
        synchronized (list) {
            list.addAll(eventListenerList);
        }
    }

    private List<MethodEventListener> list = new ArrayList<>();
    /**
     * 监听源
     */
    private final Object source;

    /**
     * 获取监听源
     *
     * @return
     */
    public Object getSource() {
        return source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplicationEventListenerSource that = (ApplicationEventListenerSource) o;
        return list.equals(that.list) &&
                source.equals(that.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(list, source);
    }

    @Override
    public String toString() {
        return "ApplicationEventListenerSource{" +
                "list=" + list.size() +
                ", source=" + source.toString() +
                '}';
    }

    @Override
    public int compareTo(ApplicationEventListenerSource eventListener) {
        return this.compareTo(eventListener);
    }

    @Override
    public void onApplicationEvent(AbstractApplicationEvent event) {

    }
}