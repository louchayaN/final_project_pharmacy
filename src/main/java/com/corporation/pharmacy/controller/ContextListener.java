package com.corporation.pharmacy.controller;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.corporation.pharmacy.dao.connection.ConnectionPool;
import com.corporation.pharmacy.dao.connection.ConnectionPoolException;

/**
 * The Class ContextListener is for instantiating and destroying the connection
 * pool at the deployment and undeployment of application respectively.
 */
public class ContextListener implements ServletContextListener {

    private static final Logger LOGGER = LogManager.getLogger(ContextListener.class);

    /** Properties file with data base and connection pool configurations */
    private static final String DB_PROPERTIES_FILE = "db";

    /**
     * Initializing connection pool in according with configuration in properties
     * file.
     * 
     * @see {@link ServletContextListener#contextInitialized}
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        try {
            ConnectionPool.getInstance().initialize(DB_PROPERTIES_FILE);
        } catch (ConnectionPoolException e) {
            LOGGER.fatal("Connection pool wasn't initialized correctly.", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Destroy the connection pool.
     * 
     * @see {@link ServletContextListener#contextDestroyed}
     */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        try {
            ConnectionPool.getInstance().destroy();
        } catch (ConnectionPoolException e) {
            LOGGER.error("Connection pool wasn't dectroyed correctly.", e);
        }
    }

}
