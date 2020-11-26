package ghost.framework.web.angular1x.admin.plugin.controller.body;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
public class AdminGroupBody implements Serializable {
    private static final long serialVersionUID = 1017418538847715535L;
    @NotNull(message = "null error")
    @Length(max = 50, min = 2, message = "length error")
    private String groupName;
    public String getGroupName() {
        return groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    @Min(value = 0, message = "min error")
    @Max(value = 1, message = "max error")
    private short status;
    public short getStatus() {
        return status;
    }
    public void setStatus(short status) {
        this.status = status;
    }
}