//package ghost.framework.web.module.convert;
//
//import ghost.framework.beans.annotation.converter.ConverterFactory;
//import ghost.framework.context.converter.AbstractConverterFactory;
//import ghost.framework.context.converter.ConverterException;
//
//import javax.servlet.http.HttpServletRequest;
//
///**
// * package: ghost.framework.web.module.convert
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 2020/3/5:14:10
// */
//@ConverterFactory
//public class ConverterHttpServletRequestFactory <T extends HttpServletRequest, O extends Object> extends AbstractConverterFactory<T, O> {
//
//    @Override
//    public Class<?> getType() {
//        return Boolean.class;
//    }
//
//    @Override
//    public boolean isCan(O source) {
//        return source instanceof HttpServletRequest;
//    }
//
//    @Override
//    public T converter(O source, String characterEncoding) throws ConverterException {
//        if (this.isCan(source)) {
//            return (T) source;
//        }
//        return null;
//    }
//}