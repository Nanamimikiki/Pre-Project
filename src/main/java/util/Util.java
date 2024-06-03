package util;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
    private static Connection connection;
    private static volatile Util INSTANCE;
    private static final String DB_USERNAME = "db.username";
    private static final String DB_PASSWORD = "db.password";
    private static final String DB_URL = "db.url";
    private static final String DB_DRIVER_CLASS = "driver.class.name";
    private static final String DB_POOL_SIZE = "db.pool.size";

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

    private Util() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = getDataSource().getConnection();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static DataSource getDataSource() throws IOException, SQLException {
        DataSource dataSource = new HikariDataSource(getDatasourceConfig());
        dataSource.setLoginTimeout(3);
        return dataSource;
    }

    private static HikariConfig getDatasourceConfig() throws IOException {
        Properties properties = getProperties();
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(properties.getProperty(DB_DRIVER_CLASS));
        config.setJdbcUrl(properties.getProperty(DB_URL));
        config.setUsername(properties.getProperty(DB_USERNAME));
        config.setPassword(properties.getProperty(DB_PASSWORD));
        config.setMinimumIdle(10);
        config.setMaximumPoolSize(Integer.parseInt(properties.getProperty(DB_POOL_SIZE)));
        config.setAutoCommit(true);
        return config;
    }

    private static Properties getProperties() throws IOException {
        try (FileInputStream fis = new FileInputStream("src/main/java/application.properties");) { //
            Properties properties = new Properties();
            properties.load(fis);
            return properties;
        } catch (IOException e) {
            throw new IOException("application.properties file not found", e);
        }
    }
}