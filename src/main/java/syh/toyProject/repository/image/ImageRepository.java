package syh.toyProject.repository.image;

import syh.toyProject.domain.image.Image;

import java.util.List;

public interface ImageRepository {

    void uploadImage(Image image);

    List<Image> findByPostId(Long postId);

    void editImage(Long imageId, Image imageEditDto);

    void deleteImage(Long imageId);

    void deleteImageAll(Long postId);
}
