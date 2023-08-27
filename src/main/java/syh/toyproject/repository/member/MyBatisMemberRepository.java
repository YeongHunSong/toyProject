package syh.toyproject.repository.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import syh.toyproject.domain.member.Member;
import syh.toyproject.repository.mybatis.MemberMapper;

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
    public List<Member> findAll(String searchUsername) {
        return memberMapper.findAll(searchUsername);
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
