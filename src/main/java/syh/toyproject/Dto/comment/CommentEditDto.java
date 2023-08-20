package syh.toyproject.Dto.comment;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CommentEditDto {

    @NotBlank
    private String commentContent;

    public CommentEditDto(String commentContent) {
        this.commentContent = commentContent;
    }
}
