package syh.toyProject.service.image;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;
import syh.toyProject.domain.image.Image;
import syh.toyProject.repository.image.ImageRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    public void uploadImage(Image image) {
        imageRepository.uploadImage(image);
    }

    public List<Image> findByPostId(Long postId) {
        return imageRepository.findByPostId(postId);
    }

    public void editImage(Long imageId, Image imageEditDto) {
        imageRepository.editImage(imageId, imageEditDto);
    }

    public void deleteImage(Long imageId) {
        imageRepository.deleteImage(imageId);
    }

    public void deleteImageAll(Long postId) {
        imageRepository.deleteImageAll(postId);
    }
    
    // TODO 부가 기능 추가



    public String getMediaType(String fileName) throws IOException {
        return MediaType
                .parseMediaType(
                        Files.probeContentType(
                                Paths.get(ImageStore.getFullPath(0L, fileName))))
                .toString();
    }

    public String contentDisposition(String uploadFileName) {
        String encodedUploadFileName = encodedUploadFileName(uploadFileName);
        return "attachment; filename=\"" + encodedUploadFileName + "\"";
    }

    public String byteToMB(Long fileSize) {
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

    private String encodedUploadFileName(String uploadFileName) {
        return UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
    }
}
