package com.corporation.pharmacy.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.corporation.pharmacy.dao.DAOFactory;
import com.corporation.pharmacy.dao.DAOManager;
import com.corporation.pharmacy.dao.OrderDAO;
import com.corporation.pharmacy.dao.exception.DaoException;
import com.corporation.pharmacy.entity.LocaleType;
import com.corporation.pharmacy.entity.Order;
import com.corporation.pharmacy.service.OrderService;
import com.corporation.pharmacy.service.exception.ServiceException;

/**
 * It is a class for working with the user's orders.
 */
public class OrderServiceImpl implements OrderService {

    private static final Logger LOGGER = LogManager.getLogger(OrderServiceImpl.class);

    private static final DAOManager daoManager = DAOFactory.getFactory().getNonTransactionalDAOManager();

    private static final OrderDAO orderDAO = daoManager.getOrderDAO();

    /**
     * Places a new order of the user (defined by {@code idUser}). Using the
     * transaction makes three operations: copies all products from user's basket to
     * orders, decrease quantity of product in selling on the quantity of ordered
     * products, clean the basket of the user. If transaction was failed makes the
     * rollback.
     *
     * @param idUser
     *            the id of user
     * @throws ServiceException
     *             the service exception
     */
    @Override
    public void placeOrder(Integer idUser) throws ServiceException {
        DAOManager transactionalDAO = DAOFactory.getFactory().getTransactionalDAOManager();
        try {
            transactionalDAO.startTransaction();

            transactionalDAO.getOrderDAO().formOrderFromBasket(idUser);
            transactionalDAO.getProductDAO().decreaseQuantityOnBuyedProducts(idUser);
            transactionalDAO.getBasketDAO().deleteAllBasketItems(idUser);

            transactionalDAO.commit();
        } catch (DaoException e) {
            try {
                transactionalDAO.rollback();
            } catch (DaoException e1) {
                LOGGER.error("Exception during rollback transaction of adding order.", e1);
            }
            throw new ServiceException("Exception during placing final order.", e);
        } finally {
            try {
                transactionalDAO.close();
            } catch (DaoException e) {
                LOGGER.warn("Exception during closing transactional DAO.", e);
            }
        }
    }

    /**
     * Returns all orders of the user in according with the locale.
     *
     * @param locale
     *            the locale (language)
     * @param idUser
     *            the id of user
     * @return the orders of the user
     * @throws ServiceException
     *             the service exception
     * 
     * @see OrderServiceImpl#getOrderHistory
     */
    @Override
    public List<Order> getOrderHistory(LocaleType locale, Integer idUser) throws ServiceException {
        List<Order> orders;
        try {
            orders = orderDAO.getOrders(locale, idUser);
        } catch (DaoException e) {
            throw new ServiceException("Exception during getting order history of the user.", e);
        }
        return orders;
    }

}
