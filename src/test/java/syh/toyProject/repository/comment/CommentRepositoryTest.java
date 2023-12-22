package syh.toyProject.repository.comment;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import syh.toyProject.Dto.comment.CommentEditDto;
import syh.toyProject.domain.comment.Comment;

import java.util.List;

import static java.lang.Thread.sleep;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Transactional
@SpringBootTest
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @BeforeEach @AfterEach
    void beforeAndAfterEach() {
        if (commentRepository instanceof MemoryCommentRepository) {
            ((MemoryCommentRepository) commentRepository).clear();
        }
    }

    @Test
    void addCommentAndFindByCommentIdTest() {
        Comment comment1 = newComment(1L);

        Comment findComment1 = commentRepository.findByCommentId(comment1.getCommentId());

        assertThat(findComment1.getPostId()).isEqualTo(comment1.getPostId());
        assertThat(findComment1.getMemberId()).isEqualTo(comment1.getMemberId());
        assertThat(findComment1.getCommentId()).isEqualTo(comment1.getCommentId());
        assertThat(findComment1.getWritingDate()).isEqualTo(comment1.getWritingDate());
        assertThat(findComment1.getCommentContent()).isEqualTo("댓글생성테스트1");
    }

    @Test
    void findAllTest() {
        Comment comment1 = newComment(1L);
        Comment comment2 = newComment(2L);

        Comment findComment1 = commentRepository.findByCommentId(comment1.getCommentId());
        Comment findComment2 = commentRepository.findByCommentId(comment2.getCommentId());

        List<Comment> commentList = commentRepository.findAll();

        assertThat(commentList).containsExactly(findComment1, findComment2);
    }

    @Test
    void findByMemberIdAllTest() {
        Comment comment1 = newComment(1L);
        Comment comment2 = newComment(1L);
        Comment comment3 = newComment(1L);
        Comment comment4 = newComment(1L);

        List<Comment> commentList = commentRepository.findByMemberIdAll(comment1.getMemberId(), null);

        assertThat(commentList).containsExactly(comment1, comment2, comment3, comment4);
    }

    @Test
    void findByPostIdAllTest() {
        Comment comment1 = newComment(1L);
        Comment comment2 = newComment(1L);
        Comment comment3 = newComment(1L);
        Comment comment4 = newComment(1L);

        List<Comment> commentList = commentRepository.findByPostIdAll(comment1.getPostId(), null, null);

        assertThat(commentList).containsExactly(comment1, comment2, comment3, comment4);
    }

    @Test
    void editCommentTest() throws InterruptedException {
        CommentEditDto commentDto = new CommentEditDto("댓글은이걸로변경");
        Comment beforeComment = newComment(1L);

        Comment commentEditTemp = Comment.editFrom(commentDto.getCommentContent());
        sleep(1);
        commentRepository.editComment(beforeComment.getCommentId(), commentEditTemp);
        Comment afterComment = commentRepository.findByCommentId(beforeComment.getCommentId());

        assertThat(afterComment.getCommentContent()).isEqualTo("댓글은이걸로변경");
        assertThat(afterComment.getWritingDate()).isNotEqualTo(afterComment.getLastEditDate());
    }

    @Test
    void deleteCommentTest() {
        Comment comment1 = newComment(1L);

        commentRepository.deleteComment(comment1.getCommentId());

        Comment findComment = commentRepository.findByCommentId(comment1.getCommentId());

        assertThat(findComment).isNull();
    }

    @Test
    void deleteAllCommentByPostIdTest() {
        Comment comment1 = newComment(1L);
        Comment comment2 = newComment(1L);
        Comment comment3 = newComment(1L);

        commentRepository.deleteAllCommentByPostId(comment1.getPostId());

        Comment findComment1 = commentRepository.findByCommentId(comment1.getCommentId());
        Comment findComment2 = commentRepository.findByCommentId(comment2.getCommentId());
        Comment findComment3 = commentRepository.findByCommentId(comment3.getCommentId());

        assertThat(findComment1).isNull();
        assertThat(findComment2).isNull();
        assertThat(findComment3).isNull();
    }

    private Comment newComment(Long number) {
        return commentRepository.addComment(Comment.addFrom(number, number, "댓글생성테스트" + number));

    }
}