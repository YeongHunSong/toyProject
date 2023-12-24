package syh.toyProject.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UNUSE_Grade {

    ADMIN("관리자", 0),
    COMMON("일반회원", 1);

    // 회원 등급 작성 // 관리자 // 일반 회원 // ???

    private final String memberGrade;
    private final int gradeNumber;
}
