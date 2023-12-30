package syh.toyProject.repository.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import syh.toyProject.domain.image.Image;

import java.util.List;

@Mapper
public interface ImageMapper {

    void uploadImage(@Param("image") Image image);

    List<Image> findByPostId(Long postId);

    void editImage(@Param("image") Image imageEditDto);

    void deleteImage(@Param("image") Image image);

    void deleteImageAll(Long postId);

}
