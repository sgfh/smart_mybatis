package com.smart.mybatis.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSourceUtil {
    /**
     16      * 注册数据库驱动
     17      */
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 27      * 获取数据源
     * 28      *
     * 29      * @throws SQLException
     * 30
     */
    public static Connection getConnection(String url, String user,
                                           String password) throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * 37      * 关闭数据源
     * 38      *
     * 39      * @throws SQLException
     * 40
     */
    public static void closeConnection(Connection conn) throws SQLException {
        if (null != conn) {
            conn.close();
        }
    }
}
