package com.example.projecte_fitxar;

public class User {

    private String key;
    private String name;
    private String surname;
    private String email;
    private String password;

    public User() {
    }

    public User(String key, String name, String surname, String email, String password) {
        this.key = key;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
