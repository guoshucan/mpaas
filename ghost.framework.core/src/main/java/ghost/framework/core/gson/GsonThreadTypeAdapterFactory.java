//package ghost.framework.core.gson;
//
//import com.google.gson.Gson;
//import com.google.gson.TypeAdapter;
//import com.google.gson.TypeAdapterFactory;
//import com.google.gson.reflect.TypeToken;
//
///**
// * package: ghost.framework.core.gson
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 0:49 2020/2/1
// */
//public class GsonThreadTypeAdapterFactory implements TypeAdapterFactory {
//    @Override
//    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
//        if (!Thread.class.isAssignableFrom(typeToken.getRawType())) {
//            return null;
//        }
//        return (TypeAdapter<T>) new GsonThreadTypeAdapter();
//    }
//}