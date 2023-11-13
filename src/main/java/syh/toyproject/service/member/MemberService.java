package syh.toyproject.service.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import syh.toyproject.domain.member.Member;
import syh.toyproject.paging.PageDto;
import syh.toyproject.repository.member.MemberRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member addMember(Member member) {
        return memberRepository.addMember(member);
    }

    public int totalCount(String searchUsername) {
        return memberRepository.totalCount(searchUsername);
    }

    public List<Member> findAll(String searchUsername, PageDto pageDto) {
        return memberRepository.findAll(searchUsername, pageDto);
    }

    public Member findByMemberId(Long memberId) {
        return memberRepository.findByMemberId(memberId);
    }

    public String findByUsername(Long memberId) {
        return memberRepository.findByMemberId(memberId).getUsername();
    }

    public boolean loginIdDuplicateCheck(String dupCheckId) {
       return memberRepository.findAll(null, null).stream()
                .anyMatch(member -> member.getLoginId().equals(dupCheckId));
    }

    public boolean usernameDuplicateCheck(String username) {
        return memberRepository.findAll(null, null).stream()
                .anyMatch(member -> member.getUsername().equals(username));
    }

    public void editMember(Long memberId, Member memberUpdateDto) {
        memberRepository.editMember(memberId, memberUpdateDto);
    }
}
