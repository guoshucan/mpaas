package ghost.framework.serializer;//package gsc.framework.serializer;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 序列化。
// */
//public class Serialization {
//    /**
//     * 序列化对象。
//     *
//     * @param object
//     * @return
//     * @throws SerializationObjectException
//     */
//    public Serialization put(Object object) throws SerializationObjectException, IllegalAccessException {
//        //获取所有的属性。
//        Class<?> c = object.getClass();
//        //验证对象是否可以序列化。
//        if (c.getAnnotation(Attributes.class) == null) throw new SerializationObjectException("对象未标记可序列化错误！");
//        //获取对象属性列表。
//        this.calculateLength(object, c.getDeclaredFields());
//        return this;
//    }
//
//    /**
//     * 刷新数据到基础流。
//     * @param outputStream
//     * @throws IOException
//     */
//    public void flush(OutputStream outputStream) throws IOException {
//
//    }
//    /**
//     * 总长度。
//     */
//    private int count;
//    /**
//     *
//     */
//    private List<byte[]> listBytes = new ArrayList<byte[]>();
//    private byte[] bytes;
//
//    /**
//     * 计算长度。
//     *
//     * @param object
//     * @param fields
//     */
//    private void calculateLength(Object object, Field[] fields) throws SerializationObjectException, IllegalAccessException {
//        //遍历属性。
//        for (Field field : fields) {
//            Property meta = field.getAnnotation(Property.class);
//            if (meta != null) {
//                //类型长度。
//                this.count += 4;
//                //名称长度。
//                this.bytes = field.getName().getBytes();
//                this.count += (4 + bytes.length);
//                this.listBytes.add(this.bytes);
//                //类型数据长度。
//                switch (meta.type()) {
//                    case Boolean:
//                        this.count += 1;
//                        break;
//                    case Byte:
//                        this.count += 1;
//                        break;
//                    case Short:
//                        this.count += 2;
//                        break;
//                    case Int:
//                        this.count += 4;
//                        break;
//                    case Long:
//                        this.count += 8;
//                        break;
//                    case Float:
//                        this.count += 4;
//                        break;
//                    case Double:
//                        this.count += 4;
//                        break;
//                    case Date:
//                        this.count += 8;
//                        break;
//                    case String:
//                        if (field.get(object) == null) {
//                            this.count += 4;
//                        } else {
//                            byte[] bytes = field.get(object).toString().getBytes();
//                            this.count += (4 + bytes.length);
//                            this.listBytes.add(bytes);
//                        }
//                        break;
//                    case Char:
//                        this.count += 2;
//                        break;
//                    case Bytes:
//                        if (field.get(object) == null) {
//                            this.count += 4;
//                        } else {
//                            byte[] bytes = (byte[]) field.get(object);
//                            this.count += (4 + bytes.length);
//                            this.listBytes.add(bytes);
//                        }
//                        break;
//                    case Object:
//                        if (field.get(object) == null) {
//                            this.count += 4;
//                        } else {
//                            this.put(field.get(object));
//                        }
//                        break;
//                    case Order:
//                        if (field.get(object) == null) {
//                            this.count += 4;
//                        } else {
//                            this.put(field.get(object));
//                        }
//                        break;
//                }
//            }
//        }
//    }
//}
