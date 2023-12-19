package syh.toyProject.service.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import syh.toyProject.Dto.post.PostBoardDto;
import syh.toyProject.Dto.post.PostSearchCond;
import syh.toyProject.domain.post.Post;
import syh.toyProject.paging.PageDto;
import syh.toyProject.paging.SortingDto;
import syh.toyProject.repository.comment.CommentRepository;
import syh.toyProject.repository.member.MemberRepository;
import syh.toyProject.repository.post.PostRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    public Long addPost(Post post) {
        return postRepository.addPost(post);
    }

    public int totalCount(PostSearchCond cond) {
        return postRepository.totalCount(cond);
    }

    public int totalCountByMemberId(Long memberId) {
        return postRepository.totalCountByMemberId(memberId);
    }

    public List<Post> findAll(PostSearchCond cond, PageDto pageDto, SortingDto sortingDto) {
        return postRepository.findAll(cond, pageDto, sortingDto);
    }

    public List<Post> findByMemberIdAll(Long memberId, PageDto pageDto) {
        return postRepository.findByMemberIdAll(memberId, pageDto);
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
        postRepository.recommendPost(postId, memberId);
    }

    public boolean recommendDuplicateCheck(Long postId, Long memberId) {
        return postRepository.recommendDuplicateCheck(postId, memberId);
    }

    public void deletePost(Long postId) {
        postRepository.deletePost(postId);
        commentRepository.deleteAllCommentByPostId(postId);
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
