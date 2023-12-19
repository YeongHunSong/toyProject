package syh.toyProject.domain.post;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostCategory {

    NOTICE("공지게시판", 0),
    FREE("자유게시판", 1),
    FOOD("음식게시판", 2),
    MUSIC("음향게시판", 3);

    private final String categoryName;
    private final int categoryNumber;
}
