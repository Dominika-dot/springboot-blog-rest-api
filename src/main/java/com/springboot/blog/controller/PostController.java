package com.springboot.blog.controller;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.service.PostService;
import com.springboot.blog.utils.AppConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    //create blog post
    @PostMapping
    //@Valid -> validation annotation
    public ResponseEntity<Void> createPost (@Valid @RequestBody PostDto postDto){ //@RequestBody - convert JSON to Java DTO
        //return new ResponseEntity(postService.createPost2(postDto), HttpStatus.CREATED);
        Long id = postService.createPost2(postDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(uri).build();
    }


    //getAllPosts API
    @GetMapping
    public PostResponse getAllPosts (@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                     @RequestParam (value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                     @RequestParam (value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false)String sortBy,
                                     @RequestParam (value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir){

        var post = postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
        return post;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPost (@PathVariable("id")Long postId){
        return ResponseEntity.ok(postService.getPost(postId));
    }

    @PostMapping ("/{id}")
    public ResponseEntity<PostDto> updatePost (@Valid @RequestBody PostDto postDto, @PathVariable ("id") long postId){
        return ResponseEntity.ok(postService.updatePost(postDto,postId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost (@PathVariable ("id") long postId){
        postService.deletePostById(postId);
        return ResponseEntity.ok().body(String.format("Post entity nr %s delected successfully",postId));
    }
}
