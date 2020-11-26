package ghost.framework.core.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期时间格式序列化组件
 */
public class DateTimeSerializer extends JsonSerializer<Date> {
    /**
     * 格式化日期时间。
     * @param date
     * @param generator
     * @param provider
     * @throws IOException
     */
    @Override
    public void serialize(Date date, JsonGenerator generator, SerializerProvider provider)
            throws IOException {
        if(date== null){
            generator.writeString("");
        }else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
            generator.writeString(simpleDateFormat.format(date));
        }
    }
}
