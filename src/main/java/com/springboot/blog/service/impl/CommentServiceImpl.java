package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogApiException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    private final ModelMapper mapper;

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Comment comment = mapToEntity(commentDto);
        //retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        comment.setPost(post);
        Comment newComment = commentRepository.save(comment);
        return mapToDto(newComment);
    }

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
        //retrieve comments by postI
        List<Comment> comments = commentRepository.findByPostId(postId);
        //convert list of comment entities to list of comment dto's
        return  comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {
        //retrieve post entity by id
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new ResourceNotFoundException("post","id",postId));
        //retrieve comment by id
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new ResourceNotFoundException("comment","id", commentId));

        if(comment.getPost().getId()!=post.getId()){
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"comment does not belong to post");
        }

        return mapToDto(comment);
    }

    @Override
    public CommentDto updateComment(Long postId, Long commentId, CommentDto commentRequest) {

        Post post = postRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Post", "id", postId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment","id",commentId));

        if((comment.getPost().getId())!=((post.getId()))){
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment does not belong to a post");
        }

        comment.setName(commentRequest.getName());
        comment.setEmail(commentRequest.getEmail());
        comment.setBody(commentRequest.getBody());
        Comment updatedComment = commentRepository.save(comment);

        return mapToDto(updatedComment);
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        Post post = postRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Post", "id", postId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment","id",commentId));
        if((comment.getPost().getId())!=((post.getId()))){
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment does not belong to a post");
        }
        commentRepository.delete(comment);
    }


    private CommentDto mapToDto(Comment comment){
        return mapper.map(comment,CommentDto.class);
    }

    private Comment mapToEntity (CommentDto commentDto){
        return mapper.map(commentDto,Comment.class);
    }

}
