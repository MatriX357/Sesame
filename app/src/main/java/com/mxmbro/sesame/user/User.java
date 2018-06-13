package com.mxmbro.sesame.user;

public class User {
    private String Email;
    private String ID;
    private String Name;
    private String Password;
    private String Phone;

    public User(String name, String password, String email, String phone) {
        Name = name;
        Password = password;
        Email = email;
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    public String getPassword() {
        return Password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setID(String id){
        ID = id;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
