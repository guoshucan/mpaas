package ghost.framework.localization;//package ghost.framework.language;
//import java.text.DateFormat;
//import java.text.MessageFormat;
//import java.text.NumberFormat;
//import java.util.Date;
//import java.util.Locale;
//
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:本地化线程。
// * @Date: 7:22 2018/5/28
// */
//public class LocalThread {
//    private Locale locale;
//
//    public LocalThread(Locale locale) {
//        this.locale = locale;
//    }
//
//    /**
//     * 获取本地化。
//     *
//     * @return
//     */
//    public Locale getLocale() {
//        return locale;
//    }
//
//    /**
//     * 默认本地化区域。
//     */
//    private String language = "zh-CN";
//
//    public String getLanguage() {
//        return language;
//    }
//
//    public void setLanguage(String language) {
//        this.language = language;
//        this.load();
//    }
//
//    /**
//     * 创建本地化线程。
//     */
//    public LocalThread() {
//        this.load();
//    }
//
//    /**
//     * 加载本地化。
//     */
//    private void load() {
//        String[] lans = null;
//        if (this.language.indexOf("-") != -1)
//            lans = this.language.split("-");
//        if (this.language.indexOf("_") != -1)
//            lans = this.language.split("_");
//        this.locale = new Locale(lans[0], lans[1]);
//        local.set(this);
//    }
//
//    /**
//     * 创建本地化线程。
//     *
//     * @param language 设置语言，默认zh-CN。
//     */
//    public LocalThread(String language) {
//        this.language = language;
//        this.load();
//    }
//
//    private final static ThreadLocal<LocalThread> local = new ThreadLocal<LocalThread>();
//
//    @Override
//    protected void finalize() throws Throwable {
//        local.remove();
//        super.finalize();
//    }
//
//    /**
//     * 获取本地化线程。
//     *
//     * @return
//     */
//    public static LocalThread getLocal() {
//        return local.get();
//    }
//
//    /**
//     * 获取本地化格式时间对象。
//     *
//     * @param style 设置格式样式。
//     * @return
//     */
//    public static DateFormat getTimeInstance(int style) {
//        return DateFormat.getTimeInstance(style, LocalThread.getLocal().locale);
//    }
//
//    public static DateFormat getDateInstance(int style) {
//        return DateFormat.getDateInstance(style, LocalThread.getLocal().locale);
//    }
//
//    public static String getDateTime(Date date) {
//        return DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, LocalThread.getLocal().locale).format(date);
//    }
//
//    public static DateFormat getDateTimeInstance() {
//        return DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, LocalThread.getLocal().locale);
//    }
//
//    public static DateFormat getDateTimeInstance(int dateStyle, int timeStyle) {
//        return DateFormat.getDateTimeInstance(dateStyle, timeStyle, LocalThread.getLocal().locale);
//    }
//
//    public static MessageFormat getMessageFormat(String pattern) {
//        return new MessageFormat(pattern, LocalThread.getLocal().locale);
//    }
//
//    /**
//     * 获取格式化数字。
//     *
//     * @return
//     */
//    public static NumberFormat getNumberFormat() {
//        return NumberFormat.getCurrencyInstance(LocalThread.getLocal().locale);
//    }
//
//    public void setLocale(Locale locale) {
//        this.locale = locale;
//    }
//    /**
//     * 使用默认资源文件前缀{resource}获取。
//     *
//     * @return
//     */
////    public static ResourceBundle getResourceBundle(ClassLoader loader) {
////        return ResourceBundle.getBundle("resource", LocalThread.getLocal().locale, loader);
////    }
//}