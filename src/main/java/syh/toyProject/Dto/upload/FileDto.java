package syh.toyProject.Dto.upload;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class FileDto {

    private Long fileId;
    private String fileName;
    private MultipartFile attachFile;
    private List<MultipartFile> imageFiles;

    public FileDto() {
    }
}
