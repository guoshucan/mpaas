package ghost.framework.web.mvc.nginx.ui.plugin.config;

import cn.craccd.sqlHelper.utils.SqlHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { "cn.craccd" })
public class SqlConfig  {
	@Autowired
	SqlHelper sqlHelper;


}
