package syh.toyProject.Dto.comment;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CommentEditDto {

    @NotBlank
    private String commentContent;

    private CommentEditDto() {
    }

    private CommentEditDto(String commentContent) {
        this.commentContent = commentContent;
    }

    public static CommentEditDto create(String commentContent) {
        return new CommentEditDto(commentContent);
    }
}
