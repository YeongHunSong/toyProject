package syh.toyproject.repository.post;

import syh.toyproject.domain.post.Post;

import java.util.List;

public interface PostRepository {

    public Post addPost(Post post);


    public List<Post> findAll();

    public List<Post> findByMemberIdAll(Long memberId);

    public Post findByPostId(Long postId);


    public void editPost(Long postId, Post postEditDto);


    public void deletePost(Long postId);


    public void  addViewCount(Long postId);

    public void recommendPost(Long postId, Long memberId);

    public Long recommendCounting(Long postId);

    public boolean recommendDuplicateCheck(Long postId, Long memberId);
}
