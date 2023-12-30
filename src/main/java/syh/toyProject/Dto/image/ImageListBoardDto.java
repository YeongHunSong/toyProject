package syh.toyProject.Dto.image;

import lombok.Data;

import java.util.List;

@Data
public class ImageListBoardDto {
    private Long postId;
    private List<ImageDto> imageDtoList;

    private ImageListBoardDto() {
    }

    private ImageListBoardDto(Long postId, List<ImageDto> imageDtoList) {
        this.postId = postId;
        this.imageDtoList = imageDtoList;
    }

    public static ImageListBoardDto create(Long postId, List<ImageDto> imageDtoList) {
        return new ImageListBoardDto(postId, imageDtoList);
    }
}
