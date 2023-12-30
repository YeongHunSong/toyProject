package syh.toyProject.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import syh.toyProject.Dto.comment.CommentAddDto;
import syh.toyProject.Dto.comment.CommentBoardDto;
import syh.toyProject.Dto.comment.CommentEditDto;
import syh.toyProject.domain.comment.Comment;
import syh.toyProject.paging.PageDto;
import syh.toyProject.paging.SortingDto;
import syh.toyProject.repository.comment.CommentRepository;
import syh.toyProject.repository.member.MemberRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    public Comment addComment(Long postId, Long loginMemberId, CommentAddDto commentAddDto) {
        return commentRepository.addComment(Comment.addFrom(postId, loginMemberId, commentAddDto.getCommentContent()));
    }

    public int totalCount(Long postId) {
        return commentRepository.totalCount(postId);
    }

    public int totalCountByMemberId(Long memberId) {
        return commentRepository.totalCountByMemberid(memberId);
    }

    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    public List<Comment> findByMemberIdAll(Long memberId, PageDto pageDto) {
        return commentRepository.findByMemberIdAll(memberId, pageDto);
    }

    public List<Comment> findByPostIdAll(Long postId, PageDto pageDto, SortingDto sortingDto) {
        return commentRepository.findByPostIdAll(postId, pageDto, sortingDto);
    }

    public Comment findByCommentId(Long commentId) {
        return commentRepository.findByCommentId(commentId);
    }

    public Long findByMemberId(Long commentId) {
        return commentRepository.findByCommentId(commentId).getMemberId();
    }


    public void editComment(Long CommentId, CommentEditDto commentEditDto) {
        commentRepository.editComment(CommentId, Comment.editFrom(commentEditDto.getCommentContent()));
    }


    public void deleteComment(Long commentId) {
        commentRepository.deleteComment(commentId);
    }

    public List<CommentBoardDto> commentListToCommentDto(List<Comment> commentList) {
        return commentList.stream().map(this::commentToCommentDto)
                .collect(Collectors.toList());
    }

    public CommentBoardDto commentToCommentDto(Comment comment) {
        return CommentBoardDto.create(comment, findUsername(comment.getMemberId()));
    }


    private String findUsername(Long memberId) {
        return memberRepository.findByMemberId(memberId).getUsername();
    }
}
