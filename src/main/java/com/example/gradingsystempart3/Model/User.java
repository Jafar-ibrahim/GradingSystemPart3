package com.example.gradingsystempart3.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    protected int id;
    protected String username;
    protected String password;
    protected String firstName;
    protected String LastName;
    protected Role role;

    public User(String username, String password, String firstName, String lastName, Role role) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        LastName = lastName;
        this.role = role;
    }
}