package syh.toyProject.domain.image;

import lombok.Data;
import syh.toyProject.Dto.image.UploadImage;

import java.time.LocalDateTime;

@Data
public class Image {

    private Long imageId;
    private Long postId;
    private Long fileSize;

    private String originName;
    private String serverName;

    private LocalDateTime uploadDate;

    private Image() {
    }

    private Image(UploadImage uploadImage) {
        this.postId = uploadImage.getPostId();
        this.fileSize = uploadImage.getFileSize();
        this.originName = uploadImage.getOriginName();
        this.serverName = uploadImage.getServerName();
    }

    private Image(Long postId, Long imageId) {
        this.postId = postId;
        this.imageId = imageId;
    }

    public static Image create(UploadImage uploadImage) {
        return new Image(uploadImage);
    }

    public static Image deleteDto(Long postId, Long imageId) {
        return new Image(postId, imageId);
    }
}
