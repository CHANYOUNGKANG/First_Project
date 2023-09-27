package com.sparta.first_project.service;

import com.sparta.first_project.dto.SignupRequestDto;
import com.sparta.first_project.entity.Post;
import com.sparta.first_project.entity.User;
import com.sparta.first_project.entity.UserRoleEnum;
import com.sparta.first_project.jwt.JwtUtil;
import com.sparta.first_project.repository.PostRepository;
import com.sparta.first_project.repository.UserRepository;
import com.sparta.first_project.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.sparta.first_project.entity.UserRoleEnum.ADMIN;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;
    private final JwtUtil jwtUtil;

    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public void signup(SignupRequestDto requestDto) {

        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        String email = requestDto.getEmail();
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email이 존재합니다.");
        }

        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 유효하지 않아 등록이 불가능합니다.");
            }
            role = ADMIN;
        }

        User user = User.builder().username(username).password(password).email(email).intro(requestDto.getIntro()).role(role).build();
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User getProfile(String username) {
        if (username == null) {
            throw new IllegalArgumentException("사용자 이름을 입력해 주세요.");
        }

        return findByUsername(username);
    }

    @Transactional
    public void update(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void delete(Long id) {
        // 사용자를 ID로 검색
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<Post> userPosts = postRepository.findByAuthor(user.getUsername());

        postRepository.deleteAll(userPosts);

        userRepository.delete(user);
    }



    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자 이름의 회원을 찾을 수 없습니다. 사용자 이름: " + username));
    }

    private User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("인증된 사용자 정보를 가져올 수 없습니다.");
        }
        return ((UserDetailsImpl) authentication.getPrincipal()).getUser();
    }
}