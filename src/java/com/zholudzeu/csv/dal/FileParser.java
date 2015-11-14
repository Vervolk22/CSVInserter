/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zholudzeu.csv.dal;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author andrey
 */
public class FileParser {
    private Connection conn;
    private char separator = ',';
    
    public FileParser(Connection conn) {
        this.conn = conn;
    }
    
    public void setSeparator(char separator) {
        this.separator = separator;
    }
    
    public void parse(File file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file)); 
            String line;
            String[] array;
            /*PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO APP.USERS " + 
                    "(NAME, SURNAME, LOGIN, EMAIL, PHONENUMBER)" + 
                    "VALUES (?, ?, ?, ?, ?)", Statement.);*/
            PreparedStatement stmt = conn.prepareStatement(
                    "MERGE INTO APP.USERS A ON A.LOGIN = ?" +
                    "WHEN MATCHED THEN UPDATE SET" +
                    "A.NAME = ?, A.SURNAME = ?, A.EMAIL = ?, A.PHONENUMBER = ? " +
                    "WHEN NOT MATCHED THEN INSERT " +
                    //"(NAME, SURNAME, LOGIN, EMAIL, PHONENUMBER)" + 
                    "VALUES (?, ?, ?, ?, ?)");
            while ((line = br.readLine()) != null) {
                array = line.split(",");
                stmt.setString(1, array[2]);
                stmt.setString(2, array[0]);
                stmt.setString(3, array[1]);
                stmt.setString(4, array[3]);
                stmt.setString(5, array[4]);
                stmt.setString(6, array[1]);
                stmt.setString(7, array[2]);
                stmt.setString(8, array[3]);
                stmt.setString(9, array[4]);
                stmt.setString(10, array[5]);
                stmt.execute();
            }
            
        } 
        catch (SQLException | IOException e) {
            System.err.print(e.getMessage());
        }
        
    }
    
    public synchronized static void processCSVFile(File file) 
            throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                parseLine(line);
            }
        } 
        catch (IOException e) {
            System.err.print(e);
        }
    }
    
    private static void parseLine(String s) {
        String[] array = s.split(",");
    }
}
