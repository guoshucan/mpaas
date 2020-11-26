//package ghost.framework.context.converter.primitive;
//
//import java.util.Objects;
//
///**
// * package: ghost.framework.context.converter.primitive
// *
// * @Author: 郭树灿{gsc-e590}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 2020/6/23:21:28
// */
//public abstract class AbstractConverterPrimitive<S, T> implements ConverterPrimitive<S, T> {
//    public AbstractConverterPrimitive(Object domain) {
//        this.domain = domain;
//    }
//
//    public AbstractConverterPrimitive() {
//    }
//
//    private Object domain;
//
//    @Override
//    public Object getDomain() {
//        return domain;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        AbstractConverterPrimitive<?, ?> that = (AbstractConverterPrimitive<?, ?>) o;
//        return domain.equals(that.domain);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(domain);
//    }
//
//    @Override
//    public String toString() {
//        return "AbstractConverterPrimitive{" +
//                "domain=" + (domain == null ? "" : domain.toString()) +
//                '}';
//    }
//}
