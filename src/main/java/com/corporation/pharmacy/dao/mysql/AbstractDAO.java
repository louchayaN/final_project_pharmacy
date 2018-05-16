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

    /** The connection. */
    private Connection connection;

    /** Defines if the connection will be transactional or not. */
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

    /**
     * Returns the connection from the connection pool if its not instantiated yet
     * or return already instantiated connection for using in transactional
     * operations.
     *
     * @return the connection
     * @throws ConnectionPoolException
     *             the connection pool exception
     */
    public Connection getConnection() throws ConnectionPoolException {
        if (connection == null) {
            return ConnectionPool.getInstance().getConnection();
        } else {
            return connection;
        }
    }

    /**
     * Close non transactional connection. If it's not transactional don't do
     * anything.
     *
     * @param connection
     *            the connection
     * @throws SQLException
     *             the SQL exception during closing the connection
     */
    public void closeNonTransactionalConnection(Connection connection) throws SQLException {
        if ( ! transactional) {
            connection.close();
        }
    }

}
