package com.krasimirkolchev.photomag.models.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "posts")
public class Post extends BaseEntity {
    @Column(name = "topic", unique = true, nullable = false)
    private String title;
    @Column(name = "content", nullable = false)
    private String content;

    public Post() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
