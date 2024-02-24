package com.example.gradingsystempart3.Model;

public class Admin extends User {
    private int adminId;
    public Admin(String username, String password, String firstName, String lastName) {
        super(username, password,firstName,lastName, Role.INSTRUCTOR);
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }


}
