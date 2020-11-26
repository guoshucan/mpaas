package ghost.framework.localization;//package ghost.framework.language;
//
//import java.text.SimpleDateFormat;
//
///**
// * 本地化日期时间转换器。
// */
//public class LocalDateFormat {
//    /**
//     * 默认本地化时间区域。
//     */
//    private String lan = "zh-cn";
//
//    /**
//     * 初始化本地化日期时间转换器。
//     */
//    public LocalDateFormat() {
//        this.dateTime = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
//        this.date = new SimpleDateFormat("yyyy年MM月dd日");
//        local.set(this);
//    }
//
//    /**
//     * 初始化本地化日期时间转换器。
//     * @param lan 设置时间区域，默认为zh-cn。
//     */
//    public LocalDateFormat(String lan) {
//        this.lan = lan;
//        if (this.lan.equals("zh-cn")) {
//            this.dateTime = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
//            this.date = new SimpleDateFormat("yyyy年MM月dd日");
//        }
//        local.set(this);
//    }
//    @Override
//    protected void finalize() throws Throwable {
//        local.remove();
//        super.finalize();
//    }
//
//    /**
//     * 图片地址。
//     */
//    private final static ThreadLocal<LocalDateFormat> local = new ThreadLocal<LocalDateFormat>();
//
//    /**
//     * 获取本地化时间。
//     * @return
//     */
//    public static LocalDateFormat getLocal() {
//        return local.get();
//    }
//    private SimpleDateFormat date;
//
//    /**
//     * 获取格式化日期。
//     * @return
//     */
//    public SimpleDateFormat getDate() {
//        return date;
//    }
//
//    private SimpleDateFormat dateTime;
//
//    /**
//     * 获取格式化日期时间。
//     * @return
//     */
//    public SimpleDateFormat getDateTime() {
//        return dateTime;
//    }
//}
