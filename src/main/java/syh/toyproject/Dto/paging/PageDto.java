package syh.toyproject.Dto.paging;

import lombok.Data;

@Data
public class PageDto {
    
    private int pageNum;
    
    // 추후 페이지 사이즈 변경할 기능을 넣을 때 사용
    private int pageSize = 5;
    private int pageCount;
    
    // 추후 PostSearchCond 랑 통합해서 사용할 예정
//    private String searchType;
//    private String searchKeyword;

    public PageDto() {
        this.pageNum = 1;
//        this.pageSize = 5;
        this.pageCount = 0;
    }

    public PageDto(int pageNum) {
        this.pageNum = pageNum;
//        this.pageSize = pageSize;
        this.pageCount = (pageNum - 1) * pageSize;
//        this.searchType = searchType;
//        this.searchKeyword = searchKeyword;
    }
}
