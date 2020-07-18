package com.krasimirkolchev.photomag.repositories;

import com.krasimirkolchev.photomag.models.entities.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, String> {

    List<Like> getAllByContest_Id(String contestTitle);
}
