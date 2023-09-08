package syh.toyproject.repository.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import syh.toyproject.Dto.post.PostSearchCond;
import syh.toyproject.domain.post.Post;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
//@Repository
@RequiredArgsConstructor
public class MemoryPostRepository implements PostRepository {

    private static final Map<Long, Post> store = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public Post addPost(Post post) {
        post.setPostId(++sequence);
        post.setWritingDate(LocalDateTime.now());
        post.setLastEditDate(LocalDateTime.now());
        store.put(post.getPostId(), post);
        return post;
    }


    @Override
    public Post findByPostId(Long postId) {
        return store.get(postId);
    }

    @Override
    public List<Post> findAll(PostSearchCond cond) {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<Post> findByMemberIdAll(Long memberId) {
        return findAll(null).stream()
                .filter(post -> post.getMemberId().equals(memberId))
                .collect(Collectors.toList());
//                .orElseThrow(() -> new RuntimeException("해당 회원의 작성글이 존재하지 않습니다"));
    }


    @Override
    public void editPost(Long postId, Post postEditDto) {
        Post findPost = findByPostId(postId);
        findPost.setPostTitle(postEditDto.getPostTitle());
        findPost.setPostContent(postEditDto.getPostContent());
        findPost.setCategory(postEditDto.getCategory());
        findPost.setLastEditDate(LocalDateTime.now());
    }


    @Override
    public void deletePost(Long postId) {
        store.remove(postId);
    }

    @Override
    public void addViewCount(Long postId) {

    }

    @Override
    public void recommendPost(Long postId, Long memberId) {

    }

    @Override
    public boolean recommendDuplicateCheck(Long postId, Long memberId) {
        return false;
    }

    public void clear() {
        sequence = 0;
        store.clear();
    }

}
