package syh.toyproject.repository.member;


import syh.toyproject.domain.member.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Member addMember(Member member);


    List<Member> findAll();


    default public List<Member> findAll(String userSearchName) {
        return null; // 검색 조건 넣어서 조회하기. 좀 있다가 개발
    }

    Member findByMemberId(Long memberId);

    Optional<Member> findByLoginId(String loginId);


    void editMember(Long memberId, Member memberEditDto);

}
