package ghost.framework.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期格式序列化组件
 */
public class DateSerializer extends JsonSerializer<Date> {
	/**
	 * 格式化时间。
	 *
	 * @param date
	 * @param generator
	 * @param provider
	 * @throws IOException
	 * @throws JsonProcessingException
	 */
	@Override
	public void serialize(Date date, JsonGenerator generator, SerializerProvider provider)
			throws IOException {
		if (date == null) {
			generator.writeString("");
		} else {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
			generator.writeString(simpleDateFormat.format(date));
		}
	}
}