package syh.toyproject.Dto.member;

import lombok.Data;
import syh.toyproject.domain.member.Address;

import javax.validation.constraints.NotBlank;

@Data
public class MemberEditDto {

    @NotBlank
    private String password;

    @NotBlank
    private String username;

    private Address address;

    public MemberEditDto(String password, String username) {
        this.password = password;
        this.username = username;
    }
}
