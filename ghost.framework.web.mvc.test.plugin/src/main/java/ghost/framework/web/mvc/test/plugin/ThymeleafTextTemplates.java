package ghost.framework.web.mvc.test.plugin;

import ghost.framework.beans.annotation.injection.Autowired;
import ghost.framework.web.context.bind.annotation.GetMapping;
import ghost.framework.web.context.bind.annotation.PostMapping;
import ghost.framework.web.mvc.context.bind.annotation.Controller;
import ghost.framework.web.mvc.context.bind.annotation.ModelAttribute;
import ghost.framework.web.mvc.context.ui.Model;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * Using text templates with Thymeleaf.
 * <p>
 * See http://blog.codeleak.pl/2017/03/getting-started-with-thymeleaf-3-text.html
 */
@Controller(value = "/mvc")
public class ThymeleafTextTemplates {

    private TemplateEngine textTemplateEngine;

    public ThymeleafTextTemplates(@Autowired TemplateEngine textTemplateEngine) {
        this.textTemplateEngine = textTemplateEngine;
    }
    @GetMapping("/form1")
    public String form1(Model model) {
//        entity.addAttribute(new Form());
        return "th-form1";
    }
    @GetMapping("/form")
    public String form(Model model) {
//        entity.addAttribute(new Form());
        return "th-form";
    }

    @PostMapping("/form")
    public String postForm(@ModelAttribute Form form, Model model) {

        Context context = new Context();
        context.setVariable("name", form.getName());
        context.setVariable("url", form.getUrl());
        context.setVariable("tags", form.getTags().split(" "));

        String text = textTemplateEngine.process("text-template", context);

        model.addAttribute("text", text);

        return "th-form";
    }



    public static class Form {
        private String name = "spring.io";
        private String url = "http://spring.io";
        private String tags = "#spring #framework #java";

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }
    }

}
