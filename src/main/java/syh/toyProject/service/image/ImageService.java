package syh.toyProject.service.image;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import syh.toyProject.Dto.image.ImageDto;
import syh.toyProject.Dto.image.UploadImage;
import syh.toyProject.domain.image.Image;
import syh.toyProject.repository.image.ImageRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageStore imageStore;
    private final ImageRepository imageRepository;

    public void uploadImage(UploadImage uploadImage) {
        imageRepository.uploadImage(Image.create(uploadImage));
    }

    public List<ImageDto> findByPostId(Long postId) {
        List<ImageDto> imageDtoList = new ArrayList<>();
        imageRepository.findByPostId(postId)
                .forEach(image -> imageDtoList.add(ImageDto.create(image)));

        log.info("imageDtoList = {}", imageDtoList);
        return imageDtoList;


//        return imageRepository.findByPostId(postId);

//         TODO 수정 혹은 삭제
//        List<ImageDto> imageDtoList = new ArrayList<>();
//        for (Image image : imageList) {
//            ImageDto imageDto = new ImageDto(image);
//            imageDtoList.add(imageDto);
//        }
//        return imageDtoList;
    }

    public void editImage(Image imageEditDto) {
        imageRepository.editImage(imageEditDto);
    }

    public void deleteImage(Image image) {
        imageRepository.deleteImage(image);
    }

    public void deleteImageAll(Long postId) {
        imageRepository.deleteImageAll(postId);
    }




    
    // TODO 부가 기능 추가
    public String getMediaType(String serverName) throws IOException {
        return MediaType
                .parseMediaType(
                        Files.probeContentType(
                                Paths.get(imageStore.getFullPath(0L, serverName))))
                .toString();
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
}
