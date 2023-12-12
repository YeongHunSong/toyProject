package syh.toyProject.Dto.image;

import lombok.Data;
import syh.toyProject.domain.image.Image;

import java.io.File;
import java.util.List;

@Data
public class ImageBoardDto {
    private Long postId;
    private String imagePath;
    private List<Image> imageList;

    public ImageBoardDto() {
    }

    public ImageBoardDto(Long postId, List<Image> imageList, String imageDir) {
        this.postId = postId;
        this.imageList = imageList;
        this.imagePath = imageDir + postId.toString() + File.separator;
    }
}
