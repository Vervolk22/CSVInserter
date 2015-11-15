/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zholudzeu.csv.dal;

/**
 *
 * @author andrey
 */
public class User {
    public int id;
    public String name;
    public String surname;
    public String login;
    public String email;
    public String phoneNumber;
    
    public User(int id, String name, String surname, String login,
            String email, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
