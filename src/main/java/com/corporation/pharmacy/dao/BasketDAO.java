package com.corporation.pharmacy.dao;

import java.util.List;
import java.util.Map;

import com.corporation.pharmacy.dao.exception.DaoException;
import com.corporation.pharmacy.entity.BasketItem;
import com.corporation.pharmacy.entity.LocaleType;
import com.corporation.pharmacy.entity.dto.BasketTO;

/**
 * The Interface BasketDAO.
 * 
 * Defines methods for work with 'basket' data base table.
 */
public interface BasketDAO {

    // ADD operations

    /**
     * Adds {@code  basketItem} to the data base or if it is already in user's
     * basket increases only its quantity.
     *
     * @param basketItem
     *            the basket item
     * @throws DaoException
     *             the exception during getting connection with data base or during
     *             working with data base.
     */
    void addBasketItem(BasketItem basketItem) throws DaoException;

    /**
     * Adds all basket items to user's basket. Or if one of them is already in
     * user's basket increases only their quantity.
     *
     * @param basket
     *            the basket of user that need to be added to the data base
     * @param idUser
     *            the id of user
     * @throws DaoException
     *             the exception during getting connection with data base or during
     *             working with data base.
     */
    void addAllBasketItems(Map<Integer, Integer> basket, Integer idUser) throws DaoException;

    // GET operations

    /**
     * Returns the basket of the user (is defined by {@code idUser}) according with
     * the {@code locale} .
     *
     * @param locale
     *            the locale
     * @param idUser
     *            the id of user
     * @return the basket of specified user representing as List of {@link BasketTO}
     *         objects consisting info according specified locale. Returns empty
     *         List if user's basket is empty
     * @throws DaoException
     *             the exception during getting connection with data base or during
     *             working with data base.
     */
    List<BasketTO> getBasket(LocaleType locale, Integer idUser) throws DaoException;

    /**
     * Returns the quantity of basket items in the basket of the specified user (by
     * {@code idUser}) that need getting or extending prescription.
     * 
     * @return the quantity of basket items in the basket of the specified user (by
     *         {@code idUser}) that need getting or extending prescription.
     * @throws DaoException
     *             the exception during getting connection with data base or during
     *             working with data base.
     */
    int getBasketItemsCountThatNeedPrescription(Integer idUser) throws DaoException;

    // UPDATE operations

    /**
     * Updates quantity of the specified basket item in user's basket.
     *
     * @param basketItem
     *            the basket item
     * @throws DaoException
     *             the exception during getting connection with data base or during
     *             working with data base.
     */
    void updateQuantityInBasket(BasketItem basketItem) throws DaoException;

    // DELETE operations

    /**
     * Deletes specified basket item from the basket.
     *
     * @param basketItem
     *            the basket item
     * @throws DaoException
     *             the exception during getting connection with data base or during
     *             working with data base.
     */
    void deleteBasketItem(BasketItem basketItem) throws DaoException;

    /**
     * Deletes all basket items from the basket of the specified user.
     *
     * @param idUser
     *            the id of user
     * @throws DaoException
     *             the exception during getting connection with data base or during
     *             working with data base.
     */
    void deleteAllBasketItems(Integer idUser) throws DaoException;

}
