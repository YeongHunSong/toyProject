package syh.toyproject.repository.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import syh.toyproject.domain.post.Post;
import syh.toyproject.repository.mybatis.PostMapper;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MyBatisPostRepository implements PostRepository {

    private final PostMapper postMapper;


    @Override
    public Post addPost(Post post) {
        postMapper.addPost(post);

        return findByPostId(post.getPostId());
    }

    @Override
    public List<Post> findAll() {
        return postMapper.findAll();
    }

    @Override
    public List<Post> findByMemberIdAll(Long memberId) {
        return postMapper.findByMemberIdAll(memberId);
    }

    @Override
    public Post findByPostId(Long postId) {
        return postMapper.findByPostId(postId);
    }

    @Override
    public void editPost(Long postId, Post postEditDto) {
        postMapper.editPost(postId, postEditDto);
    }

    @Override
    public void deletePost(Long postId) {
        postMapper.deletePost(postId);
    }

    @Override
    public void addViewCount(Long postId) {
        postMapper.addViewCount(postId);
    }

    @Override
    public void recommendPost(Long postId, Long memberId) {
        postMapper.recommendPost(postId, memberId);
    }

    @Override
    public boolean recommendDuplicateCheck(Long postId, Long memberId) {
        return postMapper.recommendDuplicateCheck(postId, memberId) != 0;
    }


}
