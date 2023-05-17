package com.springboot.blog.payload;

import java.util.Set;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostDto {

    long id;
    //title should not be null or empty; should have at least 2 characters
    @NotEmpty
    @Size (min=2, message = "Post title should have at least 2 characters")
    String title;
    //description should not be null or empty, should have at least 10 characters
    @NotEmpty
    @Size (min=10, message = "Post desctiption should have at least 10 characters")
    String description;
    //post content should not be null or empty
    @NotEmpty
    String content;
    private Set<CommentDto> comments;

}
