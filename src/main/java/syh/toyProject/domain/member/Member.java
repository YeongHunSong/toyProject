package syh.toyProject.domain.member;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Member {
    // ConcurrentHashMap, AtomicLong 고려

    // 자동 생성 값
    private Long memberId; // DB 조회용 ID값. DB 자동 생성
    private LocalDateTime signupDate; // 회원가입 날짜 및 시간 (yy-MM-dd HH:mm:ss)
    // lastEditDate 도 추가할지 고민

    private String loginId;

    private String password;

    private String username;

    private String emailAddress; // 이메일 검증을 추가한다면 사용합시다

    // 주소값은 주소 API 입력을 통해 받도록 설정
    private Address address;

    private Member() { // DB용
    }

    public Member(String password, String username) {
        this.password = password;
        this.username = username;
    }

    public Member(String loginId, String password, String username) {
        this.loginId = loginId;
        this.password = password;
        this.username = username;
    }
}
