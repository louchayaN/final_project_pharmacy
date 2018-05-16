package com.corporation.pharmacy.dao.mysql;

import java.sql.Connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.corporation.pharmacy.dao.DAOFactory;
import com.corporation.pharmacy.dao.DAOManager;
import com.corporation.pharmacy.dao.connection.ConnectionPool;
import com.corporation.pharmacy.dao.connection.ConnectionPoolException;

/**
 * A factory for creating MySqlDAO objects.
 */
public class MySqlDAOFactory extends DAOFactory {

    private static final Logger LOGGER = LogManager.getLogger(MySqlDAOFactory.class);

    /** The Constant NonTransactionalDAOManager. */
    private static final DAOManager daoManager = new NonTransactionalDAOManager();

    /**
     * @return the instance of non transactional DAOManager as singleton
     */
    @Override
    public DAOManager getNonTransactionalDAOManager() {
        return daoManager;
    }

    /**
     * Gets connection from the connection pool and returns transactional DAOManager
     * instantiated with this connection.
     * 
     * @return transactional DAOManager
     */
    @Override
    public DAOManager getTransactionalDAOManager() {
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
        } catch (ConnectionPoolException e) {
            LOGGER.error("Exception during getting a connection from connection pool", e);
            throw new RuntimeException("Exception during getting a connection from connection pool.", e);
        }
        return new TransactionalDAOManager(connection);
    }

}
