package syh.toyproject.repository.comment;

import syh.toyproject.domain.comment.Comment;

import java.util.List;

public interface CommentRepository {

    public Comment addComment(Comment comment);


    public List<Comment> findAll();

    public List<Comment> findByPostIdAll(Long postId);

    public List<Comment> findByMemberIdAll(Long memberId);

    public Comment findByCommentId(Long commentId);


    public void editComment(Long commentId, Comment commentEditDto);


    public void deleteComment(Long commentId);

    public void deleteAllCommentByPostId(Long postId);

}
