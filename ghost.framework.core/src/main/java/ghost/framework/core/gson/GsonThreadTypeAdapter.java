//package ghost.framework.core.gson;
//
//import com.google.gson.TypeAdapter;
//import com.google.gson.stream.JsonReader;
//import com.google.gson.stream.JsonToken;
//import com.google.gson.stream.JsonWriter;
//
//import java.io.IOException;
//
///**
// * package: ghost.framework.core.gson
// *
// * @Author: 郭树灿{guo-w541}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:
// * @Date: 0:43 2020/2/1
// */
//public class GsonThreadTypeAdapter extends TypeAdapter<Thread> {
//    @Override
//    public void write(JsonWriter jsonWriter, Thread thread) throws IOException {
//        if (thread == null) {
//            jsonWriter.nullValue();
//            return;
//        }
//        jsonWriter.value(thread.getName() + ":" + thread.getId());
//    }
//
//    @Override
//    public Thread read(JsonReader jsonReader) throws IOException {
//        if (jsonReader.peek() == JsonToken.NULL) {
//            jsonReader.nextNull();
//            return null;
//        }
////        Thread thread = null;
////        try {
////            thread = Class.forName(jsonReader.nextString());
////        } catch (ClassNotFoundException exception) {
////            throw new IOException(exception);
////        }
//        return null;
//    }
//}