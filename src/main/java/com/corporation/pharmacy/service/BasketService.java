package com.corporation.pharmacy.service;

import java.util.List;
import java.util.Map;

import com.corporation.pharmacy.entity.BasketItem;
import com.corporation.pharmacy.entity.LocaleType;
import com.corporation.pharmacy.entity.dto.BasketTO;
import com.corporation.pharmacy.service.exception.ServiceException;
import com.corporation.pharmacy.service.impl.BasketServiceImpl;

/**
 * Works with the user's basket.
 */
public interface BasketService {

    /**
     * Adds the basket item to user's basket.
     *
     * @param basketItem
     *            the basket item
     * @throws ServiceException
     *             the service exception
     * 
     * @see BasketServiceImpl#addBasketItem
     */
    void addBasketItem(BasketItem basketItem) throws ServiceException;

    /**
     * Adds all basket items (representing like a {@code Map<Integer, Integer>}) to
     * basket of user with the id {@code idUser}. The {@code Map<Integer, Integer>}
     * consists the id of product and its quantity like a pair key-value.
     *
     * @param basket
     *            the basket representing like a {@code Map<Integer, Integer>} where
     *            key is an id of product and value is its quantity.
     * 
     * @param idUser
     *            the id of user to whose basket basketItems will be added
     * @throws ServiceException
     *             the service exception
     * 
     * @see BasketServiceImpl#addAllBasketItems
     */
    void addAllBasketItems(Map<Integer, Integer> basket, Integer idUser) throws ServiceException;

    /**
     * Returns the basket of the user with the id {@code idUser} according with the
     * {@code locale}.
     *
     * @param locale
     *            the locale
     * @param idUser
     *            the id of user
     * @return the basket of the user like the List of BasketTO consisting info in
     *         according with the locale
     * @throws ServiceException
     *             the service exception
     * 
     * @see BasketServiceImpl#getBasket
     */
    List<BasketTO> getBasket(LocaleType locale, Integer idUser) throws ServiceException;

    /**
     * Gets detailed info about products consisting in the user basket in according
     * with chosen locale. Returns detailed basket.
     *
     * @param locale
     *            the locale
     * @param basket
     *            the basket
     * @return he basket of the user like the List of BasketTO consisting info in
     *         according with the locale
     * @throws ServiceException
     *             the service exception
     * 
     * @see BasketServiceImpl#formDetailedBasket
     */
    List<BasketTO> formDetailedBasket(LocaleType locale, Map<Integer, Integer> basket) throws ServiceException;

    /**
     * Change quantity of the basket item in the user's basket with the quantity
     * defined by {@code basketItem}.
     *
     * @param basketItem
     *            the basket item with information about new quantity of product in
     *            the basket
     * @throws ServiceException
     *             the service exception
     * 
     * @see BasketServiceImpl#changeQuantityInBasket
     */
    void changeQuantityInBasket(BasketItem basketItem) throws ServiceException;

    /**
     * Delete basket item from the user's basket.
     *
     * @param basketItem
     *            the basket item that is deleted
     * @throws ServiceException
     *             the service exception
     * 
     * @see BasketServiceImpl#deleteBasketItem
     */
    void deleteBasketItem(BasketItem basketItem) throws ServiceException;

}
