package syh.toyproject.repository.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import syh.toyproject.domain.comment.Comment;
import syh.toyproject.repository.mybatis.CommentMapper;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MyBatisCommentRepository implements CommentRepository {

    private final CommentMapper commentMapper;

    @Override
    public Comment addComment(Comment comment) {
        commentMapper.addComment(comment);

        return findByCommentId(comment.getCommentId());
    }

    @Override
    public List<Comment> findAll() {
        return commentMapper.findAll();
    }

    @Override
    public List<Comment> findByPostIdAll(Long postId) {
        return commentMapper.findByPostIdAll(postId);
    }

    @Override
    public List<Comment> findByMemberIdAll(Long memberId) {
        return commentMapper.findByMemberIdAll(memberId);
    }

    @Override
    public Comment findByCommentId(Long commentId) {
        return commentMapper.findByCommentId(commentId);
    }

    @Override
    public void editComment(Long commentId, Comment commentEditDto) {
        commentMapper.editComment(commentId, commentEditDto);
    }

    @Override
    public void deleteComment(Long commentId) {
        commentMapper.deleteComment(commentId);
    }
}
