package syh.toyProject.Dto.image;

import lombok.Data;

@Data
public class UploadImage {

    private Long postId;

    private Long fileSize;

    private String originName;

    private String serverName;

    private UploadImage() {
    }

    private UploadImage(Long postId, Long fileSize, String originName, String serverName) {
        this.postId = postId;
        this.fileSize = fileSize;
        this.originName = originName;
        this.serverName = serverName;
    }

    public static UploadImage create(Long postId, Long fileSize, String originName, String serverName) {
        return new UploadImage(postId, fileSize, originName, serverName);
    }
}
