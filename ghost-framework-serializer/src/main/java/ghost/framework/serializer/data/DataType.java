package ghost.framework.serializer.data;//package gsc.framework.serializer.data;
//
///**
// * 数据类型枚举。
// */
//public enum DataType {
//    unknown,
//    Byte,
//    Short,
//    Int,
//    Long,
//    Double,
//    Float,
//    Boolean,
//    Date,
//    DateTime,
//    String,
//    Char,
//    UUID,
//    Bytes,
//    Object,
//    Order;
//    /**
//     * 转换枚举值。
//     *
//     * @param value
//     * @return
//     */
//    public static DataType valueOf(int value) {
//        if (value == Byte.ordinal()) return Byte;
//        if (value == Short.ordinal()) return Short;
//        if (value == Int.ordinal()) return Int;
//        if (value == Long.ordinal()) return Long;
//        if (value == Double.ordinal()) return Double;
//        if (value == Float.ordinal()) return Float;
//        if (value == Boolean.ordinal()) return Boolean;
//        if (value == Date.ordinal()) return Date;
//        if (value == DateTime.ordinal()) return DateTime;
//        if (value == String.ordinal()) return String;
//        if (value == Char.ordinal()) return Char;
//        if (value == UUID.ordinal()) return UUID;
//        if (value == Bytes.ordinal()) return Bytes;
//        if (value == Object.ordinal()) return Object;
//        if (value == Order.ordinal()) return Order;
////        if (value == Column.ordinal()) return Column;
//        return unknown;
//    }
//}