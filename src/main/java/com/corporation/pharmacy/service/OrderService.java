package com.corporation.pharmacy.service;

import java.util.List;

import com.corporation.pharmacy.entity.LocaleType;
import com.corporation.pharmacy.entity.Order;
import com.corporation.pharmacy.service.exception.ServiceException;
import com.corporation.pharmacy.service.impl.OrderServiceImpl;

/**
 * The Interface OrderService.
 * 
 * Works with the user's orders.
 */
public interface OrderService {

    /**
     * Places a new order of the user (defined by {@code idUser}).
     *
     * @param idUser
     *            the id of user
     * @throws ServiceException
     *             the service exception
     * 
     * @see OrderServiceImpl#placeOrder
     */
    void placeOrder(Integer idUser) throws ServiceException;

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
    List<Order> getOrderHistory(LocaleType locale, Integer idUser) throws ServiceException;

}
