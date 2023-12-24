package syh.toyProject.Dto.login;

import lombok.Data;
import syh.toyProject.domain.member.Member;

import java.time.LocalDateTime;

@Data
public class LoginToMainDto {

    private String username;

    private Long memberId;

    private String loginId;

    private LocalDateTime signupDate;

    private LoginToMainDto(Member member) {
        this.username = member.getUsername();
        this.loginId = member.getLoginId();
        this.memberId = member.getMemberId();
        this.signupDate = member.getSignupDate();
    }

    public static LoginToMainDto create(Member member) {
        return new LoginToMainDto(member);
    }
}
