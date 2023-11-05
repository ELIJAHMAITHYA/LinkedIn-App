package com.example.linkedinapp;

public class DataClass {
    private String name;
    private String email;
    private String password;
    private String phone_number;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getGender() {
        return gender;
    }

    public String getDescription() {
        return description;
    }

    public String getSkills() {
        return skills;
    }

    public String getImage() {
        return image;
    }

    private String gender;
    private String description;

    public DataClass(String name, String email, String password, String phone_number, String gender, String description, String skills, String image) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone_number = phone_number;
        this.gender = gender;
        this.description = description;
        this.skills = skills;
        this.image = image;
    }

    private String skills;
    private String image;

}
