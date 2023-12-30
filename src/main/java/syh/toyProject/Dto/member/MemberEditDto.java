package syh.toyProject.Dto.member;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MemberEditDto {

    @NotBlank
    private String password;

    @NotBlank
    private String username;

    private MemberEditDto() {
    }

    private MemberEditDto(String password, String username) {
        this.password = password;
        this.username = username;
    }

    public static MemberEditDto create(String password, String username) {
        return new MemberEditDto(password, username);
    }
}
