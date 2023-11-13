package syh.toyproject.repository.member;


import syh.toyproject.domain.member.Member;
import syh.toyproject.paging.PageDto;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Member addMember(Member member);


    List<Member> findAll(String searchUsername, PageDto pageDto);

    int totalCount(String searchUsername);

    Member findByMemberId(Long memberId);

    Optional<Member> findByLoginId(String loginId);


    void editMember(Long memberId, Member memberEditDto);

}
