package syh.toyproject.repository.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import syh.toyproject.Dto.post.PostSearchCond;
import syh.toyproject.domain.post.Post;
import syh.toyproject.paging.PageDto;
import syh.toyproject.paging.SortingDto;

import java.util.List;

@Mapper
public interface PostMapper {

    void addPost(Post post);

    void editPost(@Param("postId") Long postId, @Param("postEditDto") Post postEditDto);

    List<Post> findAll(@Param("cond") PostSearchCond cond, @Param("pageDto") PageDto pageDto,
                       @Param("sortingDto") SortingDto sortingDto);

    int totalCount(@Param("cond") PostSearchCond cond);

    int totalCountByMemberId(@Param("memberId") Long memberId);
    List<Post> findByMemberIdAll(@Param("memberId") Long memberId, @Param("pageDto") PageDto pageDto);

    Post findByPostId(Long postId);

    void deletePost(Long postId);

    void addViewCount(Long postId);

    void recommendPost(@Param("postId") Long postId, @Param("recommenderId") Long memberId);

    int recommendDuplicateCheck(@Param("postId") Long postId, @Param("recommenderId") Long memberId);
}
