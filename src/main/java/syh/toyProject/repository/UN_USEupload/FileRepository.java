package syh.toyProject.repository.UN_USEupload;

import syh.toyProject.domain.UNUSE_upload.File;

public interface FileRepository {

    public File save(File file);

    public File findByFileId(Long fileId);
}
