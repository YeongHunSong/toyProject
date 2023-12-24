package syh.toyProject.domain.UNUSE_upload;

import lombok.Data;
import syh.toyProject.Dto.UNUSE_upload.FileDto;

import java.util.List;

@Data
public class File {

    private Long fileId;
    private String fileName;
    private UploadFile attachFile;
    private List<UploadFile> imageFiles;


    public File(FileDto fileDto, UploadFile attachFile, List<UploadFile> imageFiles) {
        this.fileId = fileDto.getFileId();
        this.fileName = fileDto.getFileName();
        this.attachFile = attachFile;
        this.imageFiles = imageFiles;
    }
}
