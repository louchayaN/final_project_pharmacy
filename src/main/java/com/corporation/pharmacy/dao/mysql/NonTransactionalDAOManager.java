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
 * It is a factory for creating DAO implementation objects as singletons.
 * Doesn't support transactional operations.
 */
public class NonTransactionalDAOManager implements DAOManager {

    private static final UserDAO userDAO = new UserDAOImpl();
    private static final ProductDAO productDAO = new ProductDAOImpl();
    private static final BasketDAO basketDAO = new BasketDAOImpl();
    private static final PrescriptionDAO prescriptionDAO = new PrescriptionDAOImpl();
    private static final OrderDAO orderDAO = new OrderDAOImpl();

    @Override
    public UserDAO getUserDAO() {
        return userDAO;
    }

    @Override
    public ProductDAO getProductDAO() {
        return productDAO;
    }

    @Override
    public BasketDAO getBasketDAO() {
        return basketDAO;
    }

    @Override
    public PrescriptionDAO getPrescriptionDAO() {
        return prescriptionDAO;
    }

    @Override
    public OrderDAO getOrderDAO() {
        return orderDAO;
    }

    @Override
    public void startTransaction() throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTransactionIsolation(int level) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void commit() throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void rollback() throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() throws DaoException {
        throw new UnsupportedOperationException();
    }

}
