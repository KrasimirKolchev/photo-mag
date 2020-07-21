package com.krasimirkolchev.photomag.services.impl;

import com.krasimirkolchev.photomag.models.bindingModels.ContestCreateBindingModel;
import com.krasimirkolchev.photomag.models.entities.Contest;
import com.krasimirkolchev.photomag.models.entities.User;
import com.krasimirkolchev.photomag.repositories.ContestRepository;
import com.krasimirkolchev.photomag.services.ContestService;
import com.krasimirkolchev.photomag.services.LikeService;
import com.krasimirkolchev.photomag.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ContestServiceImpl implements ContestService {
    private final ContestRepository contestRepository;
    private final UserService userService;
    private final CloudinaryServiceImpl cloudinaryService;
    private final LikeService likeService;
    private final ModelMapper modelMapper;

    @Autowired
    public ContestServiceImpl(ContestRepository contestRepository, UserService userService, CloudinaryServiceImpl cloudinaryService
            , LikeService likeService, ModelMapper modelMapper) {
        this.contestRepository = contestRepository;
        this.userService = userService;
        this.cloudinaryService = cloudinaryService;
        this.likeService = likeService;
        this.modelMapper = modelMapper;
    }


//    @Override
//    public List<Contest> getActiveContests() {
//        return this.contestRepository.getAllByEndDateIsAfterOrderByStartDate(LocalDateTime.now());
//    }

    @Override
    public Contest getContestById(String id) {
        return this.contestRepository.findById(id).orElse(null);
    }

    @Override
    public Contest createContest(ContestCreateBindingModel contestCreateBindingModel) {

        Contest contest = this.modelMapper.map(contestCreateBindingModel, Contest.class);
        contest.setGalleryPhotos(new ArrayList<>());

        this.contestRepository.saveAndFlush(contest);

        return contest;
    }

    @Override
    public boolean addPhotoToContest(String contestId, String photoId, String username) {
        //Principal getName
        Contest contest = this.contestRepository.getOne(contestId);
        User user = this.userService.getUserByUsername(username);

        if (!contest.getUsers().contains(user)) {

//            contest.getGalleryPhotos().add(this.photoService.getPhotoById(photoId));
            contest.getUsers().add(user);
            this.contestRepository.save(contest);
            return true;
        }

        return false;
    }

    @Override
    public Set<User> getContestWinners(String contestId) {
//        Contest contest = this.contestRepository.getOne(contestId);

        Set<User> winners = this.likeService.getWinners(contestId)
                .stream()
                .map(this.userService::getUserById)
                .collect(Collectors.toSet());

        return winners;
    }


}
