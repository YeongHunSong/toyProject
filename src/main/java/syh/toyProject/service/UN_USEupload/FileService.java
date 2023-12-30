package syh.toyProject.service.UN_USEupload;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriUtils;
import syh.toyProject.domain.UNUSE_upload.File;
import syh.toyProject.repository.UN_USEupload.FileRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@RequiredArgsConstructor
public class FileService {

    private final FileStore fileStore;
    private final FileRepository fileRepository;

    public File save(File file) {
        return fileRepository.save(file);
    }

    public File findByFileId(Long fileId) {
        return fileRepository.findByFileId(fileId);
    }


    public String getMediaType(String fileName) throws IOException {
        return MediaType.parseMediaType(
                Files.probeContentType(
                        Paths.get(fileStore.getFullPath(fileName))))
                .toString();
    }

    public String contentDisposition(String uploadFileName) {
        String encodedUploadFileName = encodedUploadFileName(uploadFileName);
        return "attachment; filename=\"" + encodedUploadFileName + "\"";
    }

    private String encodedUploadFileName(String uploadFileName) {
        return UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
    }
}
