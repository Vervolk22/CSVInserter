/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zholudzeu.csv.dal;

import java.util.ArrayList;
import java.io.File;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import org.apache.commons.dbutils.DbUtils;

/**
 *
 * @author andrey
 */
public abstract class DAO {    
    private static final String DB_URL = 
            "jdbc:derby://localhost:1527/CsvDb;create=true";
    
    private static PreparedStatement readStmt;
    private static PreparedStatement countStmt;
    private static ResultSet results;
    
    private static Connection dbconn;
    
    static {
        try {
        DriverManager.registerDriver(
                    new org.apache.derby.jdbc.ClientDriver());
        }
        catch (SQLException e) {
            System.err.print(e.getMessage());
        }
    }
    
    public static synchronized void loadCsvFile(File file) {
        try {
            dbconn = DriverManager.getConnection(DB_URL);
            FileParser parser = new FileParser(dbconn);
            parser.parse(file);
        } catch (SQLException e) {
            System.err.print(e.getMessage());
        } finally {
            DbUtils.closeQuietly(dbconn);
        }
    }
    
    public static ArrayList<User> getRecords(int page, int recordsByPage, int orderBy) {
        try {
            String part;
            switch (orderBy) {
                case 2: part = "ID DESC";break;
                case 3: part = "NAME ASC";break;
                case 4: part = "NAME DESC";break;
                case 5: part = "SURNAME ASC";break;
                case 6: part = "SURNAME DESC";break;
                case 7: part = "LOGIN ASC";break;
                case 8: part = "LOGIN DESC";break;
                case 9: part = "EMAIL ASC";break;
                case 10: part = "EMAIL DESC";break;
                case 11: part = "PHONENUMBER ASC";break;
                case 12: part = "PHONENUMBER DESC";break;
                default: part = "ID ASC";break;                
            }
            dbconn = DriverManager.getConnection(DB_URL);
            readStmt = dbconn.prepareStatement(
                    "SELECT * FROM APP.USERS ORDER BY " + part
                  + " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
            //readStmt.setMaxRows(recordsByPage);
            readStmt.setInt(1, (page - 1) * recordsByPage);
            readStmt.setInt(2, recordsByPage);
            results = readStmt.executeQuery();
            
            ArrayList<User> list = new ArrayList();
            User user;
            while (results.next()) {
                user = new User(results.getInt("ID"), 
                        results.getString("NAME"),
                        results.getString("SURNAME"),
                        results.getString("LOGIN"),
                        results.getString("EMAIL"),
                        results.getString("PHONENUMBER"));
                list.add(user);
            }
            return list;
        } catch (SQLException e) {
            System.err.print(e.getMessage());
            } finally {
            DbUtils.closeQuietly(dbconn);
            DbUtils.closeQuietly(readStmt);
            DbUtils.closeQuietly(results);
        }
        return null;
    }
    
    public static int getRecordsCount() {
        try {
            dbconn = DriverManager.getConnection(DB_URL);
            readStmt = dbconn.prepareStatement(
                    "SELECT COUNT(ID) FROM APP.USERS");
            results = readStmt.executeQuery();
            int count = 0;
            while (results.next()) {
                count = results.getInt(1);
            }
            return count;
        } catch (SQLException e) {
            System.err.print(e.getMessage());
            } finally {
            DbUtils.closeQuietly(dbconn);
            DbUtils.closeQuietly(readStmt);
            DbUtils.closeQuietly(results);
        }
        return 0;
    }
}
