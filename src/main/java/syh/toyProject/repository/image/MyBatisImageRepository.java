package syh.toyProject.repository.image;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import syh.toyProject.domain.image.Image;
import syh.toyProject.repository.mybatis.ImageMapper;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MyBatisImageRepository implements ImageRepository {

    private final ImageMapper imageMapper;


    @Override
    public void uploadImage(Image image) {
        imageMapper.uploadImage(image);
    }

    @Override
    public List<Image> findByPostId(Long postId) {
        return imageMapper.findByPostId(postId);
    }

    @Override
    public void editImage(Image imageEditDto) {
        imageMapper.editImage(imageEditDto);
    }

    @Override
    public void deleteImage(Long imageId) {
        imageMapper.deleteImage(imageId);
    }

    @Override
    public void deleteImageAll(Long postId) {
        imageMapper.deleteImageAll(postId);
    }
}

