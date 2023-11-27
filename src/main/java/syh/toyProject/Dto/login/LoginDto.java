package syh.toyProject.Dto.login;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginDto {

    @NotBlank
    private final String loginId;

    @NotBlank
    private final String password;

    public LoginDto(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
