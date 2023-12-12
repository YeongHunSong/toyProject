package syh.toyProject.service.image;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;
import syh.toyProject.Dto.image.ImageUploadDto;
import syh.toyProject.domain.image.UploadImage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class ImageStore {

    @Value("${image.dir}")
    private String imageDir; // static 을 박으면 null 값으로 나와버림.

    public String getFullPath(Long postId, String serverName) {
        return imageDir + postId.toString() + File.separator + serverName;
    }

    public List<UploadImage> storeFiles(ImageUploadDto imageUploadDto) throws IOException {
        List<UploadImage> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : imageUploadDto.getUploadImages()) {
            if (!imageUploadDto.getUploadImages().isEmpty()) {
                storeFileResult.add(storeFile(multipartFile, imageUploadDto.getPostId()));
            }
        }
        return storeFileResult;
    }

    public UploadImage storeFile(MultipartFile multipartFile, Long postId) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);

        File saveFile = new File(getFullPath(postId, storeFileName));
        saveFile.mkdirs();

        log.info("saveFile = {}", saveFile);

        multipartFile.transferTo(saveFile); // 저장
        return new UploadImage(postId, multipartFile.getSize(), originalFilename, storeFileName);
    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    //UTF-8 로 변경하면 %ED%97%A4%EB%A5%99%EC%84%B8%20%ED%8A%9C%EB%8B%9D%20%EC%B0%B8%EA%B3%A0%EC%9A%A9 이런 식으로 나옴.
    private String encodeFileName(String fileName) {
        return UriUtils.encode(fileName, StandardCharsets.UTF_8);
    }
}
