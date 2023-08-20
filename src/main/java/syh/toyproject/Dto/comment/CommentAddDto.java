package syh.toyproject.Dto.comment;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CommentAddDto {
    @NotBlank
    private String commentWriter;

    @NotBlank
    private String commentContent;

    public CommentAddDto(String commentContent) {
        this.commentContent = commentContent;
    }
}
