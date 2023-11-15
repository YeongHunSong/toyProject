package syh.toyproject.repository.post;

import syh.toyproject.Dto.post.PostSearchCond;
import syh.toyproject.domain.post.Post;
import syh.toyproject.paging.PageDto;
import syh.toyproject.paging.SortingDto;

import java.util.List;

public interface PostRepository {

    Post addPost(Post post);

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
