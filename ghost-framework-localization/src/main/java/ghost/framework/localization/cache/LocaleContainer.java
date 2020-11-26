package ghost.framework.localization.cache;//package ghost.framework.language;
//
//import ghost.framework.module.reflect.annotations.InterfaceId;
//import ghost.framework.thread.TwoRunnable;
//import java.text.DateFormat;
//import java.text.MessageFormat;
//import java.text.NumberFormat;
//import java.util.Date;
//import java.util.Locale;
//
///**
// * @Author: 郭树灿{guoshucan-pc}
// * @link: 手机:13715848993, QQ 27048384
// * @Description:本地化容器。
// * @Date: 23:47 2018-09-08
// */
//public final class LocaleContainer {
//    /**
//     * 本地化
//     */
//    private Locale locale;
//    /**
//     * 本地化监听事件。
//     */
//    private LocaleChangeListener localeChangeListener = new LocaleChangeListener();
//
//    /**
//     * 初始化本地化容器。
//     * @param locale
//     */
//    public LocaleContainer(Locale locale) {
//        this.locale = locale;
//    }
//
//    /**
//     * 注册本地化更改事件。
//     * @param event 事件对象
//     */
//    public synchronized void register(ILocaleEvent event) {
//        try {
//            if (event.getClass().isAnnotationPresent(InterfaceId.class)) {
//                this.localeChangeListener.put(event.getClass().getAnnotation(InterfaceId.class).id(), event);
//                return;
//            }
//        } catch (NullPointerException e) {
//
//        }
//        this.localeChangeListener.put(event.getClass().getName(), event);
//    }
//
//    /**
//     * 注册本地化更改事件。
//     * @param key 事件键
//     * @param event 事件对象
//     */
//    public synchronized void register(String key, ILocaleEvent event) {
//        this.localeChangeListener.put(key, event);
//    }
//
//    /**
//     * 注册本地化更改事件。
//     * @param id 事件id
//     * @param event 事件对象
//     */
//    public synchronized void register(InterfaceId id, ILocaleEvent event) {
//        this.localeChangeListener.put(id.id(), event);
//    }
//
//    /**
//     * 卸载本地化更改事件。
//     * @param id 事件id
//     */
//    public synchronized void unregister(InterfaceId id) {
//        this.localeChangeListener.remove(id.id());
//    }
//
//    /**
//     * 卸载本地化更改事件。
//     * @param key 事件键
//     */
//    public synchronized void unregister(String key) {
//        this.localeChangeListener.remove(key);
//    }
//
//    /**
//     * 卸载本地化更改事件。
//     * @param event 事件对象
//     */
//    public synchronized void unregister(ILocaleEvent event) {
//        try {
//            if (event.getClass().isAnnotationPresent(InterfaceId.class)) {
//                this.localeChangeListener.remove(event.getClass().getAnnotation(InterfaceId.class).id());
//                return;
//            }
//        } catch (NullPointerException e) {
//
//        }
//        this.localeChangeListener.remove(event.getClass().getName());
//    }
//
//    /**
//     * 设置本地化。
//     * @param locale 本地化对象
//     * @throws LocaleException
//     */
//    public synchronized void setLocale(Locale locale) {
//        this.locale = locale;
//        //通知本地化更改。
//        synchronized (this.localeChangeListener) {
//            for (ILocaleEvent event : this.localeChangeListener.values()) {
//                new Thread(new TwoRunnable<ILocaleEvent, Locale>(event, this.locale) {
//                    @Override
//                    public void run() {
//                        try {
//                            this.getA().change(this.getB());
//                        } catch (Exception e) {
//                            throw e;
//                        }
//                    }
//                }).start();
//            }
//        }
//    }
//
//    /**
//     * 获取本地化对象。
//     * @return
//     */
//    public Locale getLocale() {
//        return locale;
//    }
//    /**
//     * 获取本地化格式时间对象。
//     *
//     * @param style 设置格式样式。
//     * @return
//     */
//    public DateFormat getTimeInstance(int style) {
//        return DateFormat.getTimeInstance(style, this.locale);
//    }
//
//    public DateFormat getDateInstance(int style) {
//        return DateFormat.getDateInstance(style, this.locale);
//    }
//
//    public String getDateTime(Date date) {
//        return DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, this.locale).format(date);
//    }
//
//    public DateFormat getDateTimeInstance() {
//        return DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, this.locale);
//    }
//
//    public DateFormat getDateTimeInstance(int dateStyle, int timeStyle) {
//        return DateFormat.getDateTimeInstance(dateStyle, timeStyle, this.locale);
//    }
//
//    public MessageFormat getMessageFormat(String pattern) {
//        return new MessageFormat(pattern, this.locale);
//    }
//
//    /**
//     * 获取格式化数字。
//     *
//     * @return
//     */
//    public NumberFormat getNumberFormat() {
//        return NumberFormat.getCurrencyInstance(this.locale);
//    }
//}
