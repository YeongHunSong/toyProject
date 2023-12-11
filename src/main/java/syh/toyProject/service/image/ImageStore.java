package syh.toyProject.service.image;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import syh.toyProject.Dto.image.ImageDto;
import syh.toyProject.domain.image.UploadImage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class ImageStore {

    @Value("${file.dir}")
    private static String fileDir;


    public static String getFullPath(Long postId, String fileName) {
        return fileDir + postId.toString() + File.separator + fileName;
    }

    public List<UploadImage> storeFiles(ImageDto imageDto) throws IOException {
        List<UploadImage> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : imageDto.getUploadImages()) {
            if (!imageDto.getUploadImages().isEmpty()) {
                storeFileResult.add(storeFile(multipartFile, imageDto.getPostId()));
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
}
