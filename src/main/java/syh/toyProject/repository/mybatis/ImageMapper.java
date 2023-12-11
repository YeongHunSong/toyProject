package syh.toyProject.repository.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import syh.toyProject.domain.image.Image;

import java.util.List;

@Mapper
public interface ImageMapper {

    void uploadImage(@Param("image") Image image);

    List<Image> findByPostId(Long postId);

    void editImage(Long imageId, Image imageEditDto);

    void deleteImage(Long imageId);

    void deleteImageAll(Long postId);

}
