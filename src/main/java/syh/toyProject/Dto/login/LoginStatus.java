package syh.toyProject.Dto.login;

import lombok.Data;

@Data
public class LoginStatus {

    private boolean loginStay = false;

    private boolean authority = false;

    private boolean accessDenied = false;

    private LoginStatus() {
    }

    private LoginStatus(boolean accessDenied) {
        this.accessDenied = accessDenied;
    }

    public static LoginStatus accessDenied() {
        return new LoginStatus(true);
    }
}
