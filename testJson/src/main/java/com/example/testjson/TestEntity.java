package com.example.testjson;

import javax.persistence.*;

@Entity
@Table(name = "tbl_test")
public class TestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String bio;
    private Integer slider;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Integer getSlider() {
        return slider;
    }

    public void setSlider(Integer slider) {
        this.slider = slider;
    }
}
