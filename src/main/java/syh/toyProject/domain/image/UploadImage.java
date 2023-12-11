package syh.toyProject.domain.image;

import lombok.Data;

@Data
public class UploadImage {

    private Long postId;
    private Long fileSize;
    private String originName;
    private String serverName;

    public UploadImage(Long postId, Long fileSize, String originName, String serverName) {
        this.postId = postId;
        this.fileSize = fileSize;
        this.originName = originName;
        this.serverName = serverName;
    }
}
