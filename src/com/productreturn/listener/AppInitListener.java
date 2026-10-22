package com.productreturn.listener;

import com.productreturn.util.SchemaMigrator;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

/**
 * Runs idempotent schema migrations when the web application starts.
 * A failure is logged but does not prevent the application from starting.
 */
public class AppInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            SchemaMigrator.migrate();
        } catch (Exception e) {
            System.err.println("[AppInitListener] Schema migration failed: " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
