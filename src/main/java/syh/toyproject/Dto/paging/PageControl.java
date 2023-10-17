package syh.toyproject.Dto.paging;

import lombok.Data;

@Data
public class PageControl {
    private PageDto pageDto;

    private int postTotalCount;
    private int endPage;
    private int startPage;

    private boolean prev;
    private boolean next;

    private final int buttonNum = 5;

    private void calcPage() {
        endPage = (int) (Math.ceil(pageDto.getPageNum() / (double) buttonNum) * buttonNum);

        startPage = (endPage - buttonNum) + 1;

        prev = startPage != 1;
        next = postTotalCount > (endPage * pageDto.getPageSize());

        if (!next) {
            endPage = (int) Math.ceil(postTotalCount / (double) pageDto.getPageSize());
        }
    }

    public PageControl(PageDto pageDto, int postTotalCount) {
        this.pageDto = pageDto;
        this.postTotalCount = postTotalCount;
        calcPage();
    }
}
