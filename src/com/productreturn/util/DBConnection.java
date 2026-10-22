package com.productreturn.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Database connection utility class.
 * <p>
 * Loads database configuration from {@code db.properties} on the classpath
 * or fallback locations, and provides a static factory method for obtaining JDBC connections.
 * </p>
 */
public class DBConnection {

    private static final String PROPERTIES_FILE = "db.properties";
    private static String url = "jdbc:mysql://localhost:3306/product_return_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static String username = "root";
    private static String password = "";
    private static String driver = "com.mysql.cj.jdbc.Driver";
    private static boolean initialized = false;

    static {
        init();
    }

    public static synchronized void init() {
        try {
            Properties props = new Properties();
            InputStream is = DBConnection.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);

            if (is == null) {
                File f = new File(PROPERTIES_FILE);
                if (f.exists()) {
                    is = new FileInputStream(f);
                }
            }

            // Also check Tomcat conf or CATALINA_BASE
            if (is == null) {
                String catalinaBase = System.getProperty("catalina.base");
                if (catalinaBase != null) {
                    File cf = new File(catalinaBase, "conf/" + PROPERTIES_FILE);
                    if (cf.exists()) {
                        is = new FileInputStream(cf);
                    }
                }
            }

            if (is == null) {
                File hf = new File(System.getProperty("user.home"), PROPERTIES_FILE);
                if (hf.exists()) {
                    is = new FileInputStream(hf);
                }
            }

            if (is != null) {
                try (InputStream stream = is) {
                    props.load(stream);
                    if (props.getProperty("db.driver") != null) driver = props.getProperty("db.driver");
                    if (props.getProperty("db.url") != null) url = props.getProperty("db.url");
                    if (props.getProperty("db.username") != null) username = props.getProperty("db.username");
                    if (props.getProperty("db.password") != null) password = props.getProperty("db.password");
                }
            }

            // Environment variable overrides
            String envUrl = System.getenv("DB_URL");
            if (envUrl != null && !envUrl.isBlank()) url = envUrl;
            String envUser = System.getenv("DB_USERNAME");
            if (envUser != null && !envUser.isBlank()) username = envUser;
            String envPass = System.getenv("DB_PASSWORD");
            if (envPass != null) password = envPass;

            Class.forName(driver);
            initialized = true;
        } catch (Exception e) {
            System.err.println("DBConnection init warning: " + e.getMessage());
        }
    }

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        if (!initialized) {
            init();
        }
        return DriverManager.getConnection(url, username, password);
    }

    public static String getUrl() {
        return url;
    }

    public static String getUsername() {
        return username;
    }
}
