package com.example.sweater.domain;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Пожалуйста, заполни поле")
    @Length(max = 2048, message = "Слишком длинное, сократи :С")
    private String text;

    private String tag;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    private String filename;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "message_likes",
            joinColumns = { @JoinColumn(name = "message_id") },
            inverseJoinColumns = { @JoinColumn (name = "user_id")})
    private Set<User> likesFrom = new HashSet<>();




    public Message() {

    }

    public Set<User> getLikesFrom() {
        return likesFrom;
    }

    public int getCountLikes() {
        return likesFrom.size();
    }

    public void setLikesFrom(Set<User> messageLikesFromUsers) {
        this.likesFrom = messageLikesFromUsers;
    }

    public Message(String text, String tag) {
        this.text = text;
        this.tag = tag;
    }


    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getTag() {
        return tag;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
