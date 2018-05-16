package com.corporation.pharmacy.dao.mysql;

import java.sql.Connection;
import java.sql.SQLException;

import com.corporation.pharmacy.dao.BasketDAO;
import com.corporation.pharmacy.dao.DAOManager;
import com.corporation.pharmacy.dao.OrderDAO;
import com.corporation.pharmacy.dao.PrescriptionDAO;
import com.corporation.pharmacy.dao.ProductDAO;
import com.corporation.pharmacy.dao.UserDAO;
import com.corporation.pharmacy.dao.exception.DaoException;
import com.corporation.pharmacy.dao.mysql.impl.BasketDAOImpl;
import com.corporation.pharmacy.dao.mysql.impl.OrderDAOImpl;
import com.corporation.pharmacy.dao.mysql.impl.PrescriptionDAOImpl;
import com.corporation.pharmacy.dao.mysql.impl.ProductDAOImpl;
import com.corporation.pharmacy.dao.mysql.impl.UserDAOImpl;

/**
 * The Class TransactionalDAOManager is a factory of DAO implementation objects
 * with defined connection. Supports transactional operations.
 */
public class TransactionalDAOManager implements DAOManager {

    /** The connection. */
    private Connection connection;

    /**
     * Instantiates a new transactional DAOManager with defined connection.
     *
     * @param connection
     *            the connection for transactional operations
     */
    protected TransactionalDAOManager(Connection connection) {
        this.connection = connection;
    }

    /**
     * @return new UserDAO implementation with defined connection.
     */
    @Override
    public UserDAO getUserDAO() {
        return new UserDAOImpl(connection);
    }

    /**
     * @return new ProductDAO implementation with defined connection.
     */
    @Override
    public ProductDAO getProductDAO() {
        return new ProductDAOImpl(connection);
    }

    /**
     * @return new BasketDAO implementation with defined connection.
     */
    @Override
    public BasketDAO getBasketDAO() {
        return new BasketDAOImpl(connection);
    }

    /**
     * @return new PrescriptionDAO implementation with defined connection.
     */
    @Override
    public PrescriptionDAO getPrescriptionDAO() {
        return new PrescriptionDAOImpl(connection);
    }

    /**
     * @return new OrderDAO implementation with defined connection.
     */
    @Override
    public OrderDAO getOrderDAO() {
        return new OrderDAOImpl(connection);
    }

    /**
     * Starts the transaction by calling {@code setAutoCommit(false)} on the
     * connection of this DAO implementation.
     */
    @Override
    public void startTransaction() throws DaoException {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * Ends the transaction by calling {@code commit()} on the connection of this
     * DAO implementation.
     */
    @Override
    public void commit() throws DaoException {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * Rollback the transaction by calling {@code rollback()} on the connection of
     * this DAO implementation.
     */
    @Override
    public void rollback() throws DaoException {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * Closes the transaction by calling {@code close()} on the connection of this
     * DAO implementation.
     */
    @Override
    public void close() throws DaoException {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    /**
     * Sets the transaction isolation level by calling
     * {@code setTransactionIsolation(level)} on the connection of this DAO
     * implementation.
     */
    @Override
    public void setTransactionIsolation(int level) throws DaoException {
        try {
            connection.setTransactionIsolation(level);
        } catch (SQLException e) {
            throw new DaoException(e);
        }

    }

}
