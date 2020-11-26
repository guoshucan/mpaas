package ghost.framework.web.angular1x.admin.plugin.controller.body;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
public class AdminEditBody extends AdminBody {
    private static final long serialVersionUID = 3638047436040966053L;
    @Length(max = 36, min = 36, message = "length error")
    @NotNull
    private String adminId;
    public String getAdminId() {
        return adminId;
    }
    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }
}