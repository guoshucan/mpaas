//package ghost.framework.core.event.order;
//
//import ghost.framework.context.bean.BeanMethod;
//import ghost.framework.context.maven.OwnerEventTargetHandle;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * package: ghost.framework.core.event.order.factory.method
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 15:32 2020/1/26
// */
//public class MethodOrderEventTargetHandle<O, T> extends OwnerEventTargetHandle<O, T> implements IMethodOrderEventTargetHandle<O, T> {
//    /**
//     * 初始化事件不表处理头
//     *
//     * @param owner  设置事件目标对象拥有者
//     * @param target 设置目标对象
//     */
//    public MethodOrderEventTargetHandle(O owner, T target) {
//        super(owner, target);
//    }
//
//    /**
//     * 获取绑定函数列表
//     *
//     * @return
//     */
//    @Override
//    public List<List<BeanMethod>> getBeanMethodList() {
//        //如果还有执行函数排序事件工厂时返回需要创建一个空白列表
//        if (beanMethodList == null) {
//            beanMethodList = new ArrayList<>();
//        }
//        return beanMethodList;
//    }
//
//    private List<List<BeanMethod>> beanMethodList;
//
//    @Override
//    public void setBeanMethodList(List<List<BeanMethod>> beanMethodList) {
//        this.beanMethodList = beanMethodList;
//    }
//}