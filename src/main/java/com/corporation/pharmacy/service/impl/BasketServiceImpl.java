package com.corporation.pharmacy.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.corporation.pharmacy.dao.BasketDAO;
import com.corporation.pharmacy.dao.DAOFactory;
import com.corporation.pharmacy.dao.DAOManager;
import com.corporation.pharmacy.dao.ProductDAO;
import com.corporation.pharmacy.dao.exception.DaoException;
import com.corporation.pharmacy.entity.BasketItem;
import com.corporation.pharmacy.entity.LocaleType;
import com.corporation.pharmacy.entity.Product;
import com.corporation.pharmacy.entity.dto.BasketTO;
import com.corporation.pharmacy.service.BasketService;
import com.corporation.pharmacy.service.exception.ServiceException;

/**
 * The Class BasketServiceImpl is a class for working with the user's basket.
 */
public class BasketServiceImpl implements BasketService {

    /**
     * The single instance of non transactional DAOManager witch is a factory of DAO
     * that is used in non transactional operations.
     */
    private static final DAOManager daoManager = DAOFactory.getFactory().getNonTransactionalDAOManager();

    /**
     * The single instances of DAOs for non transactional operations.
     */
    private static final BasketDAO basketDAO = daoManager.getBasketDAO();
    private static final ProductDAO productDAO = daoManager.getProductDAO();

    /**
     * Adds the basket item to user's basket.
     *
     * @param basketItem
     *            the basket item
     * @throws ServiceException
     *             the service exception
     */
    @Override
    public void addBasketItem(BasketItem basketItem) throws ServiceException {
        try {
            basketDAO.addBasketItem(basketItem);
        } catch (DaoException e) {
            throw new ServiceException("Exception during saving new basket item in the basket.", e);
        }
    }

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
     */
    @Override
    public void addAllBasketItems(Map<Integer, Integer> basket, Integer idUser) throws ServiceException {
        try {
            basketDAO.addAllBasketItems(basket, idUser);
        } catch (DaoException e) {
            throw new ServiceException("Exception during saving basket items in the basket.", e);
        }
    }

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
     */
    @Override
    public List<BasketTO> getBasket(LocaleType locale, Integer idUser) throws ServiceException {
        List<BasketTO> basket;
        try {
            basket = basketDAO.getBasket(locale, idUser);
        } catch (DaoException e) {
            throw new ServiceException("Exception during getting basket for view.", e);
        }
        return basket;
    }

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
     */
    @Override
    public List<BasketTO> formDetailedBasket(LocaleType locale, Map<Integer, Integer> basket) throws ServiceException {
        try {
            Set<Integer> productsId = basket.keySet();
            List<Product> products = productDAO.getProductsById(locale, productsId);

            List<BasketTO> detailedBasket = new ArrayList<>();
            for (Product product : products) {
                BasketTO basketView = new BasketTO();
                basketView.setProduct(product);
                basketView.setOrderedQuantity(basket.get(product.getIdProduct()));
                detailedBasket.add(basketView);
            }

            return detailedBasket;
        } catch (DaoException e) {
            throw new ServiceException("Exception during forming basket for view.", e);
        }
    }

    /**
     * Change quantity of the basket item in the user's basket with the quantity
     * defined by {@code basketItem}.
     *
     * @param basketItem
     *            the basket item with information about new quantity of product in
     *            the basket
     * @throws ServiceException
     *             the service exception
     */
    @Override
    public void changeQuantityInBasket(BasketItem basketItem) throws ServiceException {
        try {
            basketDAO.updateQuantityInBasket(basketItem);
        } catch (DaoException e) {
            throw new ServiceException("Exception during changing quantity of item in the basket.", e);
        }
    }

    /**
     * Delete basket item from the user's basket.
     *
     * @param basketItem
     *            the basket item that is deleted
     * @throws ServiceException
     *             the service exception
     */
    @Override
    public void deleteBasketItem(BasketItem basketItem) throws ServiceException {
        try {
            basketDAO.deleteBasketItem(basketItem);
        } catch (DaoException e) {
            throw new ServiceException("Exception during deleting item from basket.", e);
        }
    }

}
