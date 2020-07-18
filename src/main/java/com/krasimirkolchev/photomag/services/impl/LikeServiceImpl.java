package com.krasimirkolchev.photomag.services.impl;

import com.krasimirkolchev.photomag.models.entities.Like;
import com.krasimirkolchev.photomag.models.entities.User;
import com.krasimirkolchev.photomag.repositories.LikeRepository;
import com.krasimirkolchev.photomag.services.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;

    @Autowired
    public LikeServiceImpl(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    @Override
    public List<String> getWinners(String contestId) {
        List<Like> contestLikes = this.likeRepository.getAllByContest_Id(contestId);
        Map<String, Integer> stats = new LinkedHashMap<>();

        contestLikes.forEach(l -> {
            if (!stats.containsKey(l.getUser().getId())) {
                stats.put(l.getUser().getId(), 0);
            }
            stats.put(l.getUser().getId(), stats.get(l.getUser().getId()) + 1);
        });

        List<String> users = stats.entrySet()
                .stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .map(Map.Entry::getKey)
                .limit(3)
                .collect(Collectors.toList());

        return users;
    }
}
