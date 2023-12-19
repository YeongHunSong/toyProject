package syh.toyProject.repository.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import syh.toyProject.Dto.post.PostSearchCond;
import syh.toyProject.domain.post.Post;
import syh.toyProject.paging.PageDto;
import syh.toyProject.paging.SortingDto;
import syh.toyProject.repository.mybatis.PostMapper;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MyBatisPostRepository implements PostRepository {

    private final PostMapper postMapper;


    @Override
    public Long addPost(Post post) {
        postMapper.addPost(post);
        return post.getPostId();
    }

    @Override
    public int totalCount(PostSearchCond cond) {
        return postMapper.totalCount(cond);
    }

    @Override
    public int totalCountByMemberId(Long memberId) {
        return postMapper.totalCountByMemberId(memberId);
    }


    @Override
    public List<Post> findAll(PostSearchCond cond, PageDto pageDto, SortingDto sortingDto) {
        return postMapper.findAll(cond, pageDto, sortingDto);
    }

    @Override
    public List<Post> findByMemberIdAll(Long memberId, PageDto pageDto) {
        return postMapper.findByMemberIdAll(memberId, pageDto);
    }

    @Override
    public Post findByPostId(Long postId) {
        return postMapper.findByPostId(postId);
    }

    @Override
    public void editPost(Long postId, Post postEditDto) {
        postMapper.editPost(postId, postEditDto);
    }

    @Override
    public void deletePost(Long postId) {
        postMapper.deletePost(postId);
    }

    @Override
    public void addViewCount(Long postId) {
        postMapper.addViewCount(postId);
    }

    @Override
    public void recommendPost(Long postId, Long memberId) {
        postMapper.recommendPost(postId, memberId);
    }

    @Override
    public boolean recommendDuplicateCheck(Long postId, Long memberId) {
        return postMapper.recommendDuplicateCheck(postId, memberId) != 0;
    }


}
