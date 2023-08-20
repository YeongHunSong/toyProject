package syh.toyproject.repository.member;


import syh.toyproject.domain.member.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    public Member addMember(Member member);


    public List<Member> findAll();


    default public List<Member> findAll(String userSearchName) {
        return null; // 좀있다가 개발
    }

    public Member findByMemberId(Long memberId);

    public Optional<Member> findByLoginId(String loginId);


    public void editMember(Long memberId, Member memberEditDto);

}
