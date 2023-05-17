package com.springboot.blog.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private long id;
    //name should not be null or empty
    @NotEmpty (message = "Name should not be null or empty")
    private String name;
    //email should not be null or empty;
    @NotEmpty (message = "Email should not be null or empty")
    @Email
    private String email;
    @NotEmpty
    @Size(min = 10, message = "comment body must have minimum 10 characters")
    private String body;

}
