package util;


import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
    private static volatile Util INSTANCE;
    private static final String DB_USERNAME = "db.username";
    private static final String DB_PASSWORD = "db.password";
    private static final String DB_URL = "db.url";
    private static final String DB_DRIVER_CLASS = "driver.class.name";

    public static Util getInstance() {
        if (null == INSTANCE) {
            synchronized (Util.class) {
                if (null == INSTANCE) {
                    INSTANCE = new Util();
                }
            }
        }
        return INSTANCE;
    }

    static {
        try(HikariDataSource datasource = new HikariDataSource();) {
            Properties properties = new Properties();
            properties.load(new FileInputStream("src/main/java/application.properties")); //
            datasource.setDriverClassName(properties.getProperty(DB_DRIVER_CLASS));
            datasource.setJdbcUrl(properties.getProperty(DB_URL));
            datasource.setUsername(properties.getProperty(DB_USERNAME));
            datasource.setPassword(properties.getProperty(DB_PASSWORD));
            datasource.setMinimumIdle(100);
            datasource.setMaximumPoolSize(1000000000);
            datasource.setAutoCommit(true);
            datasource.setLoginTimeout(3);
            datasource.getConnection();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}