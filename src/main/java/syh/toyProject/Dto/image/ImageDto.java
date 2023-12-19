package syh.toyProject.Dto.image;

import lombok.Data;
import syh.toyProject.domain.image.Image;

import java.text.DecimalFormat;
import java.time.LocalDateTime;

@Data
public class ImageDto {

    private Long imageId;

    private String fileSize;

    private String originName;
    private String serverName;

    private LocalDateTime uploadDate;

    public ImageDto() {
    }

    public ImageDto(Image image) {
        this.imageId = image.getImageId();
        this.fileSize = byteToMB(image.getFileSize());
        this.originName = image.getOriginName();
        this.serverName = image.getServerName();
        this.uploadDate = image.getUploadDate();
    }

    private String byteToMB(Long fileSize) {
        String retFormat = "MB";
        Double fs = Double.valueOf((fileSize));
        String[] strArr = {"byte", "KB", "MB", "GB"};

        if(fs != 0) {
            int idx = (int)Math.floor(Math.log(fs)/ Math.log(1024));
            DecimalFormat df = new DecimalFormat("#,###.##");
            double ret = fs / Math.pow(1024, idx);
            retFormat = df.format(ret) + " " + strArr[idx];
        }else {
            retFormat += " "+ strArr[0];
        }
        return retFormat;
    }
}
