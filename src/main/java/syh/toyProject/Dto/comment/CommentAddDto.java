package syh.toyProject.Dto.comment;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CommentAddDto {
    @NotBlank
    private String commentWriter;

    @NotBlank
    private String commentContent;

    private CommentAddDto() {
    }
}
