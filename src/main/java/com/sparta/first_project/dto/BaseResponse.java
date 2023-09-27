package com.sparta.first_project.dto;

import com.sparta.first_project.entity.Post;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseResponse<T> {

    private HttpStatus statusNum;
    private String message;
    private T data;

    public BaseResponse(HttpStatus statusNum, String message) {
        this.statusNum = statusNum;
        this.message = message;
    }

    public BaseResponse(HttpStatus statusNum, String message, T data) {
        this.statusNum = statusNum;
        this.message = message;
        this.data = data;
    }

    public BaseResponse(Post post) {
        this.statusNum = HttpStatus.OK;
        this.message = "게시글 작성 성공";
        this.data = (T) post;
    }
}
