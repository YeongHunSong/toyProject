package syh.toyProject.Dto.login;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginDto {

    @NotBlank
    private final String loginId;

    @NotBlank
    private final String password;
}
