package ghost.framework.core.jackson;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * package: ghost.framework.core.jackson
 *
 * @Author: 郭树灿{guo-w541}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:jackson日期时间格式化扩展
 * @Date: 2020/4/4:18:30
 */
public class ObjectMapperDateFormatExtend extends DateFormat {

    private static final long serialVersionUID = 2198468413604845844L;
    /**
     * 默认格式化器
     */
    private DateFormat dateFormat;
    /**
     * 兼容格式列表
     */
    private SimpleDateFormat[] format = new SimpleDateFormat[] {
            new SimpleDateFormat("yyy-MM-dd HH:mm:ss"),
            new SimpleDateFormat("yyy/MM/dd HH:mm:ss"),
            new SimpleDateFormat("yyy-MM-dd"),
            new SimpleDateFormat("yyy/MM/dd")
    };

    /**
     * 初始化jackson日期时间格式化扩展
     * @param dateFormat 默认的dateformat
     */
    public ObjectMapperDateFormatExtend(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    //序列化时会执行这个方法
    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        return dateFormat.format(date, toAppendTo, fieldPosition);
    }

    //反序列化时执行此方法，我们先让他执行我们自己的format，如果异常则执执行他的
    //当然这里只是简单实现，可以有更优雅的方式来处理更多的格式
    @Override
    public Date parse(String source, ParsePosition pos) {
        Date date = null;
        for (SimpleDateFormat f : format) {
            try {
                date = f.parse(source, pos);
                if (date != null) {
                    break;
                }
            } catch (Exception e) {

            }
        }
        if (date == null) {
            date = dateFormat.parse(source, pos);
        }
        return date;
    }

    //此方法在objectmapper 默认的dateformat里边用到，这里也要重写
    @Override
    public Object clone() {
        DateFormat dateFormat = (DateFormat) this.dateFormat.clone();
        return new ObjectMapperDateFormatExtend(dateFormat);
    }
}