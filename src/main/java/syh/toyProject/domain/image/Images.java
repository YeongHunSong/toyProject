package syh.toyProject.domain.image;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Images {

    private Long imagesId;
    private Long uploadedPostId;

    private Integer imageNumber;

    private LocalDateTime uploadDate;
    private LocalDateTime lastEditDate;

    private List<UploadImage> uploadImages;
}
