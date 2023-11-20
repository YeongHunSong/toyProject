package syh.toyproject.paging;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class PageDto {
    
    private int page;
    private int pageView;
    private int pageCount;
    

    public void pageProcess() {
        this.page = page <= 0 ? 1 : page;
        this.pageCount = (page - 1) * pageView;
    }


    public PageDto() {
        this.page = 1;
        this.pageCount = 0;
        this.pageView = 10;
    }

    public PageDto(int page, int pageView) {
        this.page = page <= 0 ? 1 : page;
        this.pageCount = (page - 1) * pageView;
        this.pageView = pageView;
    }
}
