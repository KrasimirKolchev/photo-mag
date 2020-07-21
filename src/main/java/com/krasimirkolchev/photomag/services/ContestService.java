package com.krasimirkolchev.photomag.services;

import com.krasimirkolchev.photomag.models.bindingModels.ContestCreateBindingModel;
import com.krasimirkolchev.photomag.models.entities.Contest;
import com.krasimirkolchev.photomag.models.entities.User;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface ContestService {

//    List<Contest> getActiveContests();

    Contest getContestById(String id);

    Contest createContest(ContestCreateBindingModel contestCreateBindingModel);

    boolean addPhotoToContest(String contestId, String photoId, String username) throws IOException;

    Set<User> getContestWinners(String contestId);
}
