package com.corporation.pharmacy.dao.mysql;

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
 * The Class NonTransactionalDAOManager is a factory for creating DAO
 * implementation objects as singletons. Doesn't support transactional
 * operations.
 */
public class NonTransactionalDAOManager implements DAOManager {

    /** Define the single instances of DAOImpls. */
    private static final UserDAO userDAO = new UserDAOImpl();
    private static final ProductDAO productDAO = new ProductDAOImpl();
    private static final BasketDAO basketDAO = new BasketDAOImpl();
    private static final PrescriptionDAO prescriptionDAO = new PrescriptionDAOImpl();
    private static final OrderDAO orderDAO = new OrderDAOImpl();

    /**
     * @return the single instance of UserDAO implementation .
     */
    @Override
    public UserDAO getUserDAO() {
        return userDAO;
    }

    /**
     * @return the single instance of UserDAO implementation .
     */
    @Override
    public ProductDAO getProductDAO() {
        return productDAO;
    }

    /**
     * @return the single instance of BasketDAO implementation .
     */
    @Override
    public BasketDAO getBasketDAO() {
        return basketDAO;
    }

    /**
     * @return the single instance of PrescriptionDAO implementation .
     */
    @Override
    public PrescriptionDAO getPrescriptionDAO() {
        return prescriptionDAO;
    }

    /**
     * @return the single instance of OrderDAO implementation .
     */
    @Override
    public OrderDAO getOrderDAO() {
        return orderDAO;
    }

    /**
     * @throws UnsupportedOperationException
     *             as the class doesn't support transactional operations
     */
    @Override
    public void startTransaction() throws DaoException {
        throw new UnsupportedOperationException();
    }

    /**
     * @throws UnsupportedOperationException
     *             as the class doesn't support transactional operations
     */
    @Override
    public void setTransactionIsolation(int level) throws DaoException {
        throw new UnsupportedOperationException();
    }

    /**
     * @throws UnsupportedOperationException
     *             as the class doesn't support transactional operations
     */
    @Override
    public void commit() throws DaoException {
        throw new UnsupportedOperationException();
    }

    /**
     * @throws UnsupportedOperationException
     *             as the class doesn't support transactional operations
     */
    @Override
    public void rollback() throws DaoException {
        throw new UnsupportedOperationException();
    }

    /**
     * @throws UnsupportedOperationException
     *             as the class doesn't support transactional operations
     */
    @Override
    public void close() throws DaoException {
        throw new UnsupportedOperationException();
    }

}
