package syh.toyproject.Dto.login;

import lombok.Data;

@Data
public class LoginStatus {

    private boolean loginStay = false;

    private boolean authority = false;

    private boolean accessDenied = false;

    public LoginStatus() {
    }

    public LoginStatus(boolean accessDenied) {
        this.accessDenied = accessDenied;
    }
}
