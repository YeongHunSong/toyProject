package syh.toyproject.repository.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import syh.toyproject.domain.member.Member;
import syh.toyproject.paging.PageDto;
import syh.toyproject.paging.SortingDto;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MemberMapper {

    // 파라미터가 하나만 넘어가는 경우에는 @Param 을 작성할 필요가 없으나, 두 개 이상 넘어가야하는 경우는 다 작성해줘야 함.

    void addMember(Member member);

    void editMember(@Param("memberId") Long memberId, @Param("memberEditDto") Member memberEditDto);

    List<Member> findAll(@Param("searchUsername") String searchUsername, @Param("pageDto")PageDto pageDto,
                         @Param("sortingDto") SortingDto sortingDto);

    int totalCount(String searchUsername);

    Member findByMemberId(Long memberId);

    Optional<Member> findByLoginId(String loginId);
}
