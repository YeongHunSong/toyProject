package syh.toyproject.repository.member;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import syh.toyproject.Dto.member.MemberEditDto;
import syh.toyproject.domain.member.Member;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@Transactional
@SpringBootTest
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach @AfterEach
    void beforeAndAfterEach() {
        if (memberRepository instanceof MemoryMemberRepository) {
            ((MemoryMemberRepository) memberRepository).clear();
        }
    }

    @Test
    void addMemberAndFindByMemberIdTest() {
        Member member1 = newMember(1L);

        Member findMember1 = memberRepository.findByMemberId(member1.getMemberId());

        assertThat(findMember1.getLoginId()).isEqualTo("test1");
        assertThat(findMember1.getMemberId()).isEqualTo(member1.getMemberId());
        assertThat(findMember1.getPassword()).isEqualTo("pw1");
        assertThat(findMember1.getUsername()).isEqualTo("테스트1호");
        assertThat(findMember1.getSignupDate()).isEqualTo(member1.getSignupDate());
    }


    @Test
    void findAllTest() {
        Member member1 = newMember(1L);
        Member member2 = newMember(2L);

        Member findMember1 = memberRepository.findByMemberId(member1.getMemberId());
        Member findMember2 = memberRepository.findByMemberId(member2.getMemberId());

        List<Member> memberList = memberRepository.findAll(null, null);

        assertThat(memberList).containsExactly(findMember1, findMember2);
    }

    @Test
    void searchUsernameTest() {
        Member member1 = newMember(1L);
        Member member2 = newMember(2L);
        Member member3 = newMember(3L);
        Member member4 = newMember(4L);

        List<Member> findMemberList1 = memberRepository.findAll("스트2", null);
        List<Member> findMemberList2 = memberRepository.findAll(null, null);

        assertThat(findMemberList1).containsExactly(memberRepository.findByMemberId(member2.getMemberId()));
        assertThat(findMemberList2).containsExactly(member1, member2, member3, member4);
    }

    @Test
    void findByLoginIdTest() {
        Member member1 = newMember(1L);

        Member findMember1 = memberRepository.findByLoginId("test1").get();
        Optional<Member> findMember2 = memberRepository.findByLoginId("없는로그인ID");

        assertThat(findMember1.getUsername()).isEqualTo("테스트1호");
        assertThat(findMember2).isEmpty();
    }

    @Test
    void editMemberTest() {
        MemberEditDto memberEditDto = new MemberEditDto("변경이후PW",  "변경이후별칭");
        Member beforeMember = newMember(1L);

        Member memberEditTemp = new Member(memberEditDto.getPassword(), memberEditDto.getUsername());
        memberRepository.editMember(beforeMember.getMemberId(), memberEditTemp);
        Member afterMember = memberRepository.findByMemberId(beforeMember.getMemberId());

        assertThat(afterMember.getPassword()).isEqualTo("변경이후PW");
        assertThat(afterMember.getUsername()).isEqualTo("변경이후별칭");
    }

    private Member newMember(Long number) {
        return memberRepository.addMember(new Member("test" + number, "pw" + number, "테스트" + number + "호"));
    }
}
