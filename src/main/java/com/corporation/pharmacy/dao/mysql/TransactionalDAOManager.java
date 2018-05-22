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
 * It is a factory of DAO implementation objects with defined connection.
 * Supports transactional operations.
 */
public class TransactionalDAOManager implements DAOManager {

    private Connection connection;

    protected TransactionalDAOManager(Connection connection) {
        this.connection = connection;
    }

    @Override
    public UserDAO getUserDAO() {
        return new UserDAOImpl(connection);
    }

    @Override
    public ProductDAO getProductDAO() {
        return new ProductDAOImpl(connection);
    }

    @Override
    public BasketDAO getBasketDAO() {
        return new BasketDAOImpl(connection);
    }

    @Override
    public PrescriptionDAO getPrescriptionDAO() {
        return new PrescriptionDAOImpl(connection);
    }

    @Override
    public OrderDAO getOrderDAO() {
        return new OrderDAOImpl(connection);
    }

    @Override
    public void startTransaction() throws DaoException {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void commit() throws DaoException {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void rollback() throws DaoException {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void close() throws DaoException {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void setTransactionIsolation(int level) throws DaoException {
        try {
            connection.setTransactionIsolation(level);
        } catch (SQLException e) {
            throw new DaoException(e);
        }

    }

}
