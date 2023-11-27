package syh.toyProject.repository.member;


import syh.toyProject.domain.member.Member;
import syh.toyProject.paging.PageDto;
import syh.toyProject.paging.SortingDto;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Member addMember(Member member);


    List<Member> findAll(String searchUsername, PageDto pageDto, SortingDto sortingDto);

    int totalCount(String searchUsername);

    Member findByMemberId(Long memberId);

    Optional<Member> findByLoginId(String loginId);


    void editMember(Long memberId, Member memberEditDto);

}
