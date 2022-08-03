package com.jdbc.demo;

import java.sql.*;

/**
 * @author ldx
 * @date 2022/8/2
 */
public class JdbcMetaDataDemo {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        String schema = "mall";
        String database = "mall";
        String table = "oms_order";
        try (Connection connection = getConnection(schema);
             ResultSet columns = getColumns(connection, database, table)) {
            while (columns.next()) {
                String typeName = columns.getString("TYPE_NAME");
                String columnSize = columns.getString("COLUMN_SIZE");
                String decimalDigits = columns.getString("DECIMAL_DIGITS");
                String columnName = columns.getString("COLUMN_NAME");
                String remarks = columns.getString("REMARKS");
                String isNullable = columns.getString("IS_NULLABLE");
                String columnDef = columns.getString("COLUMN_DEF");
                String format = String.format("columnName=%s;typeName=%s;columnSize=%s;decimalDigits=%s;isNullable=%s;columnDef=%S;remarks=%s",
                        columnName, typeName, columnSize, decimalDigits, isNullable, columnDef, remarks);
                System.out.println(format);

            }
        }
    }

    public static Connection getConnection(String schema) throws ClassNotFoundException, SQLException {
        String driver = "com.mysql.jdbc.Driver";
        String username = "mall";
        String password = "Mall@#!";
        String url = "jdbc:mysql://81.70.143.127:3306";

        Class.forName(driver);
        Connection connection = DriverManager.getConnection(url, username, password);
        connection.setSchema(schema);

        return connection;
    }

    public static ResultSet getColumns(Connection connection, String database, String table) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        return metaData.getColumns(database, database, table, "%");

    }
}
