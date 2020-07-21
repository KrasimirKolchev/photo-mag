package com.krasimirkolchev.photomag.models.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "contests")
public class Contest extends BaseEntity {
    private String title;
    private String description;
    private Date startDate;
    private Integer duration;
    private String reward;//can be entity
    private String contestPhoto;
    private List<String> galleryPhotos;
    private Set<User> users;

    public Contest() {
    }

    @Column(name = "title", nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "description", nullable = false)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "start_date", nullable = false)
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Column(name = "duration", nullable = false)
    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Column(name = "reward", nullable = false)
    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    @Column(name = "contest_photo")
    public String getContestPhoto() {
        return contestPhoto;
    }

    public void setContestPhoto(String contestPhoto) {
        this.contestPhoto = contestPhoto;
    }

    @ElementCollection
    public List<String> getGalleryPhotos() {
        return galleryPhotos;
    }

    public void setGalleryPhotos(List<String> galleryPhotos) {
        this.galleryPhotos = galleryPhotos;
    }

    @OneToMany
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

}
