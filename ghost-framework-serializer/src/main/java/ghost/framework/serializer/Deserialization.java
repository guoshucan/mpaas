package ghost.framework.serializer;//package gsc.framework.serializer;
//import gsc.framework.serializer.data.DataType;
//import java.io.DataInputStream;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
///**
// * 反序列化。
// */
//public class Deserialization {
//    /**
//     * 初始化反序列化。
//     */
//    public Deserialization() {
//
//    }
//
//    /**
//     * 初始化反序列化。
//     *
//     * @param dateFormat 设置日期格式化。
//     */
//    public Deserialization(SimpleDateFormat dateFormat) {
//        this.dateFormat = dateFormat;
//    }
//
//    /**
//     * 日期格式化。
//     */
//    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
//
//    /**
//     * 获取日期格式化。
//     *
//     * @return
//     */
//    public SimpleDateFormat getDateFormat() {
//        return dateFormat;
//    }
//
//    /**
//     * 设置日期格式化。
//     *
//     * @param dateFormat
//     */
//    public void setDateFormat(SimpleDateFormat dateFormat) {
//        this.dateFormat = dateFormat;
//    }
//
//    /**
//     * 读取响应对象。
//     *
//     * @param inputStream
//     * @throws IOException
//     */
//    public void get(DataInputStream inputStream, BaseResponse response) throws IOException {
//        response.setError(inputStream.readInt());
//        response.setErrorMessage(Nio.readString(inputStream));
//        response.setContent(Nio.readString(inputStream));
//    }
//
//    /**
//     * 读取数据响应对象。
//     * @param inputStream
//     * @param response
//     * @throws IOException
//     */
//    public void getData(DataInputStream inputStream, DataResponse response) throws IOException {
//        this.get(inputStream, response);
//        HashMap<String, Object> map = new HashMap<String, Object>();
//        response.setData(map);
//        int i = inputStream.readInt();
//        while (i-- >= 0) {
//            switch (DataType.valueOf(inputStream.readInt())) {
//                case Byte:
//                    map.put(Nio.readString(inputStream), inputStream.readByte());
//                    break;
//                case Short:
//                    map.put(Nio.readString(inputStream), inputStream.readShort());
//                    break;
//                case Int:
//                    map.put(Nio.readString(inputStream), inputStream.readInt());
//                    break;
//                case Long:
//                    map.put(Nio.readString(inputStream), inputStream.readLong());
//                    break;
//                case Double:
//                    map.put(Nio.readString(inputStream), inputStream.readDouble());
//                    break;
//                case Float:
//                    map.put(Nio.readString(inputStream), inputStream.readFloat());
//                    break;
//                case Boolean:
//                    map.put(Nio.readString(inputStream), inputStream.readBoolean());
//                    break;
//                case Date:
//                    String name = Nio.readString(inputStream);
//                    Date date = Nio.readDate(inputStream);
//                    if (this.dateFormat == null)
//                        map.put(name, date == null ? "-1" : date);
//                    else
//                        map.put(name, date == null ? "" : this.dateFormat.format(date));
//                    break;
////                case DateTime:
////                    String name = Nio.readString(inputStream);
////                    Date date = Nio.readDate(inputStream);
////                    if (this.dateFormat == null)
////                        map.put(name, date == null ? "-1" : date);
////                    else
////                        map.put(name, date == null ? "" : this.dateFormat.format(date));
////                    break;
//                case String:
//                    map.put(Nio.readString(inputStream), Nio.readString(inputStream));
//                    break;
//            }
//        }
//    }
//}