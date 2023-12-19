package syh.toyProject.repository.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import syh.toyProject.domain.comment.Comment;
import syh.toyProject.paging.PageDto;
import syh.toyProject.paging.SortingDto;
import syh.toyProject.repository.mybatis.CommentMapper;

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
    public int totalCount(Long postId) {
        return commentMapper.totalCount(postId);
    }

    @Override
    public int totalCountByMemberid(Long memberId) {
        return commentMapper.totalCountByMemberId(memberId);
    }

    @Override
    public List<Comment> findAll() {
        return commentMapper.findAll();
    }

    @Override
    public List<Comment> findByPostIdAll(Long postId, PageDto pageDto, SortingDto sortingDto) {
        return commentMapper.findByPostIdAll(postId, pageDto, sortingDto);
    }

    @Override
    public List<Comment> findByMemberIdAll(Long memberId, PageDto pageDto) {
        return commentMapper.findByMemberIdAll(memberId, pageDto);
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

    @Override
    public void deleteAllCommentByPostId(Long postId) {
        commentMapper.deleteAllCommentByPostId(postId);
    }
}
