package syh.toyProject.repository.image;

import syh.toyProject.domain.image.Image;

import java.util.List;

public interface ImageRepository {

    void uploadImage(Image image);

    List<Image> findByPostId(Long postId);

    void editImage(Image imageEditDto);

    void deleteImage(Image image);

    void deleteImageAll(Long postId);
}
