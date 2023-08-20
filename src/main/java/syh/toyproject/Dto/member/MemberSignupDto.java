package syh.toyproject.Dto.member;

import lombok.Data;
import syh.toyproject.domain.member.Address;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class MemberSignupDto {

    @NotBlank
    private String loginId;

    @NotBlank
    private String password;

    @NotBlank
    private String username;

    @Email
    private String emailAddress; // 추후 이메일 검증 시 사용, 안 쓰면 걍 지우는 것도.

    private Address address;

    public MemberSignupDto(String loginId, String password, String username) {
        this.loginId = loginId;
        this.password = password;
        this.username = username;
    }
}
