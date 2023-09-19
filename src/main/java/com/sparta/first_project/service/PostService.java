package com.sparta.first_project.service;

import com.sparta.first_project.dto.PostRequestDto;
import com.sparta.first_project.dto.PostResponseDto;
import com.sparta.first_project.entity.Likes;
import com.sparta.first_project.entity.Post;
import com.sparta.first_project.entity.User;
import com.sparta.first_project.repository.CommentRepository;
import com.sparta.first_project.repository.LikesRepository;
import com.sparta.first_project.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.sparta.first_project.entity.UserRoleEnum.ADMIN;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikesRepository likesRepository;

    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto, User user) {
        // DB 저장
        postRequestDto.addAuthor(user.getUsername());
        Post post = new Post(postRequestDto);
        Post savedpost = postRepository.save(post);
        return new PostResponseDto(savedpost);
    }
    // 전체조회
    public List<PostResponseDto> findAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream().map(PostResponseDto::new)
                .toList();
    }
    // 단일 조회
    public PostResponseDto findPostById(Long id) {
        Post findPost = findPost(id);
        return new PostResponseDto(findPost);
    }

    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto postRequestDto, User user) {
        Post findPost = findPost(id);

        // 수정하려는 user의 권한이 ADMIN 이거나 user가 게시글의 작성자 일 경우.
        if (user.getRole().equals(ADMIN) || findPost.getAuthor().equals(user.getUsername())) {
            findPost.update(postRequestDto);
            return new PostResponseDto(findPost);
        }
        throw new IllegalArgumentException("작성자만 수정이 가능합니다.");
    }

    @Transactional
    public Long deletePost(Long id, User user) {
        Post findPost = findPost(id);

        // 삭제하려는 user의 권한이 ADMIN 이거나 user가 게시글의 작성자 일 경우.
        if (user.getRole().equals(ADMIN) || findPost.getAuthor().equals(user.getUsername())) {
            commentRepository.deleteAll(findPost.getCommentList());
            likesRepository.deleteAll(findPost.getLikesList());
            likesRepository.deleteAll(findPost.getLikesList());
            postRepository.delete(findPost);
            return id;
        }
        throw new IllegalArgumentException("작성자만 삭제가 가능합니다.");
    }

    @Transactional
    public String likePostToggle(Long id, User user) {
        Post findPost = findPost(id);
        Optional<Likes> findLikes = likesRepository.findByPostAndUser(findPost, user);

        String responseMessage = "";

        // 이미 유저가 해당 게시물을 좋아요 했을 경우
        if (findLikes.isPresent()) {
            likesRepository.delete(findLikes.get());
            responseMessage = "좋아요 취소 성공";
            return responseMessage;
        }
        // 유저가 해당 게시물을 좋아요 하지 않았을 경우
        Likes likes = new Likes(findPost, user);
        likesRepository.save(likes);
        responseMessage = "좋아요 성공";
        return responseMessage;
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() -> {
            throw new IllegalArgumentException("해당 id의 게시물이 존재하지 않습니다. Post ID: " + id);
        });
    }
}
