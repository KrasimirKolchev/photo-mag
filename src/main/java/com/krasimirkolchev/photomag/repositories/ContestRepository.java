package com.krasimirkolchev.photomag.repositories;

import com.krasimirkolchev.photomag.models.entities.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ContestRepository extends JpaRepository<Contest, String> {
    List<Contest> getAllByEndDateIsAfterOrderByStartDate(LocalDateTime endDate);

}
