package syh.toyProject.paging;

public class CommentPageDto extends PageDto {

    private CommentPageDto() {
        super(1, 5);
    }

    public static PageDto create() {
        return new CommentPageDto();
    }
}
