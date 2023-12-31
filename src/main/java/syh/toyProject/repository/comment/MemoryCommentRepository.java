package syh.toyProject.repository.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import syh.toyProject.domain.comment.Comment;
import syh.toyProject.paging.PageDto;
import syh.toyProject.paging.SortingDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
//@Repository
@RequiredArgsConstructor
public class MemoryCommentRepository implements CommentRepository {
    private static final Map<Long, Comment> store = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public Comment addComment(Comment comment) {
        comment.setCommentId(++sequence);
        comment.setWritingDate(LocalDateTime.now());
        comment.setLastEditDate(LocalDateTime.now());
        store.put(comment.getCommentId(), comment);
        return comment;
    }

    @Override
    public int totalCount(Long postId) {
        return 0;
    }

    @Override
    public int totalCountByMemberid(Long memberId) {
        return 0;
    }


    @Override
    public Comment findByCommentId(Long commentId) {
        return store.get(commentId);
    }

    @Override
    public List<Comment> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<Comment> findByMemberIdAll(Long memberId, PageDto pageDto) {
        return findAll().stream()
                .filter(comment -> comment.getMemberId().equals(memberId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Comment> findByPostIdAll(Long postId, PageDto pageDto, SortingDto sortingDto) {
        return findAll().stream()
                .filter(comment -> comment.getPostId().equals(postId))
                .collect(Collectors.toList());
    }


    @Override
    public void editComment(Long commentId, Comment commentEditDto) {
        Comment findComment = findByCommentId(commentId);
        findComment.setCommentContent(commentEditDto.getCommentContent());
        findComment.setLastEditDate(LocalDateTime.now());
    }


    @Override
    public void deleteComment(Long commentId) {
        store.remove(commentId);
    }

    @Override
    public void deleteAllCommentByPostId(Long postId) {

    }

    public void clear() {
        sequence = 0;
        store.clear();
    }
}
