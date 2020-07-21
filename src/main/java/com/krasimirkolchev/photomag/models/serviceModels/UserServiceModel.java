package com.krasimirkolchev.photomag.models.serviceModels;

import java.time.LocalDateTime;

public class UserServiceModel extends BaseServiceModel {
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String reward;//can be entity
    private String contestPhoto;

    public UserServiceModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getContestPhoto() {
        return contestPhoto;
    }

    public void setContestPhoto(String contestPhoto) {
        this.contestPhoto = contestPhoto;
    }
}
