package syh.toyproject.paging;

import lombok.Data;

@Data
public class PageControl {
    private PageDto pageDto;

    private int totalCount;
    private int dpStartPage;
    private int dpEndPage;

    private int lastPage;

    private boolean prev;
    private boolean next;

    private final int dpButtonNum = 10;

    private void pageProcess() { // 30
        dpEndPage = (int) (Math.ceil(pageDto.getPageNum() / (double) dpButtonNum) * dpButtonNum);
        dpStartPage = (dpEndPage - dpButtonNum) + 1;
        lastPage = (int) (Math.ceil(totalCount / (double) pageDto.getPageView()));

        prev = dpStartPage != 1;
        next = totalCount > (dpEndPage * pageDto.getPageView());

        if (!next) { // pageNum 으로 DB 페이지를 초월하는 값이 들어올 경우, 결과가 buttonNum 페이지 만큼 안 나올 경우.
            dpEndPage = totalCount <= 0 ? 1 : (int) Math.ceil(totalCount / (double) pageDto.getPageView());
            dpStartPage =
                    ((dpEndPage / dpButtonNum) * dpButtonNum + 1) > dpEndPage ?
                    (((dpEndPage / dpButtonNum) - 1) * dpButtonNum + 1) :
                    ((dpEndPage / dpButtonNum) * dpButtonNum + 1);
        }
    }

    public PageControl(PageDto pageDto, int totalCount) {
        this.pageDto = pageDto;
        this.totalCount = totalCount;
        pageProcess();
    }
}
