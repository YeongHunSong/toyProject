package syh.toyproject.Dto.post;

import lombok.Data;
import syh.toyproject.domain.post.PostCategory;

import javax.validation.constraints.NotBlank;

@Data
public class PostAddDto {
    private PostCategory category;

    @NotBlank
    private String postTitle;

    @NotBlank
    private String postContent;

    private Long writerId;
    private String username;


    public PostAddDto(String postTitle, String postContent) {
        this.postTitle = postTitle;
        this.postContent = postContent;
    }
}
