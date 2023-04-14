package com.fyp1.assignment4.POJOS;

public class User {
    private String name;
    private String age;
    private String email;
    private String password;
    private boolean isAdmin;

    public User() {}

    public User(String name, String age, String email, String password) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.password = password;
        this.isAdmin = false;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}

