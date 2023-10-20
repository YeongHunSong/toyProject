package syh.toyproject.Dto.paging;

import lombok.Data;

@Data
public class PageControl {
    private PageDto pageDto;

    private int postTotalCount;
    private int dpStartPage;
    private int dpEndPage;

    private boolean prev;
    private boolean next;

    private final int buttonNum = 5;

    private void calcPage() {
        dpEndPage = (int) (Math.ceil(pageDto.getPageNum() / (double) buttonNum) * buttonNum);
        dpStartPage = (dpEndPage - buttonNum) + 1;

        prev = dpStartPage != 1;
        next = postTotalCount > (dpEndPage * pageDto.getPageSize());

        if (!next) {
            dpEndPage = (int) Math.ceil(postTotalCount / (double) pageDto.getPageSize());
        }
    }

    public PageControl(PageDto pageDto, int postTotalCount) {
        this.pageDto = pageDto;
        this.postTotalCount = postTotalCount;
        calcPage();
    }
}
