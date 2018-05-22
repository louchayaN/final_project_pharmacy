package com.corporation.pharmacy.dao;

import com.corporation.pharmacy.dao.exception.DaoException;

/**
 * Defines the methods for DAO factories
 */
public interface DAOManager {

    UserDAO getUserDAO();

    ProductDAO getProductDAO();

    BasketDAO getBasketDAO();

    PrescriptionDAO getPrescriptionDAO();

    OrderDAO getOrderDAO();

    void startTransaction() throws DaoException;

    void setTransactionIsolation(int level) throws DaoException;

    void commit() throws DaoException;

    void rollback() throws DaoException;

    void close() throws DaoException;

}
