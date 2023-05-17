package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final ModelMapper mapper;


    @Override
    public Long createPost2(PostDto postDto) {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription());
        return postRepository.save(post).getId();

    }

    @Override
    public PostDto getPost(Long postId) {
        return postRepository.findById(postId)
                .map(this::mapToPostDto)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
    }

    @Override
    @Transactional
    public PostDto updatePost(PostDto postDto, long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        // podmien wszystkie elementy z postdto i save do db
        //post.builder().content(postDto.getContent()).build(); -> to nie zadziala bo builder zrobi nowy obiekt
        //toBuilder() -> tworzy też nowy obiekt na końcu
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        //postRepository.save(post); -> mamy @EnableTransactionalManagement w klasie głównej + tutaj @Transactional więc się samo zapisuje do db
        return mapToPostDto(post);
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        //create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Post> posts = postRepository.findAll(pageable);
        //get content for page objects
        List <Post> listOfPosts = posts.getContent();

        List<PostDto> content = listOfPosts.stream().map(this::mapToPostDto)
                .collect(Collectors.toList());
        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNumber(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setLast(posts.isLast());
        return postResponse;
    }

    @Override
    public Long deletePostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        postRepository.delete(post);
        return id;
    }

    @Override
    public PostDto createPost(PostDto postDto) {
        //convert DTO to entity and save in database
        Post newPost = postRepository.save(mapToEntity(postDto));
        //convert entity to DTO and return
        return mapToPostDto(newPost);
    }

    private PostDto mapToPostDto(Post post) {
        return mapper.map(post,PostDto.class);
    }

    private Post mapToEntity (PostDto postDto){
        return mapper.map(postDto,Post.class);
    }
}