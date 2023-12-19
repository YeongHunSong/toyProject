package syh.toyProject.Dto.comment;

import lombok.Data;

@Data
public class CommentEditStatus {

    private boolean flag = false;
    private Long commentId;
    private EditCommentMode editCommentMode = EditCommentMode.OFF;
    private boolean accessDenied = false;

    public CommentEditStatus() {
    }

    public CommentEditStatus(Long commentId, EditCommentMode mode) {
        this.flag = true;
        this.commentId = commentId;
        this.editCommentMode = mode;
    }

    public CommentEditStatus(long commentId, boolean accessDenied) {
        this.commentId = commentId;
        this.accessDenied = accessDenied;
    }
}

/*
댓글 수정 절차
1.
status.flag 가 false(기본값) 혹은 null 일 경우 수정 버튼이 노출되고,
수정 버튼을 누르면 editStartFlag 가 true, COMMENT_ID 에 commentId(해당 코멘트의 id값) 가 들어간다

2.
postDetail 로 리다이렉트 되어 editStartFlag 가 true 이기 때문에
status.flag = true, status.commentId = COMMENT_ID
COMMENT_ID 로 댓글 내용을 받아와서 commentEditDto 에 설정 후
editStartFlag = false, COMMENT_ID = null

3.
status.flag 가 true 이며, commentList 에서 commentId 가 같은 comment 는 댓글 수정 인풋란 노출

4-1 실패
저장을 누르면 bindingResult 돌리고 errorDto 에 에러 담은 다음 수정창이 계속 보이도록
startFlag 와 에러 처리를 위한 failedFlag 둘 다 true 로 변환 후 리다이렉트 

다시 2번 수행 후, 실패 처리
errorDto 에 있는 에러 그대로 bindingResult 에 넣은 다음 에러 띄워주고, 플래그 다 false 변환
후 다시 3번 상태로 이동

4-2 성공
comment 에서 memberId 값 가지고 오고, commentDto 에서 commentContent 가지고 와서
수정 후 다시 postDetail 로 리다이렉트

 */