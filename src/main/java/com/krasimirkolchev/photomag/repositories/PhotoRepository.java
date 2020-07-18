package com.krasimirkolchev.photomag.repositories;

import com.krasimirkolchev.photomag.models.entities.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, String> {

    List<Photo> findAllByUser_Id(String id);
}
