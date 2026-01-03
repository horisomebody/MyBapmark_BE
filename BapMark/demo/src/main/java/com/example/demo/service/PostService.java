package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.repository.PostRepository;
import com.example.demo.requestDto.PostRequestDto;
import com.example.demo.responseDto.PostResponseDto;
import com.example.demo.domain.Post;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;


    // 게시글 작성
    @Transactional
    public void createPost(PostRequestDto dto, User user) {
        Post post = new Post();

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setAddress(dto.getAddress());
        post.setLatitude(dto.getLatitude());
        post.setLongitude(dto.getLongitude());
        post.setPlaceName(dto.getPlaceName());

        post.setUser(user); // 🔐 작성자 정보 저장 (매우 중요)

        postRepository.save(post);
    }


    // 게시글 수정
    @Transactional
    public void updatePost(Long postId, PostRequestDto dto, User user) throws AccessDeniedException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // ✅ 작성자 검증
        if (!post.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("해당 게시글을 수정할 권한이 없습니다.");
        }

        // ✅ 수정
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setAddress(dto.getAddress());
        post.setLatitude(dto.getLatitude());
        post.setLongitude(dto.getLongitude());
    }


    // 게시글 삭제
    @Transactional
    public void deletePost(Long postId, User user) throws AccessDeniedException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // ✅ 작성자 검증
        if (!post.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("해당 게시글을 삭제할 권한이 없습니다.");
        }

        // ✅ 삭제
        postRepository.delete(post);
    }


    // 모든 게시글 가져오기
    public List<PostResponseDto> getAllPosts() {
        return postRepository.findAll().stream()
                .map(PostResponseDto::new)
                .toList();
    }

    public List<Post> searchPosts(String keyword) { // 검색 기능
        return postRepository.findByTitleContainingOrContentContaining(keyword, keyword);
    }

    public List<Post> getPostsByUser(User user) {
        return postRepository.findAllByUser(user);
    }

}
