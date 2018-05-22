package com.corporation.pharmacy.dao.mysql;

import java.sql.Connection;
import java.sql.SQLException;

import com.corporation.pharmacy.dao.connection.ConnectionPool;
import com.corporation.pharmacy.dao.connection.ConnectionPoolException;

/**
 * The Class AbstractDAO is used as superclass for another DAO classes working
 * with a data base.
 */
public class AbstractDAO {

    private Connection connection;

    private boolean transactional;

    /**
     * Instantiates a new abstract DAO for non transactional operations.
     */
    public AbstractDAO() {
    }

    /**
     * Instantiates a new abstract DAO for transactional operations.
     * 
     * @param connection
     *            the connection that can be transferred between different DAO
     */
    public AbstractDAO(Connection connection) {
        this.connection = connection;
        this.transactional = true;
    }

    public Connection getConnection() throws ConnectionPoolException {
        if (connection == null) {
            return ConnectionPool.getInstance().getConnection();
        } else {
            return connection;
        }
    }

    public void closeNonTransactionalConnection(Connection connection) throws SQLException {
        if ( ! transactional) {
            connection.close();
        }
    }

}
