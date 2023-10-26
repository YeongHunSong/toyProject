package syh.toyproject.Dto.paging;

import lombok.Data;

@Data
public class PageControl {
    private PageDto pageDto;

    private int postTotalCount;
    private int dpStartPage;
    private int dpEndPage;

    private int lastPage;

    private boolean prev;
    private boolean next;

    private final int dpButtonNum = 5; // ##### 이 값으로 발생하는 에러가 여러 가지

    private void calcPage() { // pageNum = 25
        dpEndPage = (int) (Math.ceil(pageDto.getPageNum() / (double) dpButtonNum) * dpButtonNum);
        dpStartPage = (dpEndPage - dpButtonNum) + 1;
        lastPage = (int) (Math.ceil(postTotalCount / (double) pageDto.getPageSize()));

        prev = dpStartPage != 1;
        next = postTotalCount > (dpEndPage * pageDto.getPageSize());

        if (!next) { // pageNum 으로 DB 페이지를 초월하는 값이 들어올 경우, 결과가 buttonNum 페이지 만큼 안 나올 경우.
            dpEndPage = (int) Math.ceil(postTotalCount / (double) pageDto.getPageSize());
            dpStartPage = (dpEndPage - (dpEndPage % dpButtonNum)) + 1;
        }
    }

    public PageControl(PageDto pageDto, int postTotalCount) {
        this.pageDto = pageDto;
        this.postTotalCount = postTotalCount;
        calcPage();
    }
}
