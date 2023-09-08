package syh.toyproject.repository.post;

import syh.toyproject.Dto.post.PostSearchCond;
import syh.toyproject.domain.post.Post;

import java.util.List;

public interface PostRepository {

    Post addPost(Post post);


    List<Post> findAll(PostSearchCond cond);

    List<Post> findByMemberIdAll(Long memberId);

    Post findByPostId(Long postId);


    void editPost(Long postId, Post postEditDto);


    void deletePost(Long postId);


    void  addViewCount(Long postId);

    void recommendPost(Long postId, Long memberId);

    boolean recommendDuplicateCheck(Long postId, Long memberId);


}
