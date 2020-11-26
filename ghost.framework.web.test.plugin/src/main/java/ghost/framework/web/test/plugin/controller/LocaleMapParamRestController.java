package ghost.framework.web.test.plugin.controller;

import ghost.framework.beans.annotation.application.Application;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.context.converter.json.JsonConverterContainer;
import ghost.framework.context.converter.json.MapToJsonConverter;
import ghost.framework.web.context.bind.annotation.LocaleMapParam;
import ghost.framework.web.context.bind.annotation.RequestMapping;
import ghost.framework.web.context.bind.annotation.RestController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.Map;

/**
 * package: ghost.framework.web.test.plugin.controller
 *
 * @Author: 郭树灿{gsc-e590}
 * @link: 手机:13715848993, QQ 27048384
 * @Description:
 * {@link LocaleMapParam}
 * @Date: 2020/6/18:12:41
 */
@RestController("/param")
public class LocaleMapParamRestController {
    private Log log = LogFactory.getLog(LocaleMapParamRestController.class);

    @RequestMapping
    public String get(@LocaleMapParam Map map, @Application @Autowired JsonConverterContainer container) throws IOException {
        MapToJsonConverter converter = (MapToJsonConverter) container.getConverter(MapToJsonConverter.class);
        return converter.toString(map);
    }
}