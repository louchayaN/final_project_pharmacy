package com.corporation.pharmacy.dao;

import com.corporation.pharmacy.dao.exception.DaoException;

/**
 * The Interface DAOManager.
 */
public interface DAOManager {

    /**
     * Returns the {@link UserDAO}
     *
     * @return the UserDAO
     */
    UserDAO getUserDAO();

    /**
     * Returns the {@link ProductDAO}
     *
     * @return the ProductDAO
     */
    ProductDAO getProductDAO();

    /**
     * Returns the {@link BasketDAO}
     *
     * @return the BasketDAO
     */
    BasketDAO getBasketDAO();

    /**
     * Returns the {@link PrescriptionDAO}
     *
     * @return the PrescriptionDAO
     */
    PrescriptionDAO getPrescriptionDAO();

    /**
     * Returns the {@link OrderDAO}
     *
     * @return the OrderDAO
     */
    OrderDAO getOrderDAO();

    /**
     * Start transaction.
     *
     * @throws DaoException
     *             the DaoException during transactional operations
     */
    void startTransaction() throws DaoException;

    /**
     * Sets the level of transaction isolation.
     *
     * @param level
     *            the new transaction isolation
     * @throws DaoException
     *             the DaoException during transactional operations
     */
    void setTransactionIsolation(int level) throws DaoException;

    /**
     * Commit transaction.
     *
     * @throws DaoException
     *             the DaoException during transactional operations
     */
    void commit() throws DaoException;

    /**
     * Rollback the transaction.
     *
     * @throws DaoException
     *             the DaoException during transactional operations
     */
    void rollback() throws DaoException;

    /**
     * Close connection.
     *
     * @throws DaoException
     *             the DaoException during transactional operations
     */
    void close() throws DaoException;

}
