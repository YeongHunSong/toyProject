package syh.toyProject.repository.post;

import syh.toyProject.Dto.post.PostSearchCond;
import syh.toyProject.domain.post.Post;
import syh.toyProject.paging.PageDto;
import syh.toyProject.paging.SortingDto;

import java.util.List;

public interface PostRepository {

    Long addPost(Post post);

    int totalCount(PostSearchCond cond);

    int totalCountByMemberId(Long memberId);

    List<Post> findAll(PostSearchCond cond, PageDto pageDto, SortingDto sortingDto);

    List<Post> findByMemberIdAll(Long memberId, PageDto pageDto);

    Post findByPostId(Long postId);


    void editPost(Long postId, Post postEditDto);


    void deletePost(Long postId);


    void  addViewCount(Long postId);

    void recommendPost(Long postId, Long memberId);

    boolean recommendDuplicateCheck(Long postId, Long memberId);


}
