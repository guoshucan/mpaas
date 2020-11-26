package ghost.framework.web.mvc.nginx.ui.plugin.utils;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.beans.annotation.stereotype.Component;
import ghost.framework.web.mvc.nginx.ui.plugin.service.SettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class SendMailUtils {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	SettingService settingService;

	public void sendMailSmtp(String to, String title, String msg) {

		MailAccount account = new MailAccount();
		account.setHost(settingService.get("mail_host"));
		if (settingService.get("mail_port") != null) {
			account.setPort(Integer.parseInt(settingService.get("mail_port")));
		}
		account.setAuth(true);
		account.setFrom(settingService.get("mail_from"));
		account.setUser(settingService.get("mail_user"));
		account.setPass(settingService.get("mail_pass"));
		if (settingService.get("mail_ssl") != null) {
			account.setSslEnable(Boolean.parseBoolean(settingService.get("mail_ssl")));
		}

		MailUtil.send(account, to, title, msg, false); 
		logger.info("发送邮件: " + to);
	}
}
