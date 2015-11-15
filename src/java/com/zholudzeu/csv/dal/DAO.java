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
 * Handles all work with DB.
 * @author andrey
 */
public abstract class DAO {    
    private static final String DB_URL = 
            "jdbc:derby://localhost:1527/CsvDb;create=true";
    
    private static PreparedStatement readStmt;
    private static PreparedStatement countStmt;
    private static ResultSet results;
    
    private static Connection dbconn;
    
    /**
     * Registration of apache derby jdbc driver.
     */
    static {
        try {
        DriverManager.registerDriver(
                new org.apache.derby.jdbc.ClientDriver());
        }
        catch (SQLException e) {
            System.err.print(e.getMessage());
        }
    }
    
    /**
     * Preparations to parse the file.
     * @param file File to process.
     */
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
    
    /**
     * Gets some ordered records from db.
     * @param page Page to show.
     * @param recordsByPage Records to show in a single page.
     * @param orderBy Number, represents the column and asc/desc ordering.
     * @return ArrayList<User> with "recordsByPage" length, or less if 
     * its the last page.
     */
    public static ArrayList<User> getRecords(int page, int recordsByPage, int orderBy) {
        try {
            // Get string, representing the ordering.
            String part;
            
            dbconn = DriverManager.getConnection(DB_URL);
            // Complete the query to the DB.
            readStmt = dbconn.prepareStatement(
                    "SELECT * FROM APP.USERS ORDER BY " + 
                    getOrderingString(orderBy) +
                    " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
            //readStmt.setMaxRows(recordsByPage);
            readStmt.setInt(1, (page - 1) * recordsByPage);
            readStmt.setInt(2, recordsByPage);
            results = readStmt.executeQuery();
            
            // Get rusults into the ArrayList.
            ArrayList<User> list = new ArrayList();
            User user;
            while (results.next()) {
                list.add(parseResults(results));
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
    
    /**
     * Get part of query, responsible for ordering.
     * @param orderBy Number, that represents ordering.
     * @return Part of query, responsible for irdering.
     */
    protected static String getOrderingString(int orderBy) {
        switch (orderBy) {
            case 2: return "ID DESC";
            case 3: return "NAME ASC";
            case 4: return "NAME DESC";
            case 5: return "SURNAME ASC";
            case 6: return "SURNAME DESC";
            case 7: return "LOGIN ASC";
            case 8: return "LOGIN DESC";
            case 9: return "EMAIL ASC";
            case 10: return "EMAIL DESC";
            case 11: return "PHONENUMBER ASC";
            case 12: return "PHONENUMBER DESC";
            default: return "ID ASC";                
        }
    }
    
    /**
     * Parse ResultSet into User object.
     * @param results ResultSet to parse.
     * @return User object.
     * @throws SQLException When ResultSet doesn't hold User data. 
     */
    protected static User parseResults(ResultSet results) 
            throws SQLException {
        return new User(results.getInt("ID"), 
                results.getString("NAME"),
                results.getString("SURNAME"),
                results.getString("LOGIN"),
                results.getString("EMAIL"),
                results.getString("PHONENUMBER"));
    }
    
    /**
     * Returns number of records in DB.
     * @return Number of records in DB.
     */
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
        } 
        finally {
            DbUtils.closeQuietly(dbconn);
            DbUtils.closeQuietly(readStmt);
            DbUtils.closeQuietly(results);
        }
        return 0;
    }
}
