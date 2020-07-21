package com.krasimirkolchev.photomag.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "likes")
public class Like extends BaseEntity {
    private User user;
    private String photo;
    private Contest contest;

    public Like() {
    }

    @ManyToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "photo")
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @OneToOne
    public Contest getContest() {
        return contest;
    }

    public void setContest(Contest contest) {
        this.contest = contest;
    }

}
