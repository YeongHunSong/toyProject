package syh.toyProject.Dto.post;

import lombok.Data;

@Data
public class PostSearchCond {

    // 1: TITLE
    // 2: CONTENT
    // 3: USERNAME
    private String searchType;

    private String searchKeyword;

    private PostSearchCond() {
    }
}
