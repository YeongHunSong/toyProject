package syh.toyproject.Dto.post;

import lombok.Data;

@Data
public class PostEditStatus {

    private boolean accessDenied = false;

    public PostEditStatus() {
    }

    public PostEditStatus(boolean accessDenied) {
        this.accessDenied = accessDenied;
    }
}
