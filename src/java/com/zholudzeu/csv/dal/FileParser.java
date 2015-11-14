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
import org.apache.derby.shared.common.error.
        DerbySQLIntegrityConstraintViolationException;
import org.apache.commons.dbutils.DbUtils;

/**
 *
 * @author andrey
 */
public class FileParser {
    private Connection conn;
    private String separator = ",";
    
    public FileParser(Connection conn) {
        this.conn = conn;
    }
    
    public void setSeparator(String separator) {
        this.separator = separator;
    }
    
    public void parse(File file) {
        PreparedStatement insertStmt = null;
        PreparedStatement updateStmt = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file)); 
            String line;
            String[] array;
            int result = -1;
            insertStmt = conn.prepareStatement(
                    "INSERT INTO APP.USERS "
                  + "(NAME, SURNAME, LOGIN, EMAIL, PHONENUMBER) "
                  + "VALUES (?, ?, ?, ?, ?)");
            updateStmt = conn.prepareStatement(
                    "UPDATE APP.USERS SET "
                  + "NAME = ?, SURNAME = ?, EMAIL = ?, PHONENUMBER = ? "
                  + "WHERE LOGIN = ?");
            while ((line = br.readLine()) != null) {
                array = line.split(separator);
                insertStmt.setString(1, array[0]);
                insertStmt.setString(2, array[1]);
                insertStmt.setString(3, array[2]);
                insertStmt.setString(4, array[3]);
                insertStmt.setString(5, array[4]);
                try {
                    insertStmt.executeUpdate();
                } catch (DerbySQLIntegrityConstraintViolationException e) {
                    updateStmt.setString(1, array[0]);
                    updateStmt.setString(2, array[1]);
                    updateStmt.setString(3, array[3]);
                    updateStmt.setString(4, array[4]);
                    updateStmt.setString(5, array[2]);
                    updateStmt.executeUpdate();
                }
            }
            
        } 
        catch (SQLException | IOException e) {
            System.err.print(e.getMessage());
        }
        finally {
            DbUtils.closeQuietly(insertStmt);
            DbUtils.closeQuietly(updateStmt);
        }
        
    }
}
