package syh.toyProject.repository.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import syh.toyProject.domain.member.Member;
import syh.toyProject.paging.PageDto;
import syh.toyProject.paging.SortingDto;
import syh.toyProject.repository.mybatis.MemberMapper;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MyBatisMemberRepository implements MemberRepository {

    private final MemberMapper memberMapper;

    @Override
    public Member addMember(Member member) {
        memberMapper.addMember(member);

        return findByMemberId(member.getMemberId());
    }
    @Override
    public int totalCount(String searchUsername) {
        return memberMapper.totalCount(searchUsername);
    }

    @Override
    public List<Member> findAll(String searchUsername, PageDto pageDto, SortingDto sortingDto) {
        return memberMapper.findAll(searchUsername, pageDto, sortingDto);
    }


    @Override
    public Member findByMemberId(Long memberId) {
        return memberMapper.findByMemberId(memberId);
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        return memberMapper.findByLoginId(loginId);
    }

    @Override
    public void editMember(Long memberId, Member memberEditDto) {
        memberMapper.editMember(memberId, memberEditDto);
    }
}
