package syh.toyproject.repository.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import syh.toyproject.domain.comment.Comment;

import java.util.List;

@Mapper
public interface CommentMapper {

    void addComment(Comment comment);

    List<Comment> findAll();

    List<Comment> findByPostIdAll(Long postId);

    List<Comment> findByMemberIdAll(Long memberId);

    Comment findByCommentId(Long commentId);

    void editComment(@Param("commentId") Long commentId, @Param("commentEditDto") Comment commentEditDto);

    void deleteComment(Long commentId);

    void deleteAllCommentByPostId(Long postId);

}
