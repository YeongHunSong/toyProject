package syh.toyProject.domain.image;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Image {

    private Long imageId;
    private Long postId;
    private Long fileSize;

    private String originName;
    private String serverName;

    private LocalDateTime uploadDate;

    public Image() {
    }

    public Image(UploadImage uploadImage) {
        this.postId = uploadImage.getPostId();
        this.fileSize = uploadImage.getFileSize();
        this.originName = uploadImage.getOriginName();
        this.serverName = uploadImage.getServerName();
    }
}
