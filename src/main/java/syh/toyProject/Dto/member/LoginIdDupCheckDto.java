package syh.toyProject.Dto.member;

import lombok.Data;

@Data
public class LoginIdDupCheckDto {

    private String duplicateCheckId;

    public LoginIdDupCheckDto(String duplicateCheckId) {
        this.duplicateCheckId = duplicateCheckId;
    }
}
