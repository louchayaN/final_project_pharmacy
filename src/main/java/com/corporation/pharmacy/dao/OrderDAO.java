package com.corporation.pharmacy.dao;

import java.util.List;

import com.corporation.pharmacy.dao.exception.DaoException;
import com.corporation.pharmacy.entity.LocaleType;
import com.corporation.pharmacy.entity.Order;

/**
 * The Interface OrderDAO.
 * 
 * Defines methods for work with user orders.
 */
public interface OrderDAO {

    // ADD operations

    /**
     * Places all items from the basket of the user with the specified
     * <code>id</code> to user's orders.
     *
     * @param idUser
     *            the id of user
     * @throws DaoException
     *             the exception during getting connection with data base or during
     *             working with data base.
     */
    void formOrderFromBasket(Integer idUser) throws DaoException;

    // GET operations

    /**
     * Gets the orders of the user with this <code>id</code> consisting info in
     * according with the <code>locale</code>. Returns the List of orders. Returns
     * empty List if the user don't have any orders.
     *
     * @param locale
     *            the locale (language)
     * @param idUser
     *            the id of user
     * @return the List of orders of the user. The List will be empty if the user
     *         don't have any orders.
     * @throws DaoException
     *             the exception during getting connection with data base or during
     *             working with data base.
     */
    List<Order> getOrders(LocaleType locale, Integer idUser) throws DaoException;

}
