package syh.toyproject.repository.post;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import syh.toyproject.Dto.post.PostEditDto;
import syh.toyproject.Dto.post.PostSearchCond;
import syh.toyproject.domain.member.Member;
import syh.toyproject.domain.post.Post;
import syh.toyproject.repository.member.MemberRepository;
import syh.toyproject.repository.member.MemoryMemberRepository;

import java.util.List;

import static java.lang.Thread.sleep;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Transactional
@SpringBootTest
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach @AfterEach
    void beforeAndAfterEach() {
        if (postRepository instanceof MemoryMemberRepository) {
            ((MemoryMemberRepository) postRepository).clear();
        }
    }

    @Test
    void addPostAndFindByPostIdTest() {
        Post post1 = newPost(1L);

        Post findPost1 = postRepository.findByPostId(post1.getPostId());

        assertThat(findPost1.getPostId()).isEqualTo(post1.getPostId());
        assertThat(findPost1.getMemberId()).isEqualTo(post1.getMemberId());
        assertThat(findPost1.getPostTitle()).isEqualTo("생성테스트제목1");
        assertThat(findPost1.getWritingDate()).isEqualTo(post1.getWritingDate());
        assertThat(findPost1.getPostContent()).isEqualTo("생성테스트내용1");
    }

    @Test
    void findAllTest() {
        Post post1 = newPost(1L);
        Post post2 = newPost(2L);

        Post findPost1 = postRepository.findByPostId(post1.getPostId());
        Post findPost2 = postRepository.findByPostId(post2.getPostId());

        List<Post> postList = postRepository.findAll(null);

        assertThat(postList).containsExactly(findPost1, findPost2);
    }

    @Test
    void postSearchCondTest() {
        Member member = memberRepository.addMember(new Member("loginId", "pw", "검색조건테스트"));
        Post post1 = newPost(member.getMemberId());
        Post post2 = newPost(2L);

        PostSearchCond cond1 = new PostSearchCond("title", "트제목1");
        PostSearchCond cond2 = new PostSearchCond("content", "트내용2");
        PostSearchCond cond3 = new PostSearchCond("username", "검색조건");
        PostSearchCond cond4 = new PostSearchCond();

        Post findPost1 = postRepository.findByPostId(post1.getPostId());
        Post findPost2 = postRepository.findByPostId(post2.getPostId());

        List<Post> findPostTitle1 = postRepository.findAll(cond1);
        List<Post> findPostTitle2 = postRepository.findAll(cond2);
        List<Post> findPostTitle3 = postRepository.findAll(cond3);
        List<Post> findPostTitle4 = postRepository.findAll(cond4);

        assertThat(findPostTitle1).containsExactly(findPost1);
        assertThat(findPostTitle2).containsExactly(findPost2);
        assertThat(findPostTitle3).containsExactly(findPost1);
        assertThat(findPostTitle4).containsExactly(findPost1, findPost2);
    }

    @Test
    void findByMemberIdAllTest() {
        Post post1 = newPost(3L);
        Post post2 = newPost(3L);
        Post post3 = newPost(3L);

        Post findPost1 = postRepository.findByPostId(post1.getPostId());
        Post findPost2 = postRepository.findByPostId(post2.getPostId());
        Post findPost3 = postRepository.findByPostId(post3.getPostId());

        List<Post> postList1 = postRepository.findByMemberIdAll(3L);
        List<Post> postList2 = postRepository.findByMemberIdAll(1000L); // 없는 값

        assertThat(postList1).containsExactly(findPost1, findPost2, findPost3);
        assertThat(postList2).isEmpty();
    }

    @Test
    void editPostTest() throws InterruptedException {
        PostEditDto postEditDto = new PostEditDto(null, "변경이후제목", "변경이후내용");
        Post beforePost = newPost(1L);

        Post postEditTemp = new Post(postEditDto.getCategory(), postEditDto.getPostTitle(), postEditDto.getPostContent());
        sleep(1); // 같은 트랜잭션 내라 now() 호출 시기가 달라져도 같은 값을 return 함. (mySQL 을 사용할 경우, SYSDATE() 로 바꾸자)
        postRepository.editPost(beforePost.getPostId(), postEditTemp);
        Post afterPost = postRepository.findByPostId(beforePost.getPostId());

        assertThat(afterPost.getPostId()).isEqualTo(beforePost.getPostId());
        assertThat(afterPost.getMemberId()).isEqualTo(beforePost.getMemberId());
        assertThat(afterPost.getPostTitle()).isEqualTo("변경이후제목");
        assertThat(afterPost.getPostContent()).isEqualTo("변경이후내용");
        assertThat(afterPost.getLastEditDate()).isNotEqualTo(beforePost.getWritingDate());
    }

    @Test
    void deletePostTest() {
        Post post1 = newPost(1L);

        postRepository.deletePost(post1.getPostId());

        Post findPost = postRepository.findByPostId(post1.getPostId());

        assertThat(findPost).isNull();
    }

    @Test
    void addViewCountTest() {
        Post post1 = newPost(1L);

        postRepository.addViewCount(post1.getPostId());
        postRepository.addViewCount(post1.getPostId());

        Post findPost = postRepository.findByPostId(post1.getPostId());

        assertThat(findPost.getViewCount()).isEqualTo(2L);
    }

    @Test
    void recommendPostTest() {
        Post post1 = newPost(1L);

        postRepository.recommendPost(post1.getPostId(), 1L);

        Post findPost = postRepository.findByPostId(post1.getPostId());

        assertThat(findPost.getRecommendCount()).isEqualTo(1L);
    }

    @Test
    void recommendDuplicateCheckTest() {
        Post post1 = newPost(1L);

        postRepository.recommendPost(post1.getPostId(), 1L);

        boolean duplicateCheck = postRepository.recommendDuplicateCheck(post1.getPostId(), 1L);

        assertThat(duplicateCheck).isTrue();
    }


    private Post newPost(Long number) {
        return postRepository.addPost(new Post(number, "생성테스트제목" + number, "생성테스트내용" + number));
    }
}