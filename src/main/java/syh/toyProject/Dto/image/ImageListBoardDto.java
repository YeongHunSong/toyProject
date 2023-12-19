package syh.toyProject.Dto.image;

import lombok.Data;

import java.util.List;

@Data
public class ImageListBoardDto {
    private Long postId;
    private List<ImageDto> imageDtoList;

    public ImageListBoardDto() {
    }

    public ImageListBoardDto(Long postId, List<ImageDto> imageDtoList) {
        this.postId = postId;
        this.imageDtoList = imageDtoList;
    }
}
