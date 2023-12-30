package syh.toyProject.Dto.member;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class
MemberSignupDto {

    @NotBlank
    private String loginId;

    @NotBlank
    private String password;

    @NotBlank
    private String username;

    @Email
    private String emailAddress; // 추후 이메일 검증 시 사용, 안 쓰면 걍 지우는 것도.

    private MemberSignupDto() {
    }
}
