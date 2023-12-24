package syh.toyProject.repository.upload;

import org.springframework.stereotype.Repository;
import syh.toyProject.domain.UNUSE_upload.File;

import java.util.HashMap;
import java.util.Map;

@Repository
public class MemoryFileRepository implements FileRepository {

    private final Map<Long, File> store = new HashMap<>();
    private long sequence = 0L;

    public File save(File file) {
        file.setFileId(++sequence);
        store.put(file.getFileId(), file);
        return file;
    }

    public File findByFileId(Long fileId) {
        return store.get(fileId);
    }
}
