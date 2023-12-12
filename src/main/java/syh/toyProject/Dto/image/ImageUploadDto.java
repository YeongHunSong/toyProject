package syh.toyProject.Dto.image;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ImageUploadDto {

    private Long postId;
    private LocalDateTime uploadDate;
    private List<MultipartFile> uploadImages;

    public ImageUploadDto() {
    }
}
