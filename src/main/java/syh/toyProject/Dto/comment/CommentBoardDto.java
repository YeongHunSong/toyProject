package syh.toyProject.Dto.comment;

import lombok.Data;
import syh.toyProject.domain.comment.Comment;

import java.time.LocalDateTime;

@Data
public class CommentBoardDto {

    private Long commentId;
    private Long postId;
    private Long memberId;
    private String username;

    private LocalDateTime writingDate;
    private LocalDateTime lastEditDate;

    private String commentContent;

    public CommentBoardDto() {
    }

    public CommentBoardDto(Comment comment, String username) {
        this.commentId = comment.getCommentId();
        this.postId = comment.getPostId();
        this.memberId = comment.getMemberId();
        this.writingDate = comment.getWritingDate();
        this.lastEditDate = comment.getLastEditDate();
        this.commentContent = comment.getCommentContent();

        this.username = username;
    }
}
