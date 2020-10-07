package com.company;

public class UserRecord {

    String username;
    String password;
    String description;
    boolean isEmployer;
    ProfileVector profile;

    public UserRecord(String username, String password, String description, boolean isEmployer, ProfileVector profile) {
        this.username = username;
        this.password = password;
        this.description = description;
        this.isEmployer = isEmployer;
        this.profile = profile;
    }
}
