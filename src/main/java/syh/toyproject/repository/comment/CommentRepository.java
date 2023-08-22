package syh.toyproject.repository.comment;

import syh.toyproject.domain.comment.Comment;

import java.util.List;

public interface CommentRepository {

    Comment addComment(Comment comment);


    List<Comment> findAll();

    List<Comment> findByPostIdAll(Long postId);

    List<Comment> findByMemberIdAll(Long memberId);

    Comment findByCommentId(Long commentId);


    void editComment(Long commentId, Comment commentEditDto);


    void deleteComment(Long commentId);

    void deleteAllCommentByPostId(Long postId);

}
