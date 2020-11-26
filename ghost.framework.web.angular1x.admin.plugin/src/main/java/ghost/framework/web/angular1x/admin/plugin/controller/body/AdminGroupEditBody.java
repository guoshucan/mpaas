package ghost.framework.web.angular1x.admin.plugin.controller.body;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class AdminGroupEditBody extends AdminGroupBody {
    private static final long serialVersionUID = 9001123983332436991L;
    @Length(max = 36, min = 36, message = "length error")
    @NotNull
    private String groupId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}