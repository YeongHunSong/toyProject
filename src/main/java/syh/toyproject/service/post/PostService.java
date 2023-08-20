package syh.toyproject.service.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import syh.toyproject.domain.post.Post;
import syh.toyproject.Dto.post.PostBoardDto;
import syh.toyproject.repository.post.PostRepository;
import syh.toyproject.repository.member.MemberRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public Post addPost(Post post) {
        return postRepository.addPost(post);
    }


    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public List<Post> findByMemberIdAll(Long memberId) {
        return postRepository.findByMemberIdAll(memberId);
    }

    public Post findByPostId(Long postId) {
        return postRepository.findByPostId(postId);
    }

    public Long findByMemberId(Long postId) {
        return postRepository.findByPostId(postId).getMemberId();
    }

    public void editPost(Long postId, Post postEditDto) {
        postRepository.editPost(postId, postEditDto);
    }

    public void addViewCount(Long postId) {
        postRepository.addViewCount(postId);
    }

    public void recommendPost(Long postId, Long memberId) {
        log.info("개추");
        postRepository.recommendPost(postId, memberId);
    }

    public boolean recommendDuplicateCheck(Long postId, Long memberId) {
        return postRepository.recommendDuplicateCheck(postId, memberId);
    }

    public void deletePost(Long postId) {
        postRepository.deletePost(postId);
    }





    public List<PostBoardDto> postListToPostDto(List<Post> postList) {
        return postList.stream().map(this::postToPostDto)
                .collect(Collectors.toList());
    }

    public PostBoardDto postToPostDto(Post post) {
        return new PostBoardDto(post, findUsername(post.getMemberId()));
    }


    private String findUsername(Long memberId) {
        return memberRepository.findByMemberId(memberId).getUsername();
    }
}
