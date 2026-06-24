/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.databaseassignment;

/**
 *
 * @author Jameel
 */
import java.sql.*;
import java.util.Vector;

public class DatabaseManager {

    private Connection link;
    private Statement statement;
    private ResultSet results;

    public void connect(String dbPath) throws ClassNotFoundException, SQLException {
        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        String url = "jdbc:ucanaccess://" + dbPath;
        link = DriverManager.getConnection(url);
        statement = link.createStatement(
                ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_READ_ONLY
        );
    }

    public Vector<String> getTableNames() throws SQLException {
        Vector<String> tableNames = new Vector<String>();
        DatabaseMetaData meta = link.getMetaData();
        results = meta.getTables(null, null, "%", new String[]{"TABLE"});
        while (results.next()) {
            String name = results.getString("TABLE_NAME");
            if (!name.startsWith("MSys") && !name.startsWith("UCA_")) {
                tableNames.add(name);
            }
        }
        return tableNames;
    }

    public Vector<Vector<Object>> getTableData(String tableName) throws SQLException {
        Vector<Vector<Object>> rows = new Vector<Vector<Object>>();

        String query = "SELECT * FROM " + tableName;
        results = statement.executeQuery(query);

        ResultSetMetaData metaData = results.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (results.next()) {
            Vector<Object> row = new Vector<Object>();
            for (int i = 1; i <= columnCount; i++) {
                row.add(results.getObject(i));
            }
            rows.add(row);
        }
        return rows;
    }

    public int getColumnCount(String tableName) throws SQLException {
        String query = "SELECT * FROM " + tableName;
        results = statement.executeQuery(query);
        ResultSetMetaData metaData = results.getMetaData();
        return metaData.getColumnCount();
    }

    public void disconnect() throws SQLException {
        if (link != null && !link.isClosed()) {
            link.close();
        }
    }

    public boolean isConnected() {
        try {
            return link != null && !link.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}