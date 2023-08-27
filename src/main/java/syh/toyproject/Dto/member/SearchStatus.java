package syh.toyproject.Dto.member;

import lombok.Data;

@Data
public class SearchStatus {

    private boolean flag = false;

    public SearchStatus() {
    }

    public SearchStatus(boolean flag) {
        this.flag = flag;
    }
}
