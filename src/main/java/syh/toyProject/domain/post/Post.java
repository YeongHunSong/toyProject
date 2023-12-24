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

    private Post() {
    }

    private Post(PostCategory category, String postTitle, String postContent) {
        this.category = category;
        this.postTitle = postTitle;
        this.postContent = postContent;
    }

    private Post(Long memberId, String postTitle, String postContent) {
        this.memberId = memberId;
        this.postTitle = postTitle;
        this.postContent = postContent;
    }

    public static Post postEdit(PostCategory category, String postTitle, String postContent) {
        return new Post(category, postTitle, postContent);
    }

    public static Post postAdd(Long memberId, String postTitle, String  postContent) {
        return new Post(memberId, postTitle, postContent);
    }
}
