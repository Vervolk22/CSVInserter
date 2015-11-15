/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zholudzeu.csv.dal;

/**
 * Represents a single record in DB or scv file.
 * @author andrey
 */
public class User {
    public int id;
    public String name;
    public String surname;
    public String login;
    public String email;
    public String phoneNumber;
    
    /**
     * User constructor.
     * @param id Id of user.
     * @param name Name of user.
     * @param surname Surname of user.
     * @param login Login of user.
     * @param email Email of user.
     * @param phoneNumber  Phone number of user.
     */
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
