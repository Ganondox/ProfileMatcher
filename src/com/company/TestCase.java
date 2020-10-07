package com.company;

import java.util.List;

public class TestCase {

    List<ProfileVector> total_employers;
    List<ProfileVector> other_users;
    int apt_n;
    int int_m;
    ProfileVector target_user;

    public TestCase(List<ProfileVector> total_employers, List<ProfileVector> other_users, int apt_n, int int_m, ProfileVector target_user) {
        this.total_employers = total_employers;
        this.other_users = other_users;
        this.apt_n = apt_n;
        this.int_m = int_m;
        this.target_user = target_user;
    }
}
