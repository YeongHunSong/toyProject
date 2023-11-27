package syh.toyProject.Dto.post;

import lombok.Data;
import syh.toyProject.domain.post.PostCategory;

import javax.validation.constraints.NotBlank;

@Data
public class PostEditDto {

    private PostCategory category; // 카테고리 수정을 없애는 게 더 낫지 않나 싶기도

    @NotBlank
    private String postTitle;

    @NotBlank
    private String postContent;

    public PostEditDto() {
    }

    public PostEditDto(PostCategory category, String postTitle, String postContent) {
        this.category = category;
        this.postTitle = postTitle;
        this.postContent = postContent;
    }

    public PostEditDto(String postTitle, String postContent) {
        this.postTitle = postTitle;
        this.postContent = postContent;
    }
}
