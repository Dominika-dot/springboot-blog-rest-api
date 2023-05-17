package com.springboot.blog.repository;

import com.springboot.blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

//@Repository -> no need to do this here
public interface PostRepository extends JpaRepository <Post, Long>{ // long to ID tabeli posts
    //spring data JPA internally provides all CRUD implementations , np. findAll, findAllByid etc.
}
