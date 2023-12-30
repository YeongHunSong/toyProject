package syh.toyProject.Dto.post;

import lombok.Data;
import syh.toyProject.Dto.image.ImageDto;
import syh.toyProject.domain.post.Post;
import syh.toyProject.domain.post.PostCategory;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class PostEditDto {

    private PostCategory category;

    private Long postId;

    @NotBlank
    private String postTitle;

    @NotBlank
    private String postContent;


    private List<ImageDto> imageDtoList;

    private PostEditDto() {
    }

    private PostEditDto(Post post, List<ImageDto> imageDtoList) {
        this.postId = post.getPostId();
        this.postTitle = post.getPostTitle();
        this.postContent = post.getPostContent();
        this.imageDtoList = imageDtoList;
    }

    public static PostEditDto create(Post post, List<ImageDto> imageDtoList) {
        return new PostEditDto(post, imageDtoList);
    }

}
