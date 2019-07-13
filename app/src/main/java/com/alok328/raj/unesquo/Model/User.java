package com.alok328.raj.unesquo.Model;

public class User  {
    private String username;
    private String Password;


    public User() {
    }

    public User(String password) {
        Password = password;
    }

    public User(String name, String password) {
        username = name;
        Password = password;
    }

    public String getName() {
        return username;
    }

    public void setName(String name) {
        username = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}