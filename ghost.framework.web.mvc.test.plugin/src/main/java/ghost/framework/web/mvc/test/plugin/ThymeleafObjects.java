package ghost.framework.web.mvc.test.plugin;

import ghost.framework.web.context.bind.annotation.GetMapping;
import ghost.framework.web.mvc.context.bind.annotation.Controller;
import ghost.framework.web.mvc.context.bind.annotation.ModelAttribute;
import ghost.framework.web.mvc.context.servlet.support.RedirectAttributes;
import ghost.framework.web.mvc.context.ui.Model;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

// http://blog.codeleak.pl/2014/05/spring-mvc-and-thymeleaf-how-to-acess-data-from-templates.html
@Controller
public class ThymeleafObjects {

    @ModelAttribute("messages")
    List<String> messages() {
        return Arrays.asList("Message 1", "Message 2", "Message 3");
    }

    @GetMapping("/entity-attr")
    public String modelAttributes(Model model) {
        return "th-objects";
    }

    @GetMapping("/query-params")
    public String queryParams() {
        return "redirect:/entity-attr?q=My Query";
    }

    @GetMapping("/session-attr")
    public String sessionAttributes(HttpSession session) {
        session.setAttribute("mySessionAttribute", "Session Attr 1");
        return "th-objects";
    }

    @GetMapping("/flash-attr")
    public String flashAttributes(RedirectAttributes ra) {
        ra.addFlashAttribute("flash", "Flash Demo");
        return "redirect:/entity-attr";
    }
}