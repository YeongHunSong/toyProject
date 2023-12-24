package syh.toyProject.repository.upload;

import syh.toyProject.domain.UNUSE_upload.File;

public interface FileRepository {

    public File save(File file);

    public File findByFileId(Long fileId);
}
