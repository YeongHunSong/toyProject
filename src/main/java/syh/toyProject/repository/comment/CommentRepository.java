package syh.toyProject.repository.comment;

import syh.toyProject.domain.comment.Comment;
import syh.toyProject.paging.PageDto;
import syh.toyProject.paging.SortingDto;

import java.util.List;

public interface CommentRepository {

    Comment addComment(Comment comment);

    int totalCount(Long postId);

    int totalCountByMemberid(Long memberId);

    List<Comment> findAll();

    List<Comment> findByPostIdAll(Long postId, PageDto pageDto, SortingDto sortingDto);

    List<Comment> findByMemberIdAll(Long memberId, PageDto pageDto);

    Comment findByCommentId(Long commentId);


    void editComment(Long commentId, Comment commentEditDto);


    void deleteComment(Long commentId);

    void deleteAllCommentByPostId(Long postId);

}
