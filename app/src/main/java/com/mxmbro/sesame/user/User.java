package com.mxmbro.sesame.user;

public class User {
    private String Name;
    private String Password;
    private String Email;
    private String Phone;
    private String ID;

    public User(String name, String password, String email, String phone) {
        Name = name;
        Password = password;
        Email = email;
        Phone = phone;
    }

    public void setID(String id){
        ID = id;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }

    public String getPhone() {
        return Phone;
    }
}
