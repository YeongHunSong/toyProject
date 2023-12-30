package syh.toyProject.Dto.post;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import syh.toyProject.domain.post.Post;
import syh.toyProject.domain.post.PostCategory;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostAddDto {

    private PostCategory category;

    @NotBlank
    private String postTitle;

    @NotBlank
    private String postContent;

    private LocalDateTime uploadDate;
    private List<MultipartFile> uploadImages;

    public Post newPost(Long memberId) {
        return Post.postAdd(memberId, postTitle, postContent);
    }

    private PostAddDto() {
    }

    private PostAddDto(String postTitle, String postContent) {
        this.postTitle = postTitle;
        this.postContent = postContent;
    }

    public static PostAddDto create(Post post) {
        return new PostAddDto(post.getPostTitle(), post.getPostContent());
    }

}
