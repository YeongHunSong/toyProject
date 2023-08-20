package syh.toyproject.Dto.post;

import lombok.Data;
import syh.toyproject.domain.post.Post;
import syh.toyproject.domain.post.PostCategory;

import java.time.LocalDateTime;

@Data
public class PostBoardDto {
    private Long postId;

    private Long memberId;
    private String username;

    private PostCategory category;
    private Long viewCount;

    private LocalDateTime writingDate;
    private LocalDateTime lastEditDate;

    private String postTitle;
    private String postContent;

    // 댓글 관련
    private Long replyCount;

    // 추가 기능
    private Long recommendCount;
    private Long dislikeCount;

    public PostBoardDto(Post post, String username) {
        this.postId = post.getPostId();
        this.memberId = post.getMemberId();
        this.category = post.getCategory();
        this.viewCount = post.getViewCount();
        this.writingDate = post.getWritingDate();
        this.lastEditDate = post.getLastEditDate();
        this.postTitle = post.getPostTitle();
        this.postContent = post.getPostContent();
        this.replyCount = post.getReplyCount();
        this.recommendCount = post.getRecommendCount();
        this.dislikeCount = post.getDislikeCount();

        this.username = username;
    }
}
