package syh.toyproject.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import syh.toyproject.domain.comment.Comment;
import syh.toyproject.Dto.comment.CommentBoardDto;
import syh.toyproject.repository.comment.CommentRepository;
import syh.toyproject.repository.member.MemberRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    public Comment addComment(Comment comment) {
        return commentRepository.addComment(comment);
    }


    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    public List<Comment> findByMemberIdAll(Long memberId) {
        return commentRepository.findByMemberIdAll(memberId);
    }

    public List<Comment> findByPostIdAll(Long postId) {
        return commentRepository.findByPostIdAll(postId);
    }

    public Comment findByCommentId(Long commentId) {
        return commentRepository.findByCommentId(commentId);
    }

    public Long findByMemberId(Long commentId) {
        return commentRepository.findByCommentId(commentId).getMemberId();
    }


    public void editComment(Long CommentId, Comment commentEditDto) {
        commentRepository.editComment(CommentId, commentEditDto);
    }


    public void deleteComment(Long commentId) {
        commentRepository.deleteComment(commentId);
    }

    public List<CommentBoardDto> commentListToCommentDto(List<Comment> commentList) {
        return commentList.stream().map(this::commentToCommentDto)
                .collect(Collectors.toList());
    }

    public CommentBoardDto commentToCommentDto(Comment comment) {
        return new CommentBoardDto(comment, findUsername(comment.getMemberId()));
    }


    private String findUsername(Long memberId) {
        return memberRepository.findByMemberId(memberId).getUsername();
    }
}