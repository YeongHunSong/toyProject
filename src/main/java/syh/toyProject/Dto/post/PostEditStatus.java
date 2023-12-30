package syh.toyProject.Dto.post;

import lombok.Data;

@Data
public class PostEditStatus {

    private boolean accessDenied = false;

    private PostEditStatus() {
    }

    private PostEditStatus(boolean accessDenied) {
        this.accessDenied = accessDenied;
    }

    public static PostEditStatus accessDenied() {
        return new PostEditStatus(true);
    }
}
