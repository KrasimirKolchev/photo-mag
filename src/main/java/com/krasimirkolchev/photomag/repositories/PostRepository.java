package com.krasimirkolchev.photomag.repositories;

import com.krasimirkolchev.photomag.models.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Column;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {
}
