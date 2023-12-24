package syh.toyProject.paging;

import lombok.Data;

@Data
public class SortingDto {

    // ASC  오름차순 - 과거순
    // DESC 내림차순 - 최신순
    // 기본값 최신순
    private String sorting = "DESC";

    private SortingDto() {
    }

    private SortingDto(String sorting) {
        if (sorting.equals("ASC")) {
            this.sorting = sorting;
        }
    }
}
