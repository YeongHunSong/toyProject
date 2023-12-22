package syh.toyProject.paging;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class PageControlTest {

    @Test
    void pageGenerateTest() {
        PageDto pageDto = PageDto.create(100, 5);
        PageControl pageControl = PageControl.create(pageDto, 191);

        log.info("pageControl = {}", pageControl);

        for (int i = pageControl.getDpStartPage(); i <= pageControl.getDpEndPage(); i++) {
            log.info("{} ", i);
        }
    }

}