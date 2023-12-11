package syh.toyProject.repository.image;

import syh.toyProject.domain.image.Image;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MemoryImageRepository implements ImageRepository {

    private final Map<Long, Image> store = new HashMap<>();
    private long sequence = 0L;

    public void uploadImage(Image image) {
        image.setImageId(++sequence);
        store.put(image.getImageId(), image);
    }

    public List<Image> findByPostId(Long postId) {
        return store.values()
                .stream()
                .filter(image -> image.getPostId().equals(postId))
                .collect(Collectors.toList());
    }

    @Override // #TODO 수정 or 삭제
    public void editImage(Long imageId, Image image) {
        Image beforeImage = store.get(imageId);
        beforeImage.setOriginName(image.getOriginName());
        beforeImage.setServerName(image.getServerName());
    }

    @Override
    public void deleteImage(Long imageId) {
        store.remove(imageId);
    }

    @Override
    public void deleteImageAll(Long postId) {
    }
}
