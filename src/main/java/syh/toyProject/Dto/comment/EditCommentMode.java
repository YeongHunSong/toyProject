package syh.toyProject.Dto.comment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EditCommentMode {
        
    ON("댓글수정중"),
    OFF("기본"),
    ERR("댓글수정오류");

    private final String desc;
}
