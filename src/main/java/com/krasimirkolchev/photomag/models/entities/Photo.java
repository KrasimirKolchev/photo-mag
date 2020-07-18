package com.krasimirkolchev.photomag.models.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "photos")
public class Photo extends BaseEntity {
    private String imageUrl;
    private LocalDateTime createdAt;
    private User user;

    public Photo() {
        this.createdAt = LocalDateTime.now();
    }

    public Photo(String imageUrl) {
        this.imageUrl = imageUrl;
        this.createdAt = LocalDateTime.now();
    }

    @Column(name = "imageUrl")
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Column(name = "created_at")
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @ManyToOne(targetEntity = User.class)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
