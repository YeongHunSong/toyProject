package syh.toyproject.repository.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import syh.toyproject.domain.post.Post;

import java.util.List;

@Mapper
public interface PostMapper {

    void addPost(Post post);

    void editPost(@Param("postId") Long postId, @Param("postEditDto") Post postEditDto);

    List<Post> findAll();

    List<Post> findByMemberIdAll(Long memberId);

    Post findByPostId(Long postId);

    void deletePost(Long postId);

    void addViewCount(Long postId);

    void recommendPost(@Param("postId") Long postId, @Param("recommenderId") Long memberId);

    int recommendDuplicateCheck(@Param("postId") Long postId, @Param("recommenderId") Long memberId);

    Long recommendCounting(Long postId);
}
