package syh.toyProject.domain.post;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Post {
    private Long postId;

    private Long memberId;

    private PostCategory category;
    private Long viewCount;

    private LocalDateTime writingDate;
    private LocalDateTime lastEditDate; // 처음값은 writingDate 와 동일, 이후 수정 시 갱신되도록.
    
    private String postTitle;
    private String postContent; // 이부분은 나중에 타입 변경이 필요할 경우 변경하는 걸로...

    private Long commentCount = 0L;
    private Long recommendCount = 0L;

    public Post() {
    }

    public Post(PostCategory category, String postTitle, String postContent) {
        this.category = category;
        this.postTitle = postTitle;
        this.postContent = postContent;
    }

    public Post(PostCategory category, String postTitle, String postContent, Long memberId) {
        this.category = category;
        this.postTitle = postTitle;
        this.postContent = postContent;

        this.memberId = memberId;
    }

    public Post(Long memberId, String postTitle, String postContent) {
        this.memberId = memberId;
        this.postTitle = postTitle;
        this.postContent = postContent;
    }
}
