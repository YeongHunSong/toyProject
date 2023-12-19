package syh.toyProject.repository.member;


import lombok.extern.slf4j.Slf4j;
import syh.toyProject.domain.member.Member;
import syh.toyProject.paging.PageDto;
import syh.toyProject.paging.SortingDto;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
//@Repository
public class MemoryMemberRepository implements MemberRepository {

    private static final Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public Member addMember(Member member) {
        member.setMemberId(++sequence);
        member.setSignupDate(LocalDateTime.now());
        store.put(member.getMemberId(), member);
        return member;
    }


    @Override
    public Member findByMemberId(Long memberId) {
        return store.get(memberId);
    }

    @Override
    public List<Member> findAll(String searchUsername, PageDto pageDto, SortingDto sortingDto) {
        return new ArrayList<>(store.values());
    }

    @Override
    public int totalCount(String searchUsername) {
        return 0;
    }

//    @Override
//    public Member findByLoginId(String loginId) {
//        return findAll().stream().filter(member -> member.getLoginId().equals(loginId))
//                .findFirst().orElseThrow(() -> new RuntimeException("해당 아이디는 존재하지 않습니다."));
//    }

    @Override
    public Optional<Member> findByLoginId(String loginId) { // Optional 추가
        return findAll(null, null, null).stream()
                .filter(member -> member.getLoginId().equals(loginId))
                .findFirst();
    }

    @Override
    public void editMember(Long memberId, Member memberEditDto) {
        Member editMember = findByMemberId(memberId);
        editMember.setPassword(memberEditDto.getPassword());
        editMember.setUsername(memberEditDto.getUsername());
        editMember.setAddress(memberEditDto.getAddress());
    }

    public void clear() {
        sequence = 0;
        store.clear();
    }
}
