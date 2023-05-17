package com.springboot.blog.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class BlogApiException extends RuntimeException{
    private HttpStatus status;
    private String message;

    public BlogApiException (String message, HttpStatus status, String message1){
        super(message);
        this.status=status;
        this.message=message1;
    }
}
