/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zholudzeu.csv.dal;

import java.io.File;
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
    private static final String DERBY_DRIVER =
            "org.apache.derby.jdbc.ClientDriver";
    
    private static PreparedStatement createStatement;
    private static PreparedStatement readStatement;
    private static PreparedStatement readAllStatement;
    private static PreparedStatement updateStatement;
    
    private static Connection dbconn;
    
    public static synchronized void loadCsvFile(File file) {
        try {
            //Class.forName(DERBY_DRIVER);
            DriverManager.registerDriver(
                    new org.apache.derby.jdbc.ClientDriver());
            dbconn = DriverManager.getConnection(DB_URL);
            FileParser parser = new FileParser(dbconn);
            parser.parse(file);
        } catch (SQLException e) {
            System.err.print(e.getMessage());
        } finally {
            DbUtils.closeQuietly(dbconn);
        }
    }
}
