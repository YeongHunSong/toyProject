package syh.toyproject.service.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import syh.toyproject.Dto.login.LoginToMainDto;
import syh.toyproject.domain.member.AuthMember;
import syh.toyproject.domain.member.Member;
import syh.toyproject.repository.member.MemberRepository;

@Service
@RequiredArgsConstructor
public class LoginService {


    private final MemberRepository memberRepository;

    public Member login(String loginId, String password) {
        // 아이디 비밀번호 통합
        return memberRepository.findByLoginId(loginId)
                .filter(member -> member.getLoginId().equals(loginId))
                .filter(member -> member.getPassword().equals(password))
                .orElse( null);
    }

    public LoginToMainDto sessionMemberIdToLoginDto(Long sessionMemberId) {
        return new LoginToMainDto(memberRepository.findByMemberId(sessionMemberId));
    }

    public boolean memberCheck(Long loginMemberId) { // 세션의 memberId 가 회원 목록에 있는지 확인
        return memberRepository.findAll(null, null, null).stream()
                .anyMatch(member -> member.getMemberId().equals(loginMemberId));
    }

    public boolean sessionAndMemberCheck(Long loginMemberId) {
        if (loginMemberId != null) { // 로그인 세션이 있는지 확인
            return memberCheck(loginMemberId);
        }
        return false; // 세션 없음
    }

    public boolean adminMemberCheck(Long loginMemberId) {
        return loginMemberId.equals(AuthMember.ADMIN_MEMBER_ID);
    }

    public boolean authMemberCheck(Long sessionMemberId, Long authMemberId) {
        return sessionMemberId.equals(authMemberId);
    }

    public boolean authAndAdminCheck(Long loginMemberId, Long authId) {
        if (sessionAndMemberCheck(loginMemberId)) { // 세션 있고, 회원도 맞음
            if (authMemberCheck(loginMemberId, authId) || adminMemberCheck(loginMemberId)) {
                return true; // 권한 회원 혹은 관리자
            }
        }
        return false; // 세션 없음 or 회원 아님
    }

//    public boolean commentAuthAndAdminCheck(Long loginMemberId, Long postId) {
//        if (loginMemberId.equals(ADMIN_MEMBER_ID)) {
//            return true;
//        }
//        else {
//            commentRepository.findByPostIdAll(postId).stream()
//                    .filter(comment -> comment.getMemberId().equals(loginMemberId))
//                    .
//
//        }
//    }
}
