package com.sparta.first_project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.first_project.dto.*;
import com.sparta.first_project.entity.User;
import com.sparta.first_project.error.ParameterValidationException;
import com.sparta.first_project.jwt.JwtUtil;
import com.sparta.first_project.security.UserDetailsImpl;
import com.sparta.first_project.service.KakaoService;
import com.sparta.first_project.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final KakaoService kakaoService;


    @Operation(hidden = true)
    @PostMapping("/signup")
    public ResponseEntity<BaseResponse> signup(@RequestBody @Valid SignupRequestDto requestDto,
                                               BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError e : fieldErrors) {
            throw new ParameterValidationException(e.getDefaultMessage());
        }
        userService.signup(requestDto);
        return ResponseEntity.ok().body(new SuccessResponse("회원 가입 완료"));
    }

    @GetMapping("/profile")
    public ResponseEntity<BaseResponse> getProfile(@RequestParam String username) {
        User user = userService.getProfile(username);
        return ResponseEntity.ok().body(new SuccessResponse("회원정보 조회 성공", user));
    }

    @PutMapping("/profile")
    public ResponseEntity<BaseResponse> updateProfile(@RequestBody @Valid ProfileRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            String username = userDetails.getUsername();
            User user = userService.findByUsername(username);

            if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }

            user.setUsername(requestDto.getUsername());
            user.setEmail(requestDto.getEmail());
            String newPassword = requestDto.getNewPassword();
            if (newPassword != null && !newPassword.isEmpty()) {
                user.setPassword(passwordEncoder.encode(newPassword));
            }
            user.setIntro(requestDto.getIntro());

            userService.update(user);

            return ResponseEntity.ok().body(new SuccessResponse("회원정보 수정 성공"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage()));
        }
    }

    @DeleteMapping("{id}/delete")
    public ResponseEntity<BaseResponse> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String username = userDetails.getUsername();
        User currentUser = userService.findByUsername(username);

        if (!currentUser.getId().equals(id)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        userService.delete(id);
        return ResponseEntity.ok().body(new SuccessResponse("회원 탈퇴 성공"));
    }

    @GetMapping("/kakao/callback")
    public String kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        String token = kakaoService.kakaoLogin(code);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);

        return "redirect:/";
    }

    @GetMapping("/user-info")
    @ResponseBody
    public UserInfoDto getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUser().getUsername();
        return new UserInfoDto(username);
    }

}