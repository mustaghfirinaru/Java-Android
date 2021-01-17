package com.example.uasa11201811347;

public class Item {
    public String username;
    public String pass;
    public Item() {

    }
    public Item(String username, String pass) {
        this.username = username;
        this.pass = pass;
    }

    public String getPass() {
        return pass;
    }
    public String getUsername() {
        return username;
    }
}