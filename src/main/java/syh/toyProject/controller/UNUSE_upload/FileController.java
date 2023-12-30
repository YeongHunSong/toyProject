package syh.toyProject.controller.UNUSE_upload;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import syh.toyProject.Dto.UNUSE_upload.FileDto;
import syh.toyProject.domain.UNUSE_upload.File;
import syh.toyProject.domain.UNUSE_upload.UploadFile;
import syh.toyProject.service.UN_USEupload.FileService;
import syh.toyProject.service.UN_USEupload.FileStore;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@Slf4j
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileStore fileStore;
    private final FileService fileService;

    @GetMapping("/new")
    public String newFile(@ModelAttribute FileDto fileDto) {
        return "upload/item-form";
    }

    @PostMapping("/new")
    public String saveFile(@ModelAttribute FileDto fileDto, RedirectAttributes redirectAttributes) throws IOException {

        UploadFile attachFile = fileStore.storeFile(fileDto.getAttachFile());
        List<UploadFile> storeImageFiles = fileStore.storeFiles(fileDto.getImageFiles());

        // DB에 저장 // DB에 파일을 직접 저장하지 않음. 보통 파일 경로와 이름 정도만 저장. // 경로의 경우도 fullPath 다 저장하는 것이 아닌 기본 경로를 맞춰놓고 나머지만 저장
        File file = new File(fileDto, attachFile, storeImageFiles);
        fileService.save(file);

        redirectAttributes.addAttribute("fileId", file.getFileId());
        return "redirect:/files/{fileId}";
    }
    
    @GetMapping("/{fileId}")
    public String fileDetail(@PathVariable Long fileId, Model model) {
        File file = fileService.findByFileId(fileId);
        model.addAttribute("file", file);
        return "upload/item-view";
    }

    @ResponseBody
    @GetMapping("/images/{fileName}") // 실제로 사용하기에는 보안에 취약함. 여러 체크 로직이 들어가야함. -> 이부분은 찾아서 좀 바꾸기
    public ResponseEntity<Resource> imageView(@PathVariable String fileName) throws IOException {

        UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(fileName));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, fileService.getMediaType(fileName))
                .body(resource);
    }

    @GetMapping("attach/{fileId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long fileId) throws MalformedURLException {
        File file = fileService.findByFileId(fileId);
        String storeFileName = file.getAttachFile().getServerName();
        String uploadFileName = file.getAttachFile().getOriginName();

        UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, fileService.contentDisposition(uploadFileName))
                .body(resource);

    }
}
