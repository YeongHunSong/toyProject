package syh.toyProject.repository.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import syh.toyProject.domain.comment.Comment;
import syh.toyProject.paging.PageDto;
import syh.toyProject.paging.SortingDto;

import java.util.List;

@Mapper
public interface CommentMapper {

    void addComment(Comment comment);

    int totalCount(Long postId);

    int totalCountByMemberId(Long memberId);

    List<Comment> findAll();

    List<Comment> findByPostIdAll(@Param("postId") Long postId, @Param("pageDto") PageDto pageDto,
                                  @Param("sortingDto") SortingDto sortingDto);

    List<Comment> findByMemberIdAll(@Param("memberId") Long memberId, @Param("pageDto") PageDto pageDto);

    Comment findByCommentId(Long commentId);

    void editComment(@Param("commentId") Long commentId, @Param("commentEditDto") Comment commentEditDto);

    void deleteComment(Long commentId);

    void deleteAllCommentByPostId(Long postId);

}
