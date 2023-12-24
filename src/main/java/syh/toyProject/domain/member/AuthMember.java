package syh.toyProject.domain.member;

import lombok.Data;

@Data
public class AuthMember {

    private Long authMemberId = null;

    public static final Long ADMIN_MEMBER_ID = 1L; // 관리자 MEMBER ID
}
