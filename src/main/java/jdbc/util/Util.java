package jdbc.util;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
    private static Connection connection;
    private static SessionFactory sessionFactory;
    private static volatile Util INSTANCE;
    private static final String DB_USERNAME = "db.username";
    private static final String DB_PASSWORD = "db.password";
    private static final String DB_URL = "db.url";
    private static final String DB_DRIVER_CLASS = "driver.class.name";
    private static final String DB_POOL_SIZE = "db.pool.size";
    private static final String DB_MIN_IDLE = "db.min.idle";

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

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(getSessionConfiguration().getProperties()).build();
                sessionFactory = getSessionConfiguration().buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
    public static Configuration getSessionConfiguration() {
        Configuration configuration = new Configuration();
        Properties settings = new Properties();
        settings.put(Environment.DRIVER, "org.postgresql.Driver");
        settings.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
        settings.put(Environment.URL, "jdbc:postgresql://localhost:5434/PreProject?Encoding=UTF-8");
        settings.put(Environment.USER, "root");
        settings.put(Environment.PASS, "root");
        settings.put(Environment.SHOW_SQL, "true");
        settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        settings.put(Environment.HBM2DDL_AUTO, "create-drop");
        configuration.setProperties(settings);
        configuration.addAnnotatedClass(User.class);
        return configuration;
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
        config.setMinimumIdle(Integer.parseInt(properties.getProperty(DB_MIN_IDLE)));
        config.setMaximumPoolSize(Integer.parseInt(properties.getProperty(DB_POOL_SIZE)));
        config.setAutoCommit(true);
        return config;
    }

    private static Properties getProperties() throws IOException {
        try (FileInputStream fis = new FileInputStream("src/main/resources/application.properties")) { //
            Properties properties = new Properties();
            properties.load(fis);
            return properties;
        } catch (IOException e) {
            throw new IOException("application.properties file not found", e);
        }
    }
}