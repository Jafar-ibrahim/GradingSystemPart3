package com.example.gradingsystempart3.Model;

public class UserDTO {
    private final String username;
    private final String firstName;
    private final String lastName;
    private final Role role;

    public UserDTO(String username, String firstName, String lastName, Role role) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public UserDTO(User user) {
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.role = user.getRole();
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Role getRole() {
        return role;
    }
}
