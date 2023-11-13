package syh.toyproject.paging;

import lombok.Data;

@Data
public class PageDto {
    
    private int pageNum;
    
    // 추후 페이지 사이즈 변경할 기능을 넣을 때 사용
    private int pageView;
    private int pageCount;
    
    // PostSearchCond 랑 통합해서 사용할지.
//    private String searchType;
//    private String searchKeyword;

    public PageDto() {
        this.pageNum = 1;
        this.pageCount = 0;
    }

    public PageDto(int pageNum, int pageView) {
        this.pageNum = pageNum <= 0 ? 1 : pageNum;
        this.pageCount = (pageNum - 1) * pageView;
        this.pageView = pageView;
    }
}
