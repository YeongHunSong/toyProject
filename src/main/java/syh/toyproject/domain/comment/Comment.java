package syh.toyproject.domain.comment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    private Long commentId; // 댓글 전체의 고유 id 값
    private Long postId; // 댓글이 어느 글에 작성이 되었는지.
    private Long memberId; // 댓글의 작성자

    private LocalDateTime writingDate;
    private LocalDateTime lastEditDate; // 따로 노출은 하지 않을 예정.
    // 아니면 댓글은 따로 수정을 못 하게 하던가

    private String commentContent; // 댓글은 따로 제목이 없도록.

    public Comment() {
    }

    public Comment(String commentContent) {
        this.commentContent = commentContent;
    }

    public Comment(Long postId, Long memberId, String commentContent) {
        this.postId = postId;
        this.memberId = memberId;
        this.commentContent = commentContent;
    }
}
