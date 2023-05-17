package com.springboot.blog.service;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto postDto);
    Long createPost2(PostDto postDto);
    PostDto getPost(Long postId);
    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);
    PostDto updatePost(PostDto postDto, long id);
    Long deletePostById (long id);
}
