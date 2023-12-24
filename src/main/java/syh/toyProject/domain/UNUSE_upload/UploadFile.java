package syh.toyProject.domain.UNUSE_upload;

import lombok.Data;

@Data
public class UploadFile {

    // 업로드한 파일명
    private String originName;
    
    // 서버 내부에서 관리하는 파일명
    private String serverName;

    public UploadFile(String originName, String serverName) {
        this.originName = originName;
        this.serverName = serverName;
    }
}
