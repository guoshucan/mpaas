package ghost.framework.web.mvc.nginx.ui.plugin.config;

//
//@Configuration
//public class FreeMarkerConfig {
//
//	@Bean
//	public FreeMarkerConfigurer freeMarkerConfigurer() throws IOException, TemplateException {
//		FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
//		freeMarkerConfigurer.setTemplateLoaderPath("classpath:templates/");
//
//		freemarker.template.Configuration configuration = freeMarkerConfigurer.createConfiguration();
//		configuration.setDefaultEncoding("UTF-8");
//		configuration.setSetting("classic_compatible", "true");// 使用经典语法
//		configuration.setSetting("number_format", "0.##");
//
//		freeMarkerConfigurer.setConfiguration(configuration);
//		return freeMarkerConfigurer;
//	}
//
//	@Bean
//	public FreeMarkerViewResolver freeMarkerViewResolver() {
//		FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
//		resolver.setPrefix("");
//		resolver.setSuffix(".html");
//		resolver.setContentType("text/html; charset=UTF-8");
//		resolver.setRequestContextAttribute("request"); // 将上下文路径注入request变量
//
//		return resolver;
//
//	}
//}
